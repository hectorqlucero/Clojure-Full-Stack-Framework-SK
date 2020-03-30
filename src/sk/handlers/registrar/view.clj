(ns sk.handlers.registrar.view
  (:require [hiccup.page :refer [include-js]]
            [sk.models.util :refer [build-form
                                    build-field 
                                    build-button]]))

;; Start registrar
(defn registrar-view [title token]
  (build-form 
    title 
    token 
    (list
      (build-field {:id "firstname"
                    :name "firstname"
                    :class "easyui-textbox"
                    :data-options "label:'Nombre:', labelPosition:'top', required:true,width:'100%'"})
      (build-field {:id "lastname"
                    :name "lastname"
                    :class "easyui-textbox"
                    :data-options "label:'Apellidos:', labelPosition:'top', required:true,width:'100%'"})
      (build-field {:id "email"
                    :name "email"
                    :class "easyui-textbox easyui-validatebox"
                    :validType "email"
                    :data-options "label:'Correo Electronico:', labelPosition:'top', required:true,width:'100%'"})
      (build-field {:id "password"
                    :name "password"
                    :class "easyui-passwordbox"
                    :data-options "label:'Contraseña:', labelPosition:'top', required:true,width:'100%'"})
      (build-field {:id "password1"
                    :name "password1"
                    :class "easyui-passwordbox"
                    :validType "confirmPass['#password']"
                    :data-options "label:'Confirmar Contraseña:', labelPosition:'top', required:true,width:'100%'"}))
    (list
      (build-button {:href "javascript:void(0)"
                     :text "Registrarse"
                     :class "easyui-linkbutton c6"
                     :id "submit"}))))


(defn registrar-scripts []
  [:script
   "
    $(document).ready(function() {
        $('a#submit').click(function() {
            $('form.fm').form('submit', {
                onSubmit: function() {
                    return $(this).form('enableValidation').form('validate');
                },
                success: function(data) {
                    try {
                        var dta = JSON.parse(data);
                        if(dta.hasOwnProperty('url')) {
                            $.messager.alert({
                                title: 'Procesado!',
                                msg: 'Usuario registrado correctamente!',
                                fn: function() {
                                    window.location.href = dta.url;
                                }
                            });
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
        });

        $('#email').textbox({
            onChange: function(value) {
                var url = '/table_ref/validate_email/'+value;
                $.get(url, function(data) {
                    try {
                        var dta = JSON.parse(data);
                        if(dta.hasOwnProperty('email')) {
                            if(value == dta.email) {
                                $.messager.alert({
                                    title: 'Error',
                                    msg: 'Este usuario ya existe!',
                                    fn: function() {
                                        window.location.href = '/registrar';
                                    }
                                });
                            }
                        }
                    } catch(e) {}
                });
            }
        });

        $.extend($.fn.validatebox.defaults.rules, {
            confirmPass: {
                validator: function(value, param) {
                    var password = $(param[0]).passwordbox('getValue');
                    return value == password;
                },
                message: 'La contraseña confirmadora no es igual!'
            }
        });
    });
   "]
  )
;; End registrar

;; Start reset-password
(defn reset-password-view [title token]
  (build-form 
    title 
    token 
    (list
      (build-field {:type "hidden" :name "username"})
      (build-field {:id "email"
                    :name "email"
                    :class "easyui-textbox"
                    :validType "email"
                    :data-options "label:'Correo Electronico:', labelPosition:'top', required:true,width:'100%'"}))
    (build-button
      {:id "submit"
       :href "javascript:void(0)"
       :text "Resetear Contraseña"
       :onclick "submitForm()"
       :class "easyui-linkbutton c6"})))

(defn reset-password-scripts []
  [:script
   "
    function submitForm() {
      $('.fm').form('submit', {
        onSubmit:function() {
          return $(this).form('enableValidation').form('validate');
        },
        success: function(data) {
          try {
            var dta = JSON.parse(data);
            if(dta.hasOwnProperty('url')) {
              $.messager.alert({
                title: 'Información!',
                msg: 'Revise su correo electronico donde vera instrucciones para resetear su contraseña!',
                fn: function() {
                  window.location.href = dta.url;
                }
              });
            } else if(dta.hasOwnProperty('error')) {
              $.messager.show({
                title: 'Error',
                msg: dta.error
              });
            }
          } catch(e) {
            console.error('Invalid JSON');
          }
        }
      });
    }

    function give_error() {
      $.messager.alert({
        title: 'Error',
        msg: 'Este correo no existe en la base de datos, intente otra vez!',
        fn: function() {
          window.location.href = '/rpaswd';
        }
      });
    }

    $(document).ready(function() {
      $('#email').textbox({
        onChange: function(value) {
          var url = '/table_ref/validate_email/' + value;
          $.get(url, function(data) {
            try {
              var dta = JSON.parse(data);
              if(dta.hasOwnProperty('email')) {
                if(value !== dta.email) {
                  give_error();
                }
              } else {
                give_error();
              }
            } catch(e) {
              give_error();
            }
          });
        }
      });
    });
   "]
  )
;; End reset-password

;; Start reset-jwt
(defn reset-jwt-view [title token username]
  (build-form
    title
    token
    (list
      [:input {:type "hidden" :name "username" :value username}]
      (build-field 
        {:id "password"
         :name "password"
         :class "easyui-passwordbox"
         :data-options "label:'Contraseña:',labelPosition:'top',required:true,width:'100%'"})
      (build-field
        {:id "password1"
         :name "password1"
         :class "easyui-passwordbox"
         :validType "confirmPass['#password']"
         :data-options "label:'Confirmar Contraseña:',labelPosition:'top',required:true,width:'100%'"}))
    (list
      (build-button
        {:id "submit"
         :href "javascript:void(0)"
         :text "Cambiar Contraseña"
         :class "easyui-linkbutton c6"}))))

(defn reset-jwt-scripts []
  [:script
   "
    $(document).ready(function() {
      $.extend($.fn.validatebox.defaults.rules, {
        confirmPass: {
          validator: function(value, param) {
            var password = $(param[0]).passwordbox('getValue');
            return value == password;
          },
          message: 'La contraseña confirmadora no es igual.'
        }
      });

      $('a#submit').click(function() {
        $('.fm').form('submit', {
          url: '/reset_password',
          onSubmit: function() {
            return $(this).form('enableValidation').form('validate');
          },
          success: function(data) {
            var dta = JSON.parse(data);
            try {
              if(dta.hasOwnProperty('url')) {
                $.messager.alert({
                  title: 'Processado!',
                  msg: 'Su contraseña se ha cambiado!',
                  fn: function() {
                    window.location.href = dta.url;
                  }
                });
              } else if(dta.hasOwnProperty('error')) {
                $.messager.alert('Error', dta.error, 'error');
              }
            } catch(e) {
              console.error('Invalid JSON');
            }
          }
        });
      });
    });
   "]
  )
;; End reset-jwt
