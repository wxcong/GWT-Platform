/**
 * Created by Tao on 4/22/14.
 */

function countdown() {
  $("#timer1").remove();
  
  
  $("#timerParent1").append("<div id='timer1' class='cell'><div id='holder'><div class='digits1'></div></div></div>");
  
  $(".digits1").countdown({
      image: "img/digits.png",
      format: "ss",
      startTime: "30",
      continuous: true   
  });

}


$(function(){
    $(".digits1").countdown({
        image: "img/digits.png",
        format: "ss",
        startTime: "00",
        continuous: true
    });

});
