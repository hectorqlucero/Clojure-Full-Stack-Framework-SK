(ns sk.models.b-proutes
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
   "(ns sk.routes.proutes\n"
   "(:require \n"
   (apply str (process "pincludes"))
   "))\n\n"
   "(defroutes proutes\n"
   (apply str (process "proutes"))
   ")"))

(defn main-private []
  (spit (str  "src/sk/routes/proutes.clj") (build-routes)))

(comment
  (main-private)
  (build-routes))
