// $.parser.onComplete = function () {
// }
// function menu() {
//   var menuIndex = parseInt(sessionStorage.getItem("calls-menu-index")) || 0;
//   $("#menu-accordion").accordion({
//     selected: menuIndex
//   });
// }
// function openPanel() {
//   var selected = $("#menu-accordion").accordion("getSelected");
//   selected.find(".in-menu").each(function () {
//     $(this).linkbutton();
//     $(this).fadeIn(this, 100);
//   });
//   saveStateMenu(selected);
// }
// function saveStateMenu(option) {
//   if (option) {
//     var index = $("#menu-accordion").accordion("getPanelIndex", option);
//     sessionStorage.setItem("calls-menu-index", index);
//   }
// }
// document.onreadystatechange = function() { 
//   if (document.readyState !== "complete") { 
//     document.querySelector("body").style.visibility = "hidden"; 
//     document.querySelector("#content").style.visibility = "visible"; 
//   } else { 
//     document.querySelector("#content").style.display = "none"; 
//     document.querySelector("body").style.visibility = "visible"; 
//   } 
// }; 
$('body').append('<div style="" id="loadingDiv"><div class="loader">Loading...</div></div>');
$(window).on('load', function(){
  setTimeout(removeLoader, 10); //wait for page load PLUS two seconds.
});
function removeLoader(){
    $( "#loadingDiv" ).fadeOut(500, function() {
      // fadeOut complete. Remove the loading div
      $( "#loadingDiv" ).remove(); //makes page more lightweight 
  });  
}
