(ns sk.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [clj-time.core :as t]
            [sk.models.crud :refer [config]]
            [sk.models.util :refer [user-level user-name]]))

(defn build-admin []
  (list
   [:a.dropdown-item {:href "/admin/users"} "Usuarios"]))

(defn menus-private []
  (list
   [:nav.navbar.navbar-expand-sm.navbar-dark.bg-dark.fixed-top
    [:a.navbar-brand {:href "/"} (:site-name config)]
    [:button.navbar-toggler {:type "button"
                             :data-toggle "collapse"
                             :data-target "#collapsibleNavbar"}
     [:span.navbar-toggler-icon]]
    [:div#collapsibleNavbar.collapse.navbar-collapse
     [:ul.navbar-nav
      [:li.nav-item [:a.nav-link {:href "#"} "Menu 1"]]
      [:li.nav-item [:a.nav-link {:href "#"} "Menu 2"]]
      [:li.nav-item [:a.nav-link {:href "#"} "Menu 3"]]
      [:li.nav-item [:a.nav-link {:href "#"} "Menu 4"]]
      [:li.nav-item [:a.nav-link {:href "#"} "Menu 5"]]
      (when
       (or
        (= (user-level) "A")
        (= (user-level) "S"))
        [:li.nav-item.dropdown
         [:a.nav-link.dropdown-toggle {:href "#"
                                       :id "navdrop"
                                       :data-toggle "dropdown"} "Administrar"]
         [:div.dropdown-menu
          (build-admin)]])
      (when
       (or
        (= (user-level) "A")
        (= (user-level) "S"))
        [:li.nav-item [:a.nav-link {:href "/admin/users"} "Usuarios"]])
      [:li.nav-item [:a.nav-link {:href "#"} "Menu 6"]]
      [:li.nav-item [:a.nav-link {:href "/home/logoff"} (str "Salir [" (user-name) "]")]]]]]))

(defn menus-public []
  (list
   [:nav.navbar.navbar-expand-sm.navbar-dark.bg-dark.fixed-top
    [:a.navbar-brand {:href "/"} (:site-name config)]
    [:button.navbar-toggler {:type "button"
                             :data-toggle "collapse"
                             :data-target "#collapsibleNavbar"}
     [:span.navbar-toggler-icon]]
    [:div#collapsibleNavbar.collapse.navbar-collapse
     [:ul.navbar-nav
      [:li.nav-item [:a.nav-link {:href "/home/login"} "Conectar"]]]]]))

(defn menus-none []
  (list
   [:nav.navbar.navbar-expand-sm.navbar-dark.bg-dark.fixed-top
    [:a.navbar-brand {:href "#"} (:site-name config)]
    [:button.navbar-toggler {:type "button"
                             :data-toggle "collapse"
                             :data-target "#collapsibleNavbar"}
     [:span.navbar-toggler-icon]]
    [:div#collapsibleNavbar.collapse.navbar-collapse]]))

(defn app-css []
  (list
   (include-css "/bootstrap/css/bootstrap.min.css")
   (include-css "/bootstrap/css/lumen.min.css")
   (include-css "https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css")
   (include-css "/easyui/themes/gray/easyui.css")
   (include-css "/easyui/themes/icon.css")
   (include-css "/easyui/themes/color.css")
   (include-css "/css/main.css")
   (include-css "/RichText/src/richtext.min.css")))

(defn app-js []
  (list
   (include-js "/easyui/jquery.min.js")
   (include-js "/popper/popper.min.js")
   (include-js "/bootstrap/js/bootstrap.min.js")
   (include-js "/easyui/jquery.easyui.min.js")
   (include-js "/easyui/jquery.edatagrid.js")
   (include-js "/easyui/datagrid-detailview.js")
   (include-js "/easyui/datagrid-groupview.js")
   (include-js "/easyui/datagrid-bufferview.js")
   (include-js "/easyui/datagrid-scrollview.js")
   (include-js "/easyui/datagrid-filter.js")
   (include-js "/easyui/locale/easyui-lang-es.js")
   (include-js "/RichText/src/jquery.richtext.min.js")
   (include-js "/js/main.js")))

(defn application [title ok js & content]
  (html5 {:ng-app (:site-name config) :lang "en"}
         [:head
          [:title (if title
                    title
                    (:site-name config))]
          [:meta {:charset "UTF-8"}]
          [:meta {:name "viewport"
                  :content "width=device-width, initial-scale=1"}]
          (app-css)
          [:link {:rel "shortcut icon"
                  :type "image/x-icon"
                  :href "data:image/x-icon;,"}]]
         [:body {:style "width:100vw;height:93vh;"}
          (cond
            (= ok -1) (menus-none)
            (= ok 0) (menus-public)
            (> ok 0) (menus-private))
          [:div.container-fluid {:style "margin-top:55px;width:100vw;height:92vh;"}
           [:div.easyui-panel {:data-options "fit:true,border:false" :style "padding:3px;"} content]]
          (app-js)
          js]
         [:footer.bg-secondary.text-center.fixed-bottom
          [:span  "Copyright &copy" (t/year (t/now)) " Lucero Systems - All Rights Reserved"]]))

(defn error-404 [error return-url]
  [:div
   [:p [:h3 [:b "Error: "]] error]
   [:p [:h3 [:a {:href return-url} "Clic here to " [:strong "Return"]]]]])
