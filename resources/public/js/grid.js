var dg = $(".dg");
var dlg = $('.dlg');
var fm = $('.fm');
var windowHeight;
var token = $("#__anti-forgery-token").val();

$(document).ready(function() {
  dg.datagrid();
  dg.datagrid("enableFilter");
});

function returnItem(url) {
  window.location.href = url;
}

function newItem() {
  dlg.dialog("open").dialog("center").dialog('setTitle', 'Nuevo Record');
  windowHeight = $(window).height() - ($(window).height() * 0.2);
  dlg.dialog('resize', {height: windowHeight}).dialog('center');
  fm.form("clear");
  url = window.location.href;
}

function editItem(params) {
  var row = dg.datagrid('getSelected');
  if (row) {
    dlg.dialog("open").dialog('center').dialog('setTitle', 'Editar Record');
    windowHeight = $(window).height() - ($(window).height() * 0.2);
    dlg.dialog('resize', {height: windowHeight}).dialog('center');
    fm.form("clear");
    var id = params.hasOwnProperty('idField') ? params.idField : row.id;
    $.get(window.location.href + '/edit/' + id, function(data) {
      fm.form('load',data);
    }, 'json')
  }
}

function saveItem() {
  fm.form("submit", {
    url: window.location.href + '/save',
    queryParams: {
      '__anti-forgery-token': token
    },
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
        $.messager.show({
          title: 'Exito',
          msg: json.success
        })
        dlg.dialog("close");
        dg.datagrid("reload");
      }
    }
  })
}

function deleteItem() {
  var row = dg.datagrid("getSelected");
  if(row) {
    var url = window.location.href + '/delete';
    $.messager.confirm('Confirmar','Esta seguro que quiere remover este record?',function(r) {
      $.post(url, {id:row.id,'__anti-forgery-token':token},function(result) {
        if(result.success) {
          dg.datagrid("reload");
        } else {
          $messager.show({
            title: 'Error',
            msg: result.error
          });
        }
      },'json');
    })
  }
}

function dialogClose() {
  dlg.dialog("close");
}
