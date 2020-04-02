(ns sk.handlers.registrar.handler
  (:require [cheshire.core :refer [generate-string]]
            [sk.layout :refer :all]
            [sk.handlers.registrar.view :refer [registrar-scripts
                                                registrar-view
                                                reset-password-scripts
                                                reset-password-view
                                                reset-jwt-scripts
                                                reset-jwt-view]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [noir.util.crypt :as crypt]
            [sk.models.crud :refer [db 
                                    config
                                    build-postvars
                                    Query 
                                    Save 
                                    Update]]
            [sk.models.email :refer [host 
                                     send-email]]
            [sk.models.util :refer [get-session-id
                                    get-reset-url
                                    check-token
                                    create-token]]))

;; Start registrar
(defn registrar 
  [request]
  (try
    (let [title "Register Users"
          token (anti-forgery-field)
          ok (get-session-id)
          js (registrar-scripts)
          error-text "A session exists, cannot create a new user"
          return-url "/"
          content (registrar-view title token)]
      (if (> ok 0)
        (error-404 error-text return-url)
        (application title ok js content)))
    (catch Exception e (.getMessage e))))

(defn registrar!
  "Post user data to the users table"
  [{params :params}]
  (try
    (let [email (clojure.string/lower-case (or (:email params) "0"))
          password (:password params)
          params (assoc params 
                        :level "u"
                        :active "t"
                        :password (crypt/encrypt password)
                        :email email
                        :username email)
          postvars (assoc (build-postvars "users" params)
                          :email email
                          :username email)
          result (Save db :users postvars ["username = ?" email])]
      (if (seq result)
        (generate-string {:url "/login"})
        (generate-string {:error "Unable to register user!"})))
    (catch Exception e (.getMessage e))))
;; End registrar

;; Start reset-password
(defn reset-password 
  [request]
  (try
    (let [title "Reset Password"
          token (anti-forgery-field)
          ok (get-session-id)
          error-text "A session exists, cannot create a new user"
          return-url "/"
          js (reset-password-scripts)
          content (reset-password-view title token)]
      (if (> ok 0)
        (error-404 error-text return-url)
        (application title ok js content)))
    (catch Exception e (.getMessage e))))

(defn get-username-row 
  [username]
  (try
    (first (Query db ["SELECT * FROM users WHERE username = ?" username]))
    (catch Exception e (.getMessage e))))

(defn email-body
  "Create the email body"
  [row url]
  (try
    (let [nombre       (str (:firstname row) " " (:lastname row))
          email        (:email row)
          subject      "Reset your password"
          content      (str "<strong>Hi</strong> " nombre ",</br></br>"
                            "To reset your password <strong>" "<a href='" url "'>Click Here</a>" "</strong>.</br></br>"
                            "Alternatively you can copy and paste the following link on your browsers address field:</br></br>"
                            url "</br></br>"
                            "This link is only good for 10 minutes.</br></br>"
                            "If you did not request to change your password or don't want to change your password simply ignore this message.</br></br></br>"
                            "Sincerely,</br></br>"
                            "The administration")
          body         {:from    (:email-user config)
                        :to      email
                        :subject subject
                        :body    [{:type    "text/html;charset=utf-8"
                                   :content content}]}]
      body)
    (catch Exception e (.getMessage e))))

(defn reset-password! 
  [request]
  (try
    (let [params     (:params request)
          username   (:email params)
          token      (create-token username)
          url        (get-reset-url request token)
          row        (get-username-row username)
          email-body (email-body row url)]
      (if (future (send-email host email-body))
        (generate-string {:url "/"})
        (generate-string {:error "Unable to reset your password!"})))
    (catch Exception e (.getMessage e))))
;; End reset-password

(defn reset-jwt 
  [token]
  (try
    (let [title "Reset Password"
          csrf (anti-forgery-field)
          ok (get-session-id)
          username (check-token token)
          error-text "Your token is incorrect or it has expired!"
          return-url "/"
          js (reset-jwt-scripts)
          content (reset-jwt-view title csrf username)]
      (if-not (nil? username)
        (application title ok js content)
        (error-404 error-text return-url)))
    (catch Exception e (.getMessage e))))

(defn reset-jwt! 
  [{params :params}]
  (try
    (let [username (or (:username params) "0")
          postvars  {:username username
                     :password  (crypt/encrypt (:password params))}
          result (Update db :users postvars ["username = ?" username])]
      (if (seq result)
        (generate-string {:url "/"})
        (generate-string {:error "Unable to reset your password!"})))
    (catch Exception e (.getMessage e))))
;; End reset-password

