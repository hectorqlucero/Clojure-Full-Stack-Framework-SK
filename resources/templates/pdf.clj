(:require
  [pdfkit-clj.core :refer [as-stream gen-pdf]]
  [hiccup.page :refer [html5]])

"Nota: Hay que agregar las librearias aqui arriva al handler, hay que cambira xxx por el nombre correcto"

(defn xxx-pdf [_]
  (let [title "xxx"
        file-name "xxx.pdf"
        content-disposition (str "attachment;filename=" file-name)
        html (html5 (xxx-view title))]
    {:status 200
     :headers {"Content-Type" "application/pdf"
               "Content-Disposition" content-disposition}
     :body (as-stream (gen-pdf html))}))

"Ejemplo despues de cambiar xxx por el nombre correcto"

(defn contactos-pdf [_]
  (let [title "Contactos"
        file-name "contactos.pdf"
        content-disposition (str "attachment;filename=" file-name)
        html (html5 (contactos-view title))]
    {:status 200
     :headers {"Content-Type" "application/pdf"
               "Content-Disposition" content-disposition}
     :body (as-stream (gen-pdf html))}))
