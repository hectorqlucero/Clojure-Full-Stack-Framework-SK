var fm = $('.fm');
var return_url = window.location.href;

function saveItem() {
  fm.form("submit", {
    url: window.location.href + '/save',
    onSubmit: function() {
      if($(this).form('validate')) {
        $('a#submit').linkbutton('disable');
      }
      return $(this).form("validate");
    },
    success: function(result) {
      var json = JSON.parse(result);
      if(json.error) {
        $.messager.show({
          title: 'Error',
          msg: json.error
        });
        $('a#submit').linkbutton('enable');
      } else {
        $.messager.alert({
          title: 'Exito',
          msg: json.success,
          fn: function() {
            window.location.href = return_url;
          }
        })
      }
    }
  })
}
