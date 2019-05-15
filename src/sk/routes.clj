(ns sk.routes
  (:require [compojure.core :refer [defroutes GET POST]]
            [sk.table_ref :refer [table_ref-routes]]
            [sk.routes.home :as home]))

(defroutes sk-routes
  table_ref-routes
  (GET "/" request [] (home/main request))
  (GET "/login" request [] (home/login request))
  (POST "/login" [username password] (home/login! username password))
  (GET "/logoff" [] (home/logoff)))
