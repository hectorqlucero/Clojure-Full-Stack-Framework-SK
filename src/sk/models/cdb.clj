(ns sk.models.cdb
  (:require [noir.util.crypt :as crypt]
            [sk.models.crud :refer [db Insert-multi Query!]]))

(def menus-rows
  [{:id 1
    :type "O"
    :admin "F"
    :secure 0
    :root "src/sk/handlers/"
    :link "/home/login"
    :description "Conectar"}
   {:id 2
    :type "P"
    :admin "T"
    :secure 2
    :root "scr/sk/handlers/admin/"
    :link "/admin/menus"
    :description "Menus"}
   {:id 3
    :type "P"
    :admin "T"
    :secure 2
    :root "scr/sk/handlers/admin/"
    :link "/admin/users"
    :description "Usuarios"}])

(def users-rows
  [{:lastname  "User"
    :firstname "Regular"
    :username  "user@gmail.com"
    :password  (crypt/encrypt "user")
    :dob       "1957-02-07"
    :email     "user@gmail.com"
    :level     "U"
    :active    "T"}
   {:lastname "User"
    :firstname "Admin"
    :username "admin@gmail.com"
    :password (crypt/encrypt "admin")
    :dob "1957-02-07"
    :email "admin@gmail.com"
    :level "A"
    :active "T"}
   {:lastname "User"
    :firstname "Sistema"
    :username "sistema@gmail.com"
    :password (crypt/encrypt "sistema")
    :dob "1957-02-07"
    :email "sistema@gmail.com"
    :level "S"
    :active "T"}])
;; End users table

(defn populate-tables
  "Populates table with default data"
  [table rows]
  (Query! db (str "LOCK TABLES " table "WRITE;"))
  (Insert-multi db (keyword table) rows)
  (Query! db "UNLOCK TABLES;"))

(comment
  (populate-tables "menus" menus-rows)
  (populate-tables "users" users-rows))
