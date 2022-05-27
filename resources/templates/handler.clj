(ns sk.handlers.xxx.handler
  (:require [sk.models.crud :refer [build-form-row
                                    build-form-save
                                    build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id]]
            [sk.handlers.xxx.view :refer [xxx-view
                                          xxx-scripts]]))
;; Start instrucciones
"Cambiar todas las 'xxx' por el nombre del folder"
"Cambiar 'Titulo' por el verdadero titulo"
"Cambiar 'tabla' por el nombre de la tabla de la base de datos"
;; End instrucciones

(defn xxx
  [_]
  (try
    (let [title "Titulo"
          ok (get-session-id)
          js (xxx-scripts)
          content (xxx-view title)]
      (application title ok js content))
    (catch Exception e (.getMessage e))))

(defn xxx-grid
  [{params :params}]
  (try
    (let [table "tabla"]
      (build-grid params table))
    (catch Exception e (.getMessage e))))

(defn xxx-form
  [id]
  (try
    (let [table "tabla"]
      (build-form-row table id))
    (catch Exception e (.getMessage e))))

(defn xxx-save
  [{params :params}]
  (try
    (let [table "tabla"]
      (build-form-save params table))
    (catch Exception e (.getMessage e))))

(defn xxx-delete
  [{params :params}]
  (try
    (let [table "tabla"]
      (build-form-delete params table))
    (catch Exception e (.getMessage e))))
