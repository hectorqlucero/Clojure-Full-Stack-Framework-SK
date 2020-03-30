(ns sk.handlers.home.view
  (:require [hiccup.page :refer [include-js]]
            [sk.models.util :refer [build-form
                                    build-field
                                    build-button]]))

(defn login-view [token]
  (build-form
    "Ciclismo Mexicali"
    token
    (list
      (build-field
        {:id "username"
         :name "username"
         :class "easyui-textbox easyui-validatebox"
         :data-options "label:'Email:',labelPosition:'top',required:true,width:'100%'"})
      (build-field
        {:id "password"
         :name "password"
         :class "easyui-passwordbox"
         :data-options "label:'Contraseña:',labelPosition:'top',required:true,width:'100%'"})
      (build-button
        {:href "javascript:void(0)"
         :id "submit"
         :text "Acceder al Sitio"
         :class "easyui-linkbutton c6"
         :onClick "submitForm(this)"})
      )
    (list
      [:div {:style "margin-bottom:10px;"}
       [:a {:href "/registrar"} "Click para requistrarse"]]
      [:div {:style "margin-bottom:10px;"}
       [:a {:href "/rpaswd"} "Click para resetear su contraseña"]])))

(defn login-script []
  [:script
   "
    function submitForm() {
        $('a#submit').linkbutton('disable');
        $('.fm').form('submit', {
            onSubmit:function() {
                return $(this).form('enableValidation').form('validate');
            },
            success: function(data) {
                try {
                    var dta = JSON.parse(data);
                    if(dta.hasOwnProperty('url')) {
                        window.location.href = dta.url;
                    } else if(dta.hasOwnProperty('error')) {
                        $.messager.show({
                            title: 'Error: ',
                            msg: dta.error
                        });
                    }
                } catch(e) {
                    console.error('Invalid JSON');
                }
            }
        });
        $('a#submit').linkbutton('enable');
    }
   "])
