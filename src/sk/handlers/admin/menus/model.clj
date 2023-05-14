(ns sk.handlers.admin.menus.model
  (:require [sk.models.crud :refer [db Query]]))

(defn get-rows [tabla]
  (Query db [(str "select * from " tabla)]))

(comment
  (get-rows "menus"))
