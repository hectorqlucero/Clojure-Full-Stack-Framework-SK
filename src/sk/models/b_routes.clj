(ns sk.models.b-routes
  (:require [sk.models.crud :refer [db Query]]))

(defn get-rows [table]
  (Query db (str "select * from " table " order by id")))

(defn build-body [row]
  (str row "\n"))

(defn process [table]
  (let [data (map (fn [x] (:dt x)) (get-rows table))]
    (map build-body data)))

(defn build-routes []
  (str
   "(ns sk.routes.routes\n"
   "(:require \n"
   (apply str (process "rincludes"))
   "))\n\n"
   "(defroutes open-routes\n"
   (apply str (process "routes"))
   ")"))

(defn main-open []
  (spit (str  "src/sk/routes/routes.clj") (build-routes)))

(comment
  (main-open)
  (build-routes))
