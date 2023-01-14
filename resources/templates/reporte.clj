(ns sk.handlers.contactos.handler
  (:require
   [ring.util.io :refer [piped-input-stream]]
   [hiccup.core :refer [html]]
   [pdfkit-clj.core :refer [as-stream gen-pdf]]
   [clj-pdf.core :refer [pdf template]]
   [sk.layout :refer [application]]
   [sk.models.util :refer [get-session-id user-level]]
   [sk.handlers.contactos.model :refer [get-rows]]
   [sk.handlers.contactos.view :refer [contactos-view contactos-scripts]]))

(defn contactos [_]
  (let [title "Contactos"
        ok (get-session-id)
        js (contactos-scripts)
        content (contactos-view title)]
    (application title ok js content)))

(defn contactos-reporte [_]
  (let [title "Reporte de Contactos"
        ok (get-session-id)
        js nil
        content (html (contactos-view title))]
    (if
     (or
      (= (user-level) "U")
      (= (user-level) "A")
      (= (user-level) "S"))
      {:status 200
       :headers {"Content-Type" "application/pdf"
                 "Content-Disposition" "attachment;filename='pcontactos.pdf'"}
       :body (as-stream (gen-pdf content))}
      (application title ok nil "Solo mienbros pueden accessar este proceso!!!"))))

;; Start contactos-pdf
(def contactos-pdf-template
  (template
    (list
      [:cell {:align :left} (str $nombre)]
      [:cell {:align :left} (str $paterno)]
      [:cell {:align :left} (str $materno)]
      [:cell {:align :left} (str $telefono)]
      [:cell {:align :left} (str $celular)]
      [:cell {:align :left} (str $email)])))

(defn generate-report-body []
  (let [rows (get-rows "contactos")]
    (into
      [:table 
       {:cell-border true
        :style :normal
        :size 10
        :border true
        :widths [15 15 15 10 10 35]
        :header [{:background-color [233 233 233]}
                 [:paragraph {:align :left} "Nombre"]
                 [:paragraph {:align :left} "Paterno"]
                 [:paragraph {:align :left} "Materno"]
                 [:paragraph {:align :left} "Telefono"]
                 [:paragraph {:align :left} "Celular"]
                 [:paragraph {:align :left} "Email"]]}]
      (contactos-pdf-template rows))))

(defn generate-report []
  (piped-input-stream
    (fn [output-stream]
      (pdf
        [{:title "Reporte de Contactos"
          :header {:x 20
                   :y 830
                   :table
                   [:pdf-table
                    {:border false
                     :width-percent 100}
                    [100]
                    [[:pdf-cell {:type :bold :size 16 :align :center} "Reporte de Contactos"]]]}
          :footer "page"
          :left-margin 10
          :right-margin 10
          :top-margin 100
          :bottom-margin 25
          :size :a4
          :orientation :portrait
          :font {:family :helvetica :size 10}
          :align :center
          :pages true}
         (generate-report-body)]
        output-stream))))

(defn contactos-pdf [_]
  (let [title "Reporte de Contactos"
        ok (get-session-id)
        js nil
        content "Solo miembros pueden accessar este proceso!!!"]
    (if
     (or
      (= (user-level) "U")
      (= (user-level) "A")
      (= (user-level) "S"))
      {:status 200
       :headers {"Content-Type" "application/pdf"
                 "Content-Disposition" "attachment;filename='rcontactos.pdf'"}
       :body (generate-report)}
      (application title ok nil content))))
;; End contactos-pdf
