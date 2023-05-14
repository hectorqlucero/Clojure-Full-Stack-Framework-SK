(ns sk.handlers.admin.pincludes.view
  (:require [hiccup.page :refer [include-js]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.util :refer
    [build-dialog build-dialog-buttons build-field build-table build-toolbar]]))

(defn dialog-fields []
  (list
   (build-field
    {:id "id"
     :name "id"
     :type "hidden"})
   (build-field
    {:id "dt"
     :name "dt"
     :class "easyui-textbox"
     :prompt "Include lib here..."
     :data-options "label:'Data:',
        labelPosition:'top',
        required:true,
        multiline:true,
        height:120,
        width:'100%'"})))

(defn pincludes-view [title]
  (list
   (anti-forgery-field)
   (build-table
    title
    "/admin/pincludes"
    (list
     [:th {:data-options "field:'id',sortable:false,width:10"} "ID"]
     [:th {:data-options "field:'dt',sortable:true,width:100"} "DT"]))
   (build-toolbar)
   (build-dialog title (dialog-fields))
   (build-dialog-buttons)))

(defn pincludes-scripts []
  (include-js "/js/grid.js"))
