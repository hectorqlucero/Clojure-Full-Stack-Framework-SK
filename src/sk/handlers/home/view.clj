(ns sk.handlers.home.view
  (:require [sk.models.util :refer [build-button build-field build-form]]))

(defn login-view [token]
  (build-form
   "Login"
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
      :text "Site Login"
      :class "easyui-linkbutton c6"
      :onClick "submitForm()"}))
   (list
    [:div {:style "margin-bottom:10px;"}
     [:a {:href "/register"} "Click to register"]]
    [:div {:style "margin-bottom:10px;"}
     [:a {:href "/rpaswd"} "Click to reset your password"]])))

(defn login-script []
  [:script
   "
    function submitForm() {
        $('.fm').form('submit', {
            onSubmit:function() {
                if($(this).form('validate')) {
                  $('a#submit').linkbutton('disable');
                  $('a#submit').linkbutton({text: 'Processando!'});
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
                        $('a#submit').linkbutton('enable');
                        $('a#submit').linkbutton({text: 'Site Login'});
                    }
                } catch(e) {
                    console.error('Invalid JSON');
                }
            }
        });
    }
   "])
