(ns sk.handlers.home.handler
  (:require [cheshire.core :refer [generate-string]]
            [clojure.string :as st]
            [noir.response :refer [redirect]]
            [noir.session :as session]
            [noir.util.crypt :as crypt]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.handlers.home.view :refer [login-script login-view]]
            [sk.layout :refer [application error-404]]
            [sk.migrations :refer [config]]
            [sk.models.crud :refer [db Query]]
            [sk.models.util :refer [get-session-id]]))

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
                  "Clic on <strong>Login</strong> access site.")]
      title)
    (catch Exception e (.getMessage e))))

(defn main
  [_]
  (try
    (let [title (:site config)
          ok (get-session-id)
          content (get-main-title)]
      (application title ok nil content))
    (catch Exception e (.getMessage e))))
;; End main

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
    (let [row (first (Query db ["SELECT * FROM users WHERE LOWER(username) = ?" (st/lower-case username)]))
          active (:active row)]
      (if (= active "T")
        (if (crypt/compare password (:password row))
          (do
            (session/put! :user_id (:id row))
            (generate-string {:url "/"}))
          (generate-string {:error "Unable to access site!"}))
        (generate-string {:error "User is inactive!"})))
    (catch Exception e (.getMessage e))))
;; End login

(defn logoff
  []
  (try
    (session/clear!)
    (error-404 "Logoff successful!" "/")
    (catch Exception e (.getMessage e))))
