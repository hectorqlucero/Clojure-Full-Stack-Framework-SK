(ns sk.handlers.admin.users.handler
  (:require [sk.models.crud :refer [build-form-row
                                    build-form-save
                                    build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.models.util :refer [get-session-id
                                    user-level]]
            [sk.layout :refer [application]]
            [sk.handlers.admin.users.view :refer [users-view users-scripts]]))

(defn users
  [_]
  (try
    (let [title "Usuarios"
          ok (get-session-id)
          js (users-scripts)
          content (users-view title)
          level (user-level)]
      (if
        (or
          (= (user-level) "A")
          (= (user-level) "S"))
        (application title ok js content)
        (application title ok nil "Solo <strong>administradores</strong> pueden accesar esta opción!!!")))
    (catch Exception e (.getMessage e))))

(defn users-grid
  [{params :params}]
  (try
    (let [table "users"
          args {:sort-extra "lastname,firstname"}]
      (build-grid params table args))
    (catch Exception e (.getMessage e))))

(defn users-form
  [id]
  (try
    (let [table "users"]
      (build-form-row table id))
    (catch Exception e (.getMessage e))))

(defn users-save
  [{params :params}]
  (try
    (let [table "users"]
      (build-form-save params table))
    (catch Exception e (.getMessage e))))

(defn users-delete
  [{params :params}]
  (try
    (let [table "users"]
      (build-form-delete params table))
    (catch Exception e (.getMessage e))))
