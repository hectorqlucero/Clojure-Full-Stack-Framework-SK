(ns sk.handlers.admin.users.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [hiccup.page :refer [include-js]]
            [sk.models.util :refer [build-table
                                    build-dialog
                                    build-dialog-buttons
                                    build-field
                                    build-radio-buttons]]))

(def dialog-fields
  (list
    [:input {:type "hidden" :id "id" :name "id"}]
    (build-field
      {:id           "username"
       :name         "username"
       :class        "easyui-textbox easyui-validatebox"
       :validType    "email"
       :prompt       "someone@server.com - this is an email"
       :data-options "label:'Username:',
                     labelPosition:'top',
                     width:'100%',required: true"})
    (build-field
      {:id           "firstname"
       :name         "firstname"
       :class        "easyui-textbox"
       :prompt       "Enter first name ex. Bill"
       :data-options "label:'First Name:',
                     labelPosition:'top',
                     width:'100%',required: true"})
    (build-field
      {:id           "lastname"
       :name         "lastname"
       :class        "easyui-textbox"
       :prompt       "Enter Last Name ex. Smith"
       :data-options "label:'Last Name:',
                     labelPosition:'top',
                     width:'100%',required: true"})
    (build-field
      {:id           "dob"
       :name         "dob"
       :class        "easyui-datebox"
       :prompt       "mm/dd/yyyy"
       :data-options "label:'Date of Birth:',
                     labelPosition:'top',
                     width:'100%',required: false"})
    (build-field
      {:id           "email"
       :name         "email"
       :class        "easyui-textbox easyui-validatebox"
       :prompt       "Enter email here..."
       :validType    "email"
       :data-options "label:'Email:',
                     labelPosition:'top',
                     width:'100%',required: false"})
    (build-field
      {:id "level"
       :name "level"
       :class "easyui-combobox"
       :data-options "label:'User Level:',
                     labelPosition:'top',
                     url:'/table_ref/levels',
                     method:'GET',
                     required:true,
                     width:'100%'"})
    (build-radio-buttons
      "Active?"
      (list
        {:id "active_no"
         :name "active"
         :class "easyui-radiobutton"
         :value "F"
         :data-options "label:'No', checked:true"}
        {:id "active_yes"
         :name "active"
         :class "easyui-radiobutton"
         :value "T"
         :data-options "label:'Yes'"}))))

(defn users-view [title]
  (list
   (anti-forgery-field)
   (build-table
     title 
     "/admin/users" 
     (list
       [:th {:data-options "field:'username',sortable:true,fixed:false,width:100"} "Username"]
       [:th {:data-options "field:'lastname',sortable:true,fixed:false,width:100"} "Last Name"]
       [:th {:data-options "field:'firstname',sortable:true,fixed:true,width:100"} "First Name"]
       [:th {:data-options "field:'dob_formatted',sortable:true,fixed:true,width:100"} "DOB"]
       [:th {:data-options "field:'level',sortable:true,fixed:true,width:100"} "Level"]
       [:th {:data-options "field:'active',sortable:true,fixed:false,width:100"} "Active?"]))
   (list ;Here we build a toolbar without the New button - we don't want to create users
     [:div#toolbar
      [:a {:href         "javascript:void(0)"
           :class        "easyui-linkbutton"
           :data-options "iconCls: 'icon-edit',plain: true"
           :onclick      "editItem({})"} "Edit"]
      [:a {:href         "javascript:void(0)"
           :class        "easyui-linkbutton"
           :data-options "iconCls: 'icon-remove',plain: true"
           :onclick      "deleteItem()"} "Remove"]
      [:div {:style "float: right"}]])
   (build-dialog title dialog-fields)
   (build-dialog-buttons)))

(defn users-scripts []
  (include-js "/js/grid.js"))
