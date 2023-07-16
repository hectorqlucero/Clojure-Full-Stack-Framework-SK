(ns sk.handlers.admin.menus.view
  (:require [hiccup.page :refer [include-js]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.util :refer
    [build-dialog build-dialog-buttons build-field build-radio-buttons
     build-table build-toolbar]]))

(defn dialog-fields []
  (list
   (build-field
    {:id "id"
     :name "id"
     :type "hidden"})
   (build-radio-buttons
    "Type:"
    (list
     {:id "type_o"
      :name "type"
      :class "easyui-radiobutton"
      :value "O"
      :label "Open"}
     {:id "type_p"
      :name "type"
      :class "easyui-radiobutton"
      :value "P"
      :label "Private"}
     {:id "type_b"
      :name "type"
      :class "easyui-radiobutton"
      :value "B"
      :label "Both"}))
   (build-radio-buttons
    "Admin:"
    (list
     {:id "admin_t"
      :name "admin"
      :class "easyui-radiobutton"
      :value "T"
      :label "True"}
     {:id "admin_f"
      :name "admin"
      :class "easyui-radiobutton"
      :value "F"
      :label "False"}))
   (build-radio-buttons
    "Secure:"
    (list
     {:id "secure_0"
      :name "secure"
      :class "easyui-radiobutton"
      :value 0
      :label "Public"}
     {:id "secure_3"
      :name "secure"
      :class "easyui-radiobutton"
      :value 3
      :label "User"}
     {:id "secure_2"
      :name "secure"
      :class "easyui-radiobutton"
      :value 2
      :label "System"}
     {:id "secure_1"
      :name "secure"
      :class "easyui-radiobutton"
      :value 1
      :label "Admin"}))
   (build-field
    {:id "root"
     :name "root"
     :class "easyui-textbox"
     :prompt "The root folder ex. src/sk/handlers/admin"
     :data-options "label:'Root:',
        labelPosition:'top',
        required:true,
        width:'100%'"})
   (build-field
    {:id "link"
     :name "link"
     :class "easyui-textbox"
     :prompt "The link or route ex. /admin/users"
     :data-options "label:'Link:',
        labelPosition:'top',
        required:true,
        width:'100%'"})
   (build-field
    {:id "description"
     :name "description"
     :class "easyui-textbox"
     :prompt "The description ex. Conectar"
     :data-options "label:'Description:',
        labelPosition:'top',
        required:true,
        width:'100%'"})))

(defn menus-view [title]
  (list
   (anti-forgery-field)
   (build-table
    title
    "/admin/menus"
    (list
     [:th {:data-options "field:'id',sortable:true,width:100"} "ID"]
     [:th {:data-options "field:'type',sortable:true,width:100"} "TYPE"]
     [:th {:data-options "field:'admin',sortable:true,width:100"} "ADMIN"]
     [:th {:data-options "field:'secure',sortable:true,width:100"} "SECURE"]
     [:th {:data-options "field:'root',sortable:true,width:100"} "ROOT"]
     [:th {:data-options "field:'link',sortable:true,width:100"} "LINK"]
     [:th {:data-options "field:'description',sortable:true,width:100"} "DESCRIPTION"]))
   (build-toolbar)
   (build-dialog title (dialog-fields))
   (build-dialog-buttons)))

(defn menus-scripts []
  (include-js "/js/grid.js"))
