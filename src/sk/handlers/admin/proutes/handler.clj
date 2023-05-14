(ns sk.handlers.admin.proutes.handler
  (:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.handlers.admin.proutes.view :refer [proutes-view proutes-scripts]]))

(defn proutes [_]
  (let [title "Private Routes"
        ok (get-session-id)
        js (proutes-scripts)
        content (proutes-view title)]
    (if
     (= (user-level) "S")
      (application title ok js content)
      (application title ok nil "solo <strong>los administradores </strong> pueden accessar esta opción!!!"))))

(defn proutes-grid
  "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
  [{params :params}]
  (let [table "proutes"
        args {:sort-extra "id"}]
    (build-grid params table args)))

(defn proutes-form [id]
  (let [table "proutes"]
    (build-form-row table id)))

(defn proutes-save [{params :params}]
  (let [table "proutes"]
    (build-form-save params table)))

(defn proutes-delete [{params :params}]
  (let [table "proutes"]
    (build-form-delete params table)))
