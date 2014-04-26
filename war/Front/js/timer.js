/**
 * Created by Tao on 4/22/14.
 */
$(function(){
    $(".digits").countdown({
        image: "img/digits.png",
        format: "ss",
        startTime: "00",
        continuous: true
    });
});


window.countdown = function() {
  $("#timer").remove();
  
  $("#timerParent").append("<div id='timer' class='cell'><div id='holder'><div class='digits'></div></div></div>");
  
   $(".digits").countdown({
       image: "img/digits.png",
       format: "ss",
       startTime: "60",
       continuous: true
   });
}