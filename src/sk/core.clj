(ns sk.core
  (:require [compojure.core :refer [defroutes routes]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [noir.response :refer [redirect]]
            [noir.session :as session]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.reload :as reload]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.session.cookie :refer [cookie-store]]
            [sk.models.crud :refer [config KEY]]
            [sk.routes :refer [open-routes]]
            [sk.proutes :refer [proutes]])
  (:gen-class))

(defn wrap-login [hdlr]
  (fn [req]
    (try
      (if (nil? (session/get :user_id)) (redirect "/home/login") (hdlr req))
      (catch Exception _
        {:status 400 :body "Unable to process your request!"}))))

(defn wrap-exception-handling [hdlr]
  (fn [req]
    (try
      (hdlr req)
      (catch Exception _
        {:status 400 :body "Invalid data"}))))

(defroutes app-routes
  (route/resources "/")
  (route/files (:path config) {:root (:uploads config)})
  open-routes
  (wrap-login proutes)
  (route/not-found "Not Found"))

(defn -main []
  (jetty/run-jetty
    (-> (routes
          (wrap-exception-handling app-routes))
        (handler/site)
        (wrap-session)
        (session/wrap-noir-session*)
        (wrap-multipart-params)
        (reload/wrap-reload)
        (wrap-defaults (-> site-defaults
                           (assoc-in [:security :anti-forgery] true)
                           (assoc-in [:session :store] (cookie-store {:key KEY}))
                           (assoc-in [:session :cookie-attrs] {:max-age 28800})
                           (assoc-in [:session :cookie-name] "LS"))))
    {:port (:port config)}))
