(ns sk.routes.home
  (:require [cheshire.core :refer [generate-string]]
            [sk.models.crud :refer [db Query]]
            [sk.models.util :refer [get-session-id]]
            [noir.session :as session]
            [noir.util.crypt :as crypt]
            [noir.response :refer [redirect]]
            [selmer.parser :refer [render-file]]))

(defn main [request]
  (render-file "routes/main.html" {:title "Welcome to the Jungle!"}))

(defn login [_]
  (if-not (nil? (get-session-id))
    (redirect "/")
    (render-file "routes/login.html" {:title "Accesar al Sitio!"})))

(defn login! [username password]
  (let [row    (first (Query db ["select * from users where username = ?" username]))
        active (:active row)]
    (if (= active "T")
      (do
        (if (crypt/compare password (:password row))
          (do
            (session/put! :user_id (:id row))
            (generate-string {:url "/"}))
          (generate-string {:error "Hay problemas para accesar el sitio!"})))
      (generate-string {:error "El usuario esta inactivo!"}))))

(defn logoff []
  (session/clear!)
  (redirect "/"))

