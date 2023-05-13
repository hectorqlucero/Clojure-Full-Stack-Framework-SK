(ns sk.proutes
  (:require [compojure.core :refer [defroutes GET POST]]
            [sk.handlers.admin.menus.handler :as menus]
            [sk.handlers.admin.users.handler :as users]))

(defroutes proutes
  ;; Start menus
  (GET "/admin/menus"  req [] (menus/menus req))
  (POST "/admin/menus" req [] (menus/menus-grid req))
  (GET "/admin/menus/edit/:id" [id] (menus/menus-form id))
  (POST "/admin/menus/save" req [] (menus/menus-save req))
  (POST "/admin/menus/delete" req [] (menus/menus-delete req))
  ;; End menus

  ;; Start users
  (GET "/admin/users"  req [] (users/users req))
  (POST "/admin/users" req [] (users/users-grid req))
  (GET "/admin/users/edit/:id" [id] (users/users-form id))
  (POST "/admin/users/save" req [] (users/users-save req))
  (POST "/admin/users/delete" req [] (users/users-delete req))
  ;; End users
  )
