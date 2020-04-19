(:require [hiccup.page :refer [include-js]
           [sk.models.util :refer [build-field
                                   build-texteditor
                                   build-image-field
                                   build-image-field-script
                                   build-radio-buttons
                                   build-button]]])
;; Hidden field
[:input {:type "hidden" :id "id" :name "id"}]

;; Regular textbox field
(build-field
  {:id "firstname"
   :name "firstname"
   :class "easyui-textbox"
   :prompt "Enter your firstname"
   :data-options "label:'First Name:', labelPosition:'top', required:true, width:'100%'"})

;; Password field
(build-field
  {:id "password"
   :name "password"
   :class "easyui-passwordbox"
   :prompt "Your prefered password here"
   :data-options "label:'Password:',labelPosition:'top',required:true,width:'100%'"})

;; Confirm password field
(build-field
  {:id "password1"
   :name "password1"
   :class "easyui-passwordbox"
   :prompt "validate your prefered password"
   :validType "confirmPass['#password']"
   :data-options "label:'Confirm Password:',labelPosition:'top',required:true,width:'100%'"})

;; Date field
(build-field
  {:id "fecha"
   :name "fecha"
   :class "easyui-datebox"
   :prompt "mm/dd/yyy"
   :data-options "label:'Fecha:',labelPosition:'top',required:true,width:'100%'"})

;; Time field
(build-field
  {:id "hora"
   :name "hora"
   :class "easyui-combobox"
   :prompt "Escojer la hora..."
   :data-options "label:'Hora:',labelPosition:'top',
                 method:'GET',
                 url:'/table_ref/get-time',
                 width:'100%'"})

;; Validatebox field
(build-field
  {:id "titulo"
   :name "titulo"
   :class "easyui-textbox easyui-validatebox"
   :validType "length[0,100]"
   :data-options "label:'Titulo:',labelPosition:'top',required:true,width:'100%'"})

;; Email field
(build-field
  {:id "email"
   :name "email"
   :class "easyui-textbox easyui-validatebox"
   :prompt "Enter email here..."
   :validType "email"
   :data-options "label:'Email:',labelPosition:'top',required:true,width:'100%'"})

;; Text editor field
(build-text-editor
  {:label "Detalles del evento:"
   :id "texteditor"
   :name "detalles"
   :placeholder "Detalles del evento...."})

;; Phone field
(build-field
  {:id "cell"
   :name "cell"
   :class "easyui-maskedbox"
   :mask "(999) 999-9999"
   :data-options "label:'Cell Phone:',labelPosition:'top',width:'100%'"})

;; Build image field
(build-image-field)

;; Textarea field
(build-field
  {:id "lugar"
   :name "lugar"
   :class "easyui-textbox"
   :prompt "Punto de reunion ex. Parque Hidalgo"
   :data-options "label:'Punto de Reunion:',
                 labelPosition:'top',
                 required:true,
                 multiline:true,
                 width:'100%',
                 height:120" })

;; Combobox external data
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

;; Radio buttons fields
(build-radio-buttons
  "Active?"
  (list
    {:id "active_no"
     :name "active"
     :class "easyui-radiobutton"
     :value "F"
     :data-options "label:'No',checked:true"}
    {:id "active_yes"
     :name "active"
     :class "easyui-radiobutton"
     :value "T"
     :data-options "label:'Yes'"}))

;; Jquery document ready
[:script
 "
 $(document).ready(function() {

 });
 "]

;; Javascript to support image and editor fields
[:script
     "
      function newItem() {
        dg.datagrid('unselectAll');
        $('#image1').attr('src','/images/placeholder_profile.png');
        dlg.dialog('open').dialog('center').dialog('setTitle', 'Nuevo Record');
        windowHeight = $(window).height() - ($(window).height() * 0.2);
        dlg.dialog('resize', {height: windowHeight}).dialog('center');
        fm.form('clear');
        url = window.location.href;
      }

      $(function() {
        $('#texteditor').richText();
      });

      $('.dlg').dialog({
        onOpen: function() {
          $('#texteditor').val('').trigger('change');
        }
      });

      $('.fm').form({
        onLoadSuccess: function(data) {
          $('#texteditor').trigger('change');
          var url = '/table_ref/get_imagen/' + data.id;
          $.get(url, function(data) {
            var the_src = $(data).attr('src');
            $('#image1').attr('src', the_src);
          });
        }
      });
     "]

;; Javascript to support confirm password field
[:script
 "
  $.extend($.fn.validatebox.defaults.rules, {
      confirmPass: {
          validator: function(value, param) {
              var password = $(param[0]).passwordbox('getValue');
              return value == password;
          },
          message: 'The confirm password does not match the password!'
      }
  });
 "]

;; Javascript to check if email exists on system
[:script
 "
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
                              msg: 'This user exists in the database!',
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
 "]
