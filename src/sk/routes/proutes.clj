(ns sk.routes.proutes
  (:require
   [compojure.core :refer [defroutes GET POST]]
   [sk.handlers.admin.menus.handler :as menus]
   [sk.handlers.admin.users.handler :as users]
   [sk.handlers.admin.pincludes.handler :as pincludes]
   [sk.handlers.admin.proutes.handler :as proutes]
   [sk.handlers.admin.rincludes.handler :as rincludes]
   [sk.handlers.admin.routes.handler :as routes]))

(defroutes proutes
  (GET "/admin/menus" req [] (menus/menus req))
  (POST "/admin/menus" req [] (menus/menus-grid req))
  (GET "/admin/menus/edit/:id" [id] (menus/menus-form id))
  (POST "/admin/menus/save" req [] (menus/menus-save req))
  (POST "/admin/menus/delete" req [] (menus/menus-delete req))
  (GET "/admin/users" req [] (users/users req))
  (POST "/admin/users" req [] (users/users-grid req))
  (GET "/admin/users/edit/:id" [id] (users/users-form id))
  (POST "/admin/users/save" req [] (users/users-save req))
  (POST "/admin/users/delete" req [] (users/users-delete req))
  (GET "/admin/pincludes" req [] (pincludes/pincludes req))
  (POST "/admin/pincludes" req [] (pincludes/pincludes-grid req))
  (GET "/admin/pincludes/edit/:id" [id] (pincludes/pincludes-form id))
  (POST "/admin/pincludes/save" req [] (pincludes/pincludes-save req))
  (POST "/admin/pincludes/delete" req [] (pincludes/pincludes-delete req))
  (GET "/admin/proutes" req [] (proutes/proutes req))
  (POST "/admin/proutes" req [] (proutes/proutes-grid req))
  (GET "/admin/proutes/edit/:id" [id] (proutes/proutes-form id))
  (POST "/admin/proutes/save" req [] (proutes/proutes-save req))
  (POST "/admin/proutes/delete" req [] (proutes/proutes-delete req))
  (GET "/admin/rincludes" req [] (rincludes/rincludes req))
  (POST "/admin/rincludes" req [] (rincludes/rincludes-grid req))
  (GET "/admin/rincludes/edit/:id" [id] (rincludes/rincludes-form id))
  (POST "/admin/rincludes/save" req [] (rincludes/rincludes-save req))
  (POST "/admin/rincludes/delete" req [] (rincludes/rincludes-delete req))
  (GET "/admin/routes" req [] (routes/routes req))
  (POST "/admin/routes" req [] (routes/routes-grid req))
  (GET "/admin/routes/edit/:id" [id] (routes/routes-form id))
  (POST "/admin/routes/save" req [] (routes/routes-save req))
  (POST "/admin/routes/delete" req [] (routes/routes-delete req)))
