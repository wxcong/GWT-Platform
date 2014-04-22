function $(objStr)
{
  return document.getElementById(objStr);
}  
window.onload = function(){  
    var email = getCookieValue("email"); 
    var playerId = getCookieValue("playerId");
    var accessSignature = getCookieValue("accessSignature");
    
    if(email=="" || playerId=="" || accessSignature=="") {
      return;
    }else {
      $("welcome").innerHTML = "Welcome " + email + " !";
      return;
    }
}  