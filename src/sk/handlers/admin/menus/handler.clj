(ns sk.handlers.admin.menus.handler
  (:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.handlers.admin.menus.view :refer [menus-view menus-scripts]]))

(defn menus [_]
  (let [title "Menus"
        ok (get-session-id)
        js (menus-scripts)
        content (menus-view title)]
    (if (= (user-level) "S")
      (application title ok js content)
      (application title ok nil "solo <strong>los administradores </strong> pueden accessar esta opción!!!"))))

(defn menus-grid
  "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
  [{params :params}]
  (let [table "menus"
        args {:sort-extra "id"}]
    (build-grid params table args)))

(defn menus-form [id]
  (let [table "menus"]
    (build-form-row table id)))

(defn menus-save [{params :params}]
  (let [table "menus"]
    (build-form-save params table)))

(defn menus-delete [{params :params}]
  (let [table "menus"]
    (build-form-delete params table)))
