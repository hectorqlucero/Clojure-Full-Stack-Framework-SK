(ns sk.models.cdb
  (:require [noir.util.crypt :as crypt]
            [sk.models.crud :refer [db Insert-multi Query!]]))

(def rincludes-rows
  [{:id 1
    :dt "[cheshire.core :refer [generate-string]]"}
   {:id 2
    :dt "[compojure.core :refer [defroutes GET POST]]"}
   {:id 3
    :dt "[sk.handlers.home.handler :as home]"}
   {:id 4
    :dt "[sk.handlers.registrar.handler :as registrar]"}
   {:id 5
    :dt "[sk.handlers.tref.handler :as table_ref]"}])

(def routes-rows
  [{:id 1
    :dt "(GET \"/table_ref/get_users\" [] (generate-string (table_ref/get-users)))"}
   {:id 2
    :dt "(GET \"/table_ref/validate_email/:email\" [email] (generate-string (table_ref/get-users-email email)))"}
   {:id 3
    :dt "(GET \"/table_ref/months\" [] (generate-string (table_ref/months)))"}
   {:id 4
    :dt "(GET \"/table_ref/years/:pyears/:nyears\" [pyears nyears] (generate-string (table_ref/years pyears nyears)))"}
   {:id 5
    :dt "(GET \"/table_ref/get-item/:table/:field/:fname/:fval\" [table field fname fval] (table_ref/get-item table field fname fval))"}
   {:id 6
    :dt "(GET \"/table_ref/get-time\" [] (generate-string (table_ref/build-time)))"}
   {:id 7
    :dt "(GET \"/table_ref/levels\" [] (generate-string (table_ref/level-options)))"}
   {:id 8
    :dt "(GET \"/table_ref/get-titulo/:id\" [id] (table_ref/get-titulo id))"}
   {:id 9
    :dt "(GET \"/table_ref/get-titulos\" [] (generate-string (table_ref/get-titulos)))"}
   {:id 10
    :dt "(GET \"/table_ref/get-paises\" [] (generate-string (table_ref/get-pais)))"}
   {:id 11
    :dt "(GET \"/table_ref/get-pais/:id\" [id] (table_ref/get-pais-id id))"}
   {:id 12
    :dt "(GET \"/\" req [] (home/main req))"}
   {:id 13
    :dt "(GET \"/home/login\" req [] (home/login req))"}
   {:id 14
    :dt "(POST \"/home/login\" [username password] (home/login! username password))"}
   {:id 15
    :dt "(GET \"/home/logoff\" [] (home/logoff))"}
   {:id 16
    :dt "(GET \"/register\" req [] (registrar/registrar req))"}
   {:id 17
    :dt "(POST \"/register\" req [] (registrar/registrar! req))"}
   {:id 18
    :dt "(GET \"/rpaswd\" req [] (registrar/reset-password req))"}
   {:id 19
    :dt "(POST \"/rpaswd\" req [] (registrar/reset-password! req))"}
   {:id 20
    :dt "(GET \"/reset_password/:token\" [token] (registrar/reset-jwt token))"}
   {:id 21
    :dt "(POST \"/reset_password\" req [] (registrar/reset-jwt! req))"}])

(def pincludes-rows
  [{:id 1
    :dt "[compojure.core :refer [defroutes GET POST]]"}
   {:id 2
    :dt "[sk.handlers.admin.menus.handler :as menus]"}
   {:id 3
    :dt "[sk.handlers.admin.users.handler :as users]"}
   {:id 4
    :dt "[sk.handlers.admin.pincludes.handler :as pincludes]"}
   {:id 5
    :dt "[sk.handlers.admin.proutes.handler :as proutes]"}
   {:id 6
    :dt "[sk.handlers.admin.rincludes.handler :as rincludes]"}
   {:id 7
    :dt "[sk.handlers.admin.routes.handler :as routes]"}])

(def proutes-rows
  [{:id 1
    :dt "(GET \"/admin/menus\" req [] (menus/menus req))"}
   {:id 2
    :dt "(POST \"/admin/menus\" req [] (menus/menus-grid req))"}
   {:id 3
    :dt "(GET \"/admin/menus/edit/:id\" [id] (menus/menus-form id))"}
   {:id 4
    :dt "(POST \"/admin/menus/save\" req [] (menus/menus-save req))"}
   {:id 5
    :dt "(POST \"/admin/menus/delete\" req [] (menus/menus-delete req))"}
   {:id 6
    :dt "(GET \"/admin/users\" req [] (users/users req))"}
   {:id 7
    :dt "(POST \"/admin/users\" req [] (users/users-grid req))"}
   {:id 8
    :dt "(GET \"/admin/users/edit/:id\" [id] (users/users-form id))"}
   {:id 9
    :dt "(POST \"/admin/users/save\" req [] (users/users-save req))"}
   {:id 10
    :dt "(POST \"/admin/users/delete\" req [] (users/users-delete req))"}
   {:id 11
    :dt "(GET \"/admin/pincludes\" req [] (pincludes/pincludes req))"}
   {:id 12
    :dt "(POST \"/admin/pincludes\" req [] (pincludes/pincludes-grid req))"}
   {:id 13
    :dt "(GET \"/admin/pincludes/edit/:id\" [id] (pincludes/pincludes-form id))"}
   {:id 14
    :dt "(POST \"/admin/pincludes/save\" req [] (pincludes/pincludes-save req))"}
   {:id 15
    :dt "(POST \"/admin/pincludes/delete\" req [] (pincludes/pincludes-delete req))"}
   {:id 16
    :dt "(GET \"/admin/proutes\" req [] (proutes/proutes req))"}
   {:id 17
    :dt "(POST \"/admin/proutes\" req [] (proutes/proutes-grid req))"}
   {:id 18
    :dt "(GET \"/admin/proutes/edit/:id\" [id] (proutes/proutes-form id))"}
   {:id 19
    :dt "(POST \"/admin/proutes/save\" req [] (proutes/proutes-save req))"}
   {:id 20
    :dt "(POST \"/admin/proutes/delete\" req [] (proutes/proutes-delete req))"}
   {:id 21
    :dt "(GET \"/admin/rincludes\" req [] (rincludes/rincludes req))"}
   {:id 22
    :dt "(POST \"/admin/rincludes\" req [] (rincludes/rincludes-grid req))"}
   {:id 23
    :dt "(GET \"/admin/rincludes/edit/:id\" [id] (rincludes/rincludes-form id))"}
   {:id 24
    :dt "(POST \"/admin/rincludes/save\" req [] (rincludes/rincludes-save req))"}
   {:id 25
    :dt "(POST \"/admin/rincludes/delete\" req [] (rincludes/rincludes-delete req))"}
   {:id 26
    :dt "(GET \"/admin/routes\" req [] (routes/routes req))"}
   {:id 27
    :dt "(POST \"/admin/routes\" req [] (routes/routes-grid req))"}
   {:id 28
    :dt "(GET \"/admin/routes/edit/:id\" [id] (routes/routes-form id))"}
   {:id 29
    :dt "(POST \"/admin/routes/save\" req [] (routes/routes-save req))"}
   {:id 30
    :dt "(POST \"/admin/routes/delete\" req [] (routes/routes-delete req))"}])

(def menus-rows
  [{:id 1
    :type "P"
    :admin "T"
    :secure 2
    :root "scr/sk/handlers/admin/"
    :link "/admin/menus"
    :description "Menus"}
   {:id 2
    :type "P"
    :admin "T"
    :secure 2
    :root "scr/sk/handlers/admin/"
    :link "/admin/users"
    :description "Usuarios"}
   {:id 3
    :type "P"
    :admin "T"
    :secure 2
    :root "src/sk/handlers/admin/"
    :link "/admin/pincludes"
    :description "Private includes"}
   {:id 4
    :type "P"
    :admin "T"
    :secure 2
    :root "src/sk/handlers/admin/"
    :link "/admin/proutes"
    :description "Private Routes"}
   {:id 5
    :type "P"
    :admin "T"
    :secure 2
    :root "src/sk/handlers/admin/"
    :link "/admin/rincludes"
    :description "Open Includes"}
   {:id 6
    :type "P"
    :admin "T"
    :secure 2
    :root "src/sk/handlers/admin/"
    :link "/admin/routes"
    :description "Open Routes"}])

(def users-rows
  [{:lastname  "User"
    :firstname "Regular"
    :username  "user@gmail.com"
    :password  (crypt/encrypt "user")
    :dob       "1957-02-07"
    :email     "user@gmail.com"
    :level     "U"
    :active    "T"}
   {:lastname "User"
    :firstname "Admin"
    :username "admin@gmail.com"
    :password (crypt/encrypt "admin")
    :dob "1957-02-07"
    :email "admin@gmail.com"
    :level "A"
    :active "T"}
   {:lastname "User"
    :firstname "Sistema"
    :username "sistema@gmail.com"
    :password (crypt/encrypt "sistema")
    :dob "1957-02-07"
    :email "sistema@gmail.com"
    :level "S"
    :active "T"}])
;; End users table

(defn populate-tables
  "Populates table with default data"
  [table rows]
  (Query! db (str "LOCK TABLES " table "WRITE;"))
  (Insert-multi db (keyword table) rows)
  (Query! db "UNLOCK TABLES;"))

(defn database []
  (populate-tables "rincludes" rincludes-rows)
  (populate-tables "routes" routes-rows)
  (populate-tables "pincludes" pincludes-rows)
  (populate-tables "proutes" proutes-rows)
  (populate-tables "menus" menus-rows)
  (populate-tables "users" users-rows))

(comment
  (populate-tables "rincludes" rincludes-rows)
  (populate-tables "routes" routes-rows)
  (populate-tables "pincludes" pincludes-rows)
  (populate-tables "proutes" proutes-rows)
  (populate-tables "menus" menus-rows)
  (populate-tables "users" users-rows))
