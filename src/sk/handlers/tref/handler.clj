(ns sk.handlers.tref.handler
  (:require [sk.models.crud :refer [db Query]]
            [sk.models.util :refer [parse-int
                                    get-image
                                    current_year]]))

;; Start get-users
(def get-users-sql
  "SELECT
  id AS value,
  CONCAT(firstname,' ',lastname) AS text
  FROM users
  ORDER BY
  firstname,lastname")

(defn get-users []
  "Regresa todos los usuarios o vacio :ex: (get-users)"
  (Query db [get-users-sql]))
;; End get-users

;; Start get-users-email
(def get-users-email-sql
  "SELECT
  email
  FROM users
  WHERE email = ?")

(defn get-users-email [email]
  "Regresa el correo del usuario o nulo"
  (first (Query db [get-users-email-sql email])))
;; End get-users-email

(defn months []
  "Regresa un arreglo de meses en español ex: (months)"
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

;; Start calendar events
(def rodadas-sql
  "SELECT
  id,
  titulo as title,
  detalles,
  DATE_FORMAT(salida,'%h:%i %p') as hora,
  DATE_FORMAT(fecha,'%d/%m/%Y') as fecha,
  CONCAT(fecha,'T',salida) as start,
  punto_reunion as donde,
  CASE WHEN nivel = 'P' THEN 'Principiantes' WHEN nivel = 'M' THEN 'Medio' WHEN nivel = 'A' THEN 'Avanzado' WHEN nivel = 'T' THEN 'TODOS' END as nivel,
  distancia as distancia,
  velocidad as velocidad,
  leader as leader,
  leader_email as email,
  repetir,
  CONCAT('/rodadas/asistir/',id) as url
  FROM rodadas
  ORDER BY fecha,salida")

(defn build-cal-popup [row]
  (html5
    [:div {:style "margin-bottom:10px;"}
     [:label [:strong "Titulo: "] (:title row)]]
    [:div {:style "margin-bottom:5px;"}
     [:label [:strong "Describir Rodada: "] (:detalles row)]]
    [:div {:style "margin-bottom:5px;"}
     [:label [:strong "Punto de reunion: "] (:donde row)]]
    [:div {:style "margin-bottom:5px;"}
     [:label [:strong "Nivel: "] (:nivel row)]]
    [:div {:style "margin-bottom:5px;"}
     [:label [:strong "Distancia: "] (:distancia row)]]
    [:div {:style "margin-bottom:5px;"}
     [:label [:strong "Velocidad: "] (:velocidad row)]]
    [:div {:style "margin-bottom:5px;"}
     [:label [:strong "Fecha/Rodada: "] (:fecha row)]]
    [:div {:style "margin-bottom:5px;"}
     [:label [:strong "Salida: "] (:hora row)]]
    [:div {:style "margin-bottom:5px;"}
     [:label [:strong "Lider: "] (:leader row)]]
    [:div {:style "margin-bottom:5px;"}
     [:label [:strong "Lider Email: "] (:email row)]]))

(defn calendar-events []
  (let [rows (Query db rodadas-sql)
        events (map #(assoc % :description (build-cal-popup %)) rows)]
    events))
;; End calendar events

(defn nivel-options []
  (list
    {:value "P" :text "Principiantes"}
    {:value "M" :text "Medio"}
    {:value "A" :text "Avanzado"}
    {:value "T" :text "TODOS"}))

(defn imagen [table field idname value & extra-folder]
  (get-image table field idname value (first extra-folder)))

(defn get-item 
  "Generic get field value from table"
  [table field idname idvalue]
  (let [sql (str "SELECT " field " FROM " table " WHERE " idname "='" idvalue "'")
        row (first (Query db sql))]
    ((keyword field) row)))
