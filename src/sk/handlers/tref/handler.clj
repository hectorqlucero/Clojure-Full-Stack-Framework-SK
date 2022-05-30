(ns sk.handlers.tref.handler
  (:require [sk.models.crud :refer [db Query]]
            [sk.models.util :refer [current_year get-image parse-int zpl]]))

;; Start get-pais
(def get-pais-sql
  "
  SELECT
   id AS value,
   descripcion AS text
   FROM pais
   ORDER BY
   descripcion
   ")

(defn get-pais []
  (Query db [get-pais-sql]))
;; End get-pais

;; Start get-pais-id
(def get-pais-id-sql
  "
  SELECT
   descripcion
   FROM pais
   WHERE
   id = ?
   ")

(defn get-pais-id [id]
  (:descripcion (first (Query db [get-pais-id-sql id]))))
;; End get-pais-id

;; Start get-users
(def get-users-sql
  "SELECT
  id AS value,
  CONCAT(firstname,' ',lastname) AS text
  FROM users
  ORDER BY
  firstname,lastname")

(defn get-users
  "Gets all users from database :ex: (get-users)"
  []
  (Query db [get-users-sql]))
;; End get-users

;; Start get-titulos
(def get-titulos-sql
  "SELECT
   id AS value,
   descripcion AS text
   FROM titulos
   ORDER BY
   descripcion")

(defn get-titulos
  "Gets all titulos from database ex: (get-titulos)"
  []
  (Query db [get-titulos-sql]))
;; End get-titulos

;; Start get-titulo
(def get-titulo-sql
  "
  SELECT
   descripcion
   FROM titulos
   WHERE
   id = ?
   ")

(defn get-titulo
  "Gets a titulo from database ex: (get-titulo 1)"
  [id]
  (:descripcion (first (Query db [get-titulo-sql id]))))
;; End get-titulo

;; Start get-users-email
(def get-users-email-sql
  "SELECT
  LOWER(email) as email
  FROM users
  WHERE email = ?")

(defn get-users-email
  "Returns user email or nil"
  [email]
  (first (Query db [get-users-email-sql email])))
;; End get-users-email

(defn months
  "Returns months name ex: (months)"
  []
  (list
   {:value 1 :text "Enero"}
   {:value 2 :text "Febrero"}
   {:value 3 :text "Marzo"}
   {:value 4 :text "Abril"}
   {:value 5 :text "Mayo"}
   {:value 6 :text "Junio"}
   {:value 7 :text "Julio"}
   {:value 8 :text "Agosto"}
   {:value 9 :text "Septiembre"}
   {:value 10 :text "Octubre"}
   {:value 11 :text "Noviembre"}
   {:value 12 :text "Diciembre"}))

(defn level-options []
  (list
   {:value "U" :text "Usuarios"}
   {:value "A" :text "Administrador"}
   {:value "S" :text "Systema"}))

(defn years
  "Genera listado para dropdown dependiendo de p=anterioriores de este año, n=despues de este año,
  ex: (years 5 4)"
  [p n]
  (let [year   (parse-int (current_year))
        pyears (for [n (range (parse-int p) 0 -1)] {:value (- year n) :text (- year n)})
        nyears (for [n (range 0 (+ (parse-int n) 1))] {:value (+ year n) :text (+ year n)})
        years  (concat pyears nyears)]
    years))

(defn build-time
  "Builds tipical time dropdown"
  []
  (let [items (flatten
               (for [x (range 5 21)]
                 (list
                  {:value (str (zpl x 2) ":00")
                   :text (if (< x 12)
                           (str (zpl x 2) ":00 AM")
                           (str (if (> x 12) (zpl (- x 12) 2) (zpl x 2)) ":00 PM"))}
                  {:value (str (zpl x 2) ":30")
                   :text (if (< x 12)
                           (str (zpl x 2) ":30 AM")
                           (str (if (> x 12) (zpl (- x 12) 2) (zpl x 2)) ":30 PM"))})))]
    items))

(defn imagen [table field idname value & extra-folder]
  (get-image table field idname value (first extra-folder)))

(defn get-item
  "Generic get field value from table"
  [table field idname idvalue]
  (let [sql (str "SELECT " field " FROM " table " WHERE " idname "='" idvalue "'")
        row (first (Query db sql))]
    ((keyword field) row)))

(comment
  (get-pais-id 1)
  (get-pais))