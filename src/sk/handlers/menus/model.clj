(ns sk.handlers.menus.model
  (:require [sk.models.crud :refer [db Query]]))

;; Start build-open-menus
(def open-menus-sql
  (str
   "
    SELECT
    *
    FROM menus
    WHERE
    (type = 'O' or type = 'B')
    and secure = '0'
    ORDER BY description
    "))

(defn build-open-menus-rows []
  (let [rows (Query db open-menus-sql)]
    rows))
;; End build-open-menus

;; Start build-private-menus-rows
(def private-menus-sql
  (str
   "
    SELECT
    *
    FROM menus
    WHERE
    (type = 'P' or type = 'B')
   and admin = 'F' 
   and (secure = 3 or secure = 0)
    ORDER BY description
    "))

(defn build-private-menus-rows []
  (let [rows (Query db private-menus-sql)]
    rows))
;; End build-private-menus-rows

;; Start build-private-user-admin-menus-rows
(def private-user-admin-sql
  (str
   "
    SELECT
    *
    FROM menus
    WHERE
    type = 'P' and
    admin = 'T' and
    secure = 3
    ORDER BY description
    "))

(defn build-private-user-admin-menus-rows []
  (let [rows (Query db private-user-admin-sql)]
    rows))
;; ENd build-private-user-admin-menus-rows

;; Start build-private-admin-admin-menus-rows
(def private-admin-admin-sql
  (str
   "
    SELECT
    *
    FROM menus
    WHERE
    type = 'P' and
    admin = 'T' and
    secure = 1
    ORDER BY description
    "))

(defn build-private-admin-admin-menus-rows []
  (let [rows (Query db private-admin-admin-sql)]
    rows))
;; End build-private-admin-admin-menus-rows

;; Start build-private-admin-system-menus-rows
(def private-admin-system-sql
  (str
   "
    SELECT
    *
    FROM menus
    WHERE
    type = 'P' and
    admin = 'T' and
    secure = 2
    ORDER BY description
    "))

(defn build-private-admin-system-menus-rows []
  (let [rows (Query db private-admin-system-sql)]
    rows))
;; End build-private-admin-system-menus-rows

(comment
  (build-private-admin-system-menus-rows)
  (build-private-admin-admin-menus-rows)
  (build-private-user-admin-menus-rows)
  (build-private-menus-rows)
  (build-open-menus-rows))
