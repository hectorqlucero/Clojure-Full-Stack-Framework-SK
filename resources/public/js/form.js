var fm = $('.fm');

$(document).ready(function() {
  $('a#submit').click(function() {
    $('form.fm').form('submit', {
      onSubmit: function() {
        if($(this).form('validate')) {
          $('a#submit').linkbutton('disable');
        }
        return $(this).form('validate');
      },
      success: function(data) {
        try {
          var dta = JSON.parse(data);
          if(dta.hasOwnProperty('url')) {
            $.messager.alert({
              title:'Exito',
              msg:'Procesado correctamente!',
              fn: function() {
                window.location.href = dta.url;
              }
            });
          } else if(dta.hasOwnProperty('error')) {
            $.messager.show({
              title:'Error: ',
              msg: dta.error
            });
            $('a#submit').linkbutton('enable');
          }
        } catch(e) {
          console.error('Invalid JSON');
        }
      }
    });
  });
}
