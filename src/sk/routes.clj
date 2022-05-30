(ns sk.routes
  (:require [cheshire.core :refer [generate-string]]
            [compojure.core :refer [defroutes GET POST]]
            [sk.handlers.home.handler :as home]
            [sk.handlers.registrar.handler :as registrar]
            [sk.handlers.tref.handler :as table_ref]))

(defroutes open-routes
  ;; Start table_ref
  (GET "/table_ref/get_users" [] (generate-string (table_ref/get-users)))
  (GET "/table_ref/validate_email/:email" [email] (generate-string (table_ref/get-users-email email)))
  (GET "/table_ref/months" [] (generate-string (table_ref/months)))
  (GET "/table_ref/years/:pyears/:nyears" [pyears nyears] (generate-string (table_ref/years pyears nyears)))
  (GET "/table_ref/get-item/:table/:field/:fname/:fval" [table field fname fval] (table_ref/get-item table field fname fval))
  (GET "/table_ref/get-time" [] (generate-string (table_ref/build-time)))
  (GET "/table_ref/levels" [] (generate-string (table_ref/level-options)))
  (GET "/table_ref/get-titulo/:id" [id] (table_ref/get-titulo id))
  (GET "/table_ref/get-titulos" [] (generate-string (table_ref/get-titulos)))
  (GET "/table_ref/get-paises" [] (generate-string (table_ref/get-pais)))
  (GET "/table_ref/get-pais/:id" [id] (table_ref/get-pais-id id))
  ;; End table_ref

  ;; Start home
  (GET "/" request [] (home/main request))
  (GET "/home/login" request [] (home/login request))
  (POST "/home/login" [username password] (home/login! username password))
  (GET "/home/logoff" [] (home/logoff))
  ;; End home

  ;; Start registrar
  (GET "/register" request [] (registrar/registrar request))
  (POST "/register" request [] (registrar/registrar! request))
  (GET "/rpaswd" request [] (registrar/reset-password request))
  (POST "/rpaswd" request [] (registrar/reset-password! request))
  (GET "/reset_password/:token" [token] (registrar/reset-jwt token))
  (POST "/reset_password" request [] (registrar/reset-jwt! request))
  ;; End registrar
  )
