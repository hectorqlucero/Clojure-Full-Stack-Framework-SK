(ns sk.models.grid
  (:require [cheshire.core :refer [generate-string]]
            [clojure.string :as str]
            [sk.models.crud :refer [db
                                    Query
                                    build-grid-columns]]
            [sk.models.util :refer [parse-int]]))

(defn convert-search-columns [fields]
  (let [fields (map #(str "COALESCE(" % ",'')") fields)]
    (into [] fields)))

(defn grid-sort
  "Creates sorting criteria (ORDER BY) for easyui grid"
  [order-column order-dir]
  (if (not (nil? order-column)) (str " ORDER BY " order-column " " order-dir) nil))

(defn grid-sort-extra [order extra]
  (if (nil? order) (str " ORDER BY " extra) order))

(defn grid-search-extra [search extra]
  (when-not (str/blank? extra)
    (if (nil? search)
      (str " WHERE " extra)
      (str search " AND " extra))))

(defn grid-search
  "Creates search criteria for easyui grid (LIKE search) on all columns"
  [search fields]
  (if (not (clojure.string/blank? search))
    (str " WHERE LOWER(concat(" (apply str (interpose "," fields)) ")) like lower('%" search "%')") nil))

(defn grid-add-search
  "Creates search criteria for easyui grid (LIKE search) on all columns"
  [search fields]
  (if (not (clojure.string/blank? search))
    (str " concat(" (apply str (interpose "," fields)) ") like lower('%" search "%')") nil))

(defn grid-offset
  "Creates the limit and offset for pagination on easyui grids (LIMIT && OFFSET)"
  [limit page]
  (when (and (parse-int limit)
             (parse-int page))
    (let [offset (* (dec page) limit)]
      (str " LIMIT " limit " OFFSET " offset))))

(defn grid-total_sql
  "Create a total of the grid criteria"
  [table aliases join search order]
  (str "SELECT " (apply str (interpose "," aliases)) " FROM " table " " join search order))

(defn grid-sql
  "Creates select statement for easyui grid (SELECT)"
  [table aliases join search order offset]
  (str "SELECT " (apply str (interpose "," aliases)) " FROM " table " " join search order offset))

(defn grid-rows
  "Creates the row object to return to the grids"
  [table aliases join search order offset]
  {:total (count (Query db [(grid-total_sql table aliases join search order)]))
   :rows  (Query db [(grid-sql table aliases join search order offset)])})

;; Start build grid
(defn get-search-extra
  [search args]
  (try
    (let [search-extra (:search-extra args)]
      (when-not (nil? search-extra)
        (grid-search-extra search search-extra)))
    (catch Exception e (.getMessage e))))

(defn get-sort-extra
  [order args]
  (try
    (let [sort-extra (:sort-extra args)]
      (when-not (nil? sort-extra)
        (grid-sort-extra order sort-extra)))
    (catch Exception e (.getMessage e))))

(defn build-grid
  "builds grid. Parameters: table,search-extra,sort-extra,join"
  [params table & args]
  (try
    (let [args (first args)
          aliases (if-not (nil? (:aliases args)) (:aliases args) (build-grid-columns table))
          join (:join args)
          search (convert-search-columns (build-grid-columns table))
          search (get-search-extra search args)
          order (grid-sort (:sort params nil) (:order params nil))
          order (get-sort-extra order args)
          offset (grid-offset (parse-int (:rows params)) (parse-int (:page params)))
          rows (grid-rows table aliases join search order offset)]
      (generate-string rows))
    (catch Exception e (.getMessge e))))
;; End build-grid
