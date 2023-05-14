(ns sk.handlers.admin.rincludes.handler
  (:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.handlers.admin.rincludes.view :refer [rincludes-view rincludes-scripts]]))

(defn rincludes [_]
  (let [title "Open Includes"
        ok (get-session-id)
        js (rincludes-scripts)
        content (rincludes-view title)]
    (if (= (user-level) "S")
      (application title ok js content)
      (application title ok nil "solo <strong>los administradores nivel sistema </strong> pueden accessar esta opción!!!"))))

(defn rincludes-grid
  "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
  [{params :params}]
  (let [table "rincludes"
        args {:sort-extra "id"}]
    (build-grid params table args)))

(defn rincludes-form [id]
  (let [table "rincludes"]
    (build-form-row table id)))

(defn rincludes-save [{params :params}]
  (let [table "rincludes"]
    (build-form-save params table)))

(defn rincludes-delete [{params :params}]
  (let [table "rincludes"]
    (build-form-delete params table)))
