(ns sk.handlers.home.handler
  (:require [cheshire.core :refer [generate-string]]
            [hiccup.page :refer [html5]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.crud :refer [db
                                    Query]]
            [sk.models.util :refer [get-session-id]]
            [sk.layout :refer :all]
            [sk.handlers.home.view :refer [login-view login-script]]
            [noir.session :as session]
            [noir.util.crypt :as crypt]
            [noir.response :refer [redirect]]))

;; Start Main
(def main-sql
  "SELECT
   username
   FROM users
   WHERE id = ?")

(defn get-main-title 
  []
  (try
    (let [id (get-session-id)
          title (if (> id 0)
                  (str "<strong>User:</strong> " (:username (first (Query db [main-sql id]))))
                  "Click on the Login Menu to access site.")]
      title)
    (catch Exception e (.getMessage e))))

(defn main 
  [request]
  (try
    (let [title (get-main-title)
          ok (get-session-id)
          content [:div [:span {:style "margin-left:20px;"} (get-main-title)]] ]
      (application title ok nil content))
    (catch Exception e (.getMessage e))))
;; End Main

;; Start Login
(defn login 
  [_]
  (try
    (let [title "Login"
          ok (get-session-id)
          content (login-view (anti-forgery-field))
          scripts (login-script)]
      (if-not (= (get-session-id) 0)
        (redirect "/")
        (application title ok scripts content)))
    (catch Exception e (.getMessage e))))

(defn login! 
  [username password]
  (try
    (let [row (first (Query db ["SELECT * FROM users WHERE username = ?" username]))
          active (:active row)]
      (if (= active "T")
        (do
          (if (crypt/compare password (:password row))
            (do
              (session/put! :user_id (:id row))
              (generate-string {:url "/"}))
            (generate-string {:error "Unable to access the site!"})))
        (generate-string {:error "The user is inactive!"})))
    (catch Exception e (.getMessage e))))
;; End login

(defn logoff 
  []
  (try
    (session/clear!)
    (redirect "/")
    (catch Exception e (.getMessage e))))
