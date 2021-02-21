(ns sk.handlers.home.view
  (:require
   [sk.models.crud :refer [config]]
   [sk.models.util :refer [build-form
                           build-field
                           build-button]]))

(defn login-view [token]
  (build-form
   (:site-name config)
   token
   (list
    (build-field
     {:id "username"
      :name "username"
      :class "easyui-textbox"
      :prompt "Email here..."
      :validType "email"
      :data-options "label:'Email:',labelPosition:'top',required:true,width:'100%'"})
    (build-field
     {:id "password"
      :name "password"
      :class "easyui-passwordbox"
      :prompt "Password here..."
      :data-options "label:'Password:',labelPosition:'top',required:true,width:'100%'"})
    (build-button
     {:href "javascript:void(0)"
      :id "submit"
      :text "Acceder al sitio"
      :class "easyui-linkbutton c6"
      :onClick "submitForm()"}))
   (list
    [:div {:style "margin-bottom:10px;"}
     [:a {:href "/register"} "Clic para registrarse"]]
    [:div {:style "margin-bottom:10px;"}
     [:a {:href "/rpaswd"} "Clic para resetear su contraseña"]])))

(defn login-script []
  [:script
   "
    function submitForm() {
        $('.fm').form('submit', {
            onSubmit:function() {
                if($(this).form('validate')) {
                  $('a#submit').linkbutton('disable');
                  $('a#submit').linkbutton({text: 'Procesando!'});
                }
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
                        $('a#submit').linkbutton('disable');
                        $('a#submit').linkbutton({text: 'Acceder al sitio'});
                    }
                } catch(e) {
                    console.error('Invalid JSON');
                }
            }
        });
    }
   "])
