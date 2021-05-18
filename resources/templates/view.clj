(ns sk.handlers.xxx.view ;; Cambiar 'xxx' por el nombre del folder/s correspondiente
  (:require [hiccup.page :refer [include-js]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.util :refer [build-table
                                    build-dialog
                                    build-dialog-buttons
                                    build-toolbar
                                    build-image-field
                                    build-text-editor
                                    build-image-field-script
                                    build-field]]))

(def dialog-fields
  (list
    [:input {:type "hidden" :id "id" :name "id"}]
    ;; combobox.clj, date.clj, email.clj, image.clj, numberbox.clj, password.clj, password1.clj
    ;; phone.clj, readiobuttons.clj, textarea.clj, textbox.clj, texteditor.clj, time.clj
    ;; validatebox.clj 
    ;; ejemplo. :read ~/.vim/templates/date.clj  "Si usas vim"
    ;; Start fields - nota (cambiar 'xxx' por el nombre del campo en cada campo que se inserte)
    ;; End fields
    ))

(defn xxx-view 
  "Esto crea el grid y la forma en una ventana - Cambiar xxx por el nombre correcto"
  [title]
  (list
    (anti-forgery-field)
    (build-table
      title
      "/xxx"
      (list ;; Aqui los campos del grid
        [:th {:data-options "field:'campo',sortable:true"} "Encabezado"]
        ))
    (build-toolbar)
    (build-dialog title dialog-fields)))

(defn xxx-scripts
  "Esto crea el javascript necesario - Cambiar xxx por el nombre correcto"
  []
  (include-js "/js/grid.js"))
