(ns sk.models.grid
  (:require [cheshire.core :refer [generate-string]]
            [sk.models.crud :refer [db 
                                    Query
                                    build-grid-columns]]
            [sk.models.util :refer [parse-int]]))

(defn convert-search-columns [fields]
  (let [fields (map #(str "COALESCE(" % ",'')") fields)]
    (into [] fields)))

(defn grid-sort [order-column order-dir]
  "Creates sorting criteria (ORDER BY) for easyui grid"
  (if (not (nil? order-column)) (str " ORDER BY " order-column " " order-dir) nil))

(defn grid-sort-extra [order extra]
  (if (nil? order) (str " ORDER BY " extra) order))

(defn grid-search-extra [search extra]
  (if-not (clojure.string/blank? extra)
    (if (nil? search)
      (str " WHERE " extra)
      (str search " AND " extra))))

(defn grid-search [search fields]
  "Creates search criteria for easyui grid (LIKE search) on all columns"
  (if (not (clojure.string/blank? search))
    (str " WHERE LOWER(concat(" (apply str (interpose "," fields)) ")) like lower('%" search "%')") nil))

(defn grid-add-search [search fields]
  "Creates search criteria for easyui grid (LIKE search) on all columns"
  (if (not (clojure.string/blank? search))
    (str " concat(" (apply str (interpose "," fields)) ") like lower('%" search "%')") nil))

(defn grid-offset [limit page]
  "Creates the limit and offset for pagination on easyui grids (LIMIT && OFFSET)"
  (if (and (parse-int limit)
           (parse-int page))
    (let [offset (* (dec page) limit)]
      (str " LIMIT " limit " OFFSET " offset))))

(defn grid-total_sql [table aliases join search order]
  "Create a total of the grid criteria"
  (str "SELECT " (apply str (interpose "," aliases)) " FROM " table " " join search order))

(defn grid-sql [table aliases join search order offset]
  "Creates select statement for easyui grid (SELECT)"
  (str "SELECT " (apply str (interpose "," aliases)) " FROM " table " " join search order offset))

(defn grid-rows [table aliases join search order offset]
  "Creates the row object to return to the grids"
  {:total (count (Query db [(grid-total_sql table aliases join search order)]))
   :rows  (Query db [(grid-sql table aliases join search order offset)])})

;; Start build grid
(defn get-search-extra 
  [search args]
  (try
    (let [search-extra (:search-extra (first args))]
      (if-not (nil? search-extra)
        (grid-search-extra search search-extra)))
    (catch Exception e (.getMessage e))))

(defn get-sort-extra 
  [order args]
  (try
    (let [sort-extra (:sort-extra (first args))]
      (if-not (nil? sort-extra)
        (grid-sort-extra order sort-extra)))
    (catch Exception e (.getMessage e))))

(defn build-grid
  "builds grid. Parameters: table,search-extra,sort-extra,join"
  [params table & args]
  (try
    (let [aliases (build-grid-columns table)
          join (:join args)
          search nil
          search (get-search-extra search args)
          order (grid-sort (:sort params nil) (:order params nil))
          order (get-sort-extra order args)
          offset (grid-offset (parse-int (:rows params)) (parse-int (:page params)))
          rows (grid-rows table aliases join search order offset)]
      (generate-string rows))
    (catch Exception e (.getMessge e))))
;; End build-grid
