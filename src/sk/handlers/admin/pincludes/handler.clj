(ns sk.handlers.admin.pincludes.handler
  (:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.handlers.admin.pincludes.view :refer [pincludes-view pincludes-scripts]]))

(defn pincludes [_]
  (let [title "Include Libraries"
        ok (get-session-id)
        js (pincludes-scripts)
        content (pincludes-view title)]
    (if (= (user-level) "S")
      (application title ok js content)
      (application title ok nil "solo <strong>los administradores </strong> pueden accessar esta opción!!!"))))

(defn pincludes-grid
  "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
  [{params :params}]
  (let [table "pincludes"
        args {:sort-extra "id"}]
    (build-grid params table args)))

(defn pincludes-form [id]
  (let [table "pincludes"]
    (build-form-row table id)))

(defn pincludes-save [{params :params}]
  (let [table "pincludes"]
    (build-form-save params table)))

(defn pincludes-delete [{params :params}]
  (let [table "pincludes"]
    (build-form-delete params table)))
