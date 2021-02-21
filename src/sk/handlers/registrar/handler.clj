(ns sk.handlers.registrar.handler
  (:require [cheshire.core :refer [generate-string]]
            [clojure.string :as str]
            [sk.layout :refer [application
                               error-404]]
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
  [_]
  (try
    (let [title "Registro de Usuarios"
          token (anti-forgery-field)
          ok (get-session-id)
          js (registrar-scripts)
          error-text "Existe una session, no se puede crear un usuario nuevo!"
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
    (let [email (str/lower-case (or (:email params) "0"))
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
        (generate-string {:error "Incapaz de registrar usuario!"})))
    (catch Exception e (.getMessage e))))
;; End registrar

;; Start reset-password
(defn reset-password
  [_]
  (try
    (let [title "Resetear Contraseña"
          token (anti-forgery-field)
          ok (get-session-id)
          error-text "Existe una session, no se puede crear un nuevo usuario!"
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
          subject      "Resetear su contraseña"
          content      (str "<strong>Hola</strong> " nombre ",</br></br>"
                            "Para resetear su contraseña <strong>" "<a href='" url "'>Clic Aqui</a>" "</strong>.</br></br>"
                            "Como alternativa usted puede copiar y pegar el siguiente enlace en su navegador ex. Firefox, Chrome:</br></br>"
                            url "</br></br>"
                            "Este enlace es valido solo por 10 minutos.</br></br>"
                            "Si usted no intento cambiar su contraseña o no desea cambiarla, simplemente ignore este mensage.</br></br></br>"
                            "Sinceramente,</br></br>"
                            "La Administración")
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
        (generate-string {:error "Incapaz de resetear su contraseña!"})))
    (catch Exception e (.getMessage e))))
;; End reset-password

(defn reset-jwt
  [token]
  (try
    (let [title "Resetear Contraseña"
          csrf (anti-forgery-field)
          ok (get-session-id)
          username (check-token token)
          error-text "Su token es incorrecto o ya se expiro!"
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
        (generate-string {:error "Incapaz de resetear su contraseña!"})))
    (catch Exception e (.getMessage e))))
;; End reset-password

