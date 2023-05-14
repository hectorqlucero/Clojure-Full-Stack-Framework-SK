(ns sk.handlers.admin.routes.handler
  (:require [sk.handlers.admin.routes.view :refer [routes-scripts routes-view]]
            [sk.layout :refer [application]]
            [sk.models.crud :refer [build-form-delete build-form-row
                                    build-form-save]]
            [sk.models.grid :refer [build-grid]]
            [sk.models.util :refer [get-session-id user-level]]))

(defn routes [_]
  (let [title "Open Routes"
        ok (get-session-id)
        js (routes-scripts)
        content (routes-view title)]
    (if
     (= (user-level) "S")
      (application title ok js content)
      (application title ok nil "solo <strong>los administradores </strong> pueden accessar esta opción!!!"))))

(defn routes-grid
  "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
  [{params :params}]
  (let [table "routes"
        args {:sort-extra "id"}]
    (build-grid params table args)))

(defn routes-form [id]
  (let [table "routes"]
    (build-form-row table id)))

(defn routes-save [{params :params}]
  (let [table "routes"]
    (build-form-save params table)))

(defn routes-delete [{params :params}]
  (let [table "routes"]
    (build-form-delete params table)))
