(ns sk.models.cdb
  (:require [noir.util.crypt :as crypt]
            [sk.models.crud :refer [db Insert-multi Query!]]))

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
  (populate-tables "users" users-rows))
