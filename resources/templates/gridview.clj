(ns sk.handlers.administrar.eventos.view
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
    (build-image-field)
    (build-field
      {:id           "titulo"
       :name         "titulo"
       :class        "easyui-textbox easyui-validatebox"
       :data-options "label:'Tituto para el calendario<small>(ex: VII Gran Fondo</small>):',
                      labelPosition:'top',
                      width:'100%',
                      required: true"
       :validType    "length[0,100]"})
    (build-text-editor
      {:label "Detalles del evento:"
       :id "texteditor"
       :name "detalles"
       :placeholder "Detalles del evento..."})
    (build-field
      {:id           "lugar"
       :name         "lugar"
       :class        "easyui-textbox"
       :data-options "label:'Punto de Reunión(<small>ex. Parque Hidalgo</small>):',
                      labelPosition:'top',
                      width:'100%',
                      multiline:true,height:120"})
    (build-field
      {:id           "fecha"
       :name         "fecha"
       :class        "easyui-datebox"
       :data-options "label:'Fecha/Evento:',
                      labelPosition:'top',
                      width:'100%',
                      required: true"})
    (build-field
      {:id    "hora"
       :name  "hora"
       :class "easyui-combobox"
       :data-options "url:'/table_ref/get-time',
                     method:'GET',
                     label:'Hora:',
                      labelPosition:'top',
                      width:'100%'"})
    (build-field
      {:id           "organiza"
       :name         "organiza"
       :class        "easyui-textbox"
       :data-options "label:'Quién Organiza:',
                      labelPosition:'top',
                      width:'100%',
                      required: true"})))

(defn toolbar-extra []
  (list
   [:a {:href         "javascript:void(0)"
        :class        "easyui-linkbutton"
        :data-options "iconCls:'icon-back',plain:true"
        :onclick      "returnItem('/eventos/list')"} "Regresar a los Eventos"]))

(defn eventos-view [title]
  (list
    (anti-forgery-field)
    (build-table
      title
      "/administrar/eventos"
      (list
        [:th {:data-options "field:'fecha',sortable:true"} "Fecha"]
        [:th {:data-options "field:'hora',sortable:true"} "Hora"]
        [:th {:data-options "field:'titulo',sortable:true"} "Evento"]
        [:th {:data-options "field:'lugar',sortable:false"} "Punto de Reunión"]
        [:th {:data-options "field:'organiza',sortable:true"} "Quién Organiza"]))
    (build-toolbar (toolbar-extra))
    (build-dialog title dialog-fields)
    (build-dialog-buttons)))

(defn eventos-scripts []
  (list
    (include-js "/js/grid.js")
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
    [:script (build-image-field-script)]))
