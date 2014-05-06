
pagecheck();

function pagecheck() {
      var href = window.location.href;
      var guestPlayerIdStart = href.indexOf("&guestPlayerId");
      var _guestPlayerIdStart = guestPlayerIdStart + 15;
      
      var passwordStart = href.indexOf("&password");
      var _passwordStart = passwordStart + 10;
      
      var myPlayerIdStart = href.indexOf("&myPlayerId");
      var _myPlayerIdStart = myPlayerIdStart + 12;
      
      var addressStart = href.indexOf("address");
      var _addressStart = addressStart + 8;

      var heightStart = href.indexOf("&height");
      var _heightStart = heightStart + 8;

      var widthStart = href.indexOf("&width");
      var _widthStart = widthStart + 7;

      var gameIdStart = href.indexOf("&gameId");
      var _gameIdStart = gameIdStart + 8;
      
      var address;
      var height;
      var width;
      var gameId;    
      
      if(addressStart == -1 || heightStart == -1 || widthStart == -1 || gameIdStart == -1) {
        alert("Please choose a game to play");
        window.location.href = "./mainpage.html";
        return;
      }
      
      address = href.substring(_addressStart, heightStart);
      height = href.substring(_heightStart, widthStart);
      width = href.substring(_widthStart, gameIdStart);
 
      if(guestPlayerIdStart != -1) {
        var guestPlayerId = href.substring(_guestPlayerIdStart, passwordStart);
        var guestPlayerPassword = "123456";
        gameId = href.substring(_gameIdStart, guestPlayerIdStart);
        removeCookies();
        ajaxCallLoginByUserID(guestPlayerId, guestPlayerPassword);
        return;
      }else {
        if(myPlayerIdStart != -1) {
          var myPlayerId = href.substring(_myPlayerIdStart);
          var playerIdInCookie = getCookieValue("userId");
          gameId = href.substring(_gameIdStart, myPlayerIdStart);
          //If have login using this account
          if(myPlayerId == playerIdInCookie) {
            inviteGameLoad(address, height, width, gameId);
          }else {//If have not login using this account
            $('#loginModal').modal();
            return;
          }
        }else {
          //If have not login
          if(getCookieValue("userId")=="") {
            $('#loginModal').modal();
            return;
          }
        }
      }
    
     
      function removeCookies() {
        if(getCookieValue("userId")!="") {
          deleteCookie("userId", "/");	
        } 
        if(getCookieValue("accessSignature")!="") {
          deleteCookie("accessSignature", "/");
        }
        if(getCookieValue("email")!="") {
          deleteCookie("email", "/");
        }
        if(getCookieValue("imageURL")!="") {
          deleteCookie("imageURL", "/");
        }
      }
      
      function ajaxCallLoginByUserID(userId,password){
    	  var server = "http://5-dot-smg-server-rl.appspot.com/";
    	  var url=server+"user/";
    	  url+=userId;
    	  url+="?password=";
    	  url+=password;

    	  console.log(url);
    	  $.ajax({
    	    url: url,
    	    type: "GET",
    	    success: function(data, textStatus, jqXHR){ 
    	      data = JSON.parse(data);
    	      
    	      if(data["error"]==undefined) {
    	        console.log("Login_SUCCESS:" +JSON.stringify(data));
    	        setCookie("email", data["email"], "", "/");
    	        setCookie("accessSignature", data["accessSignature"], "", "/");
    	        setCookie("userId", userId, "", "/");
    	        setCookie("imageURL", data["imageURL"], "", "/");
    	        inviteGameLoad(address, height, width, gameId);
    	      }else {
    	        console.log("Login_ERROR:" +JSON.stringify(data));
    	      }
    	    },
    	    error: function(jqXHR, textStatus, errorThrown){
    	      console.log("Login_ERROR: " + textStatus + " " + errorThrown);
    	      console.log(jqXHR.responseText);
    	    }
    	  });
    	}
}

function inviteGameLoad(address, height, width, gameId) {
  var playerId = getCookieValue("userId");
  var accessSignature = getCookieValue("accessSignature");
  if(playerId=="" || accessSignature=="") {
    alert("Please login first");
    return;
  }
  var address_ = "js/Games/" + address;
  document.getElementById("game-board").innerHTML = "<iframe id='board'></iframe>";
  document.getElementById("board").width = width;
  document.getElementById("board").height = height;
  document.getElementById("board").src = address_;
  
  document.getElementById("refresh").href = "javascript:refreshForInvite(" + "'" +
                 playerId + "'" + "," + "'" + gameId + "'" + "," + "'" + accessSignature + "'" + ")";
  
  document.getElementById("quit").href = "javascript:window.handleEndButtonClick('Quit')";
}

function refreshForInvite(playerId, gameId, accessSignature) {
  window.setMyPlayerIdExported(playerId);
  window.setGameIdExported(gameId)
  window.setIdsInContainerExported(playerId, accessSignature);
  window.handleRefreshButtonClick();
}
