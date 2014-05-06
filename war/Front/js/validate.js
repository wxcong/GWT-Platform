function inviteFriend() {
  alert("-----------");
  var email = document.getElementById("friendemail").value;
  var playerId = document.getElementById("friendplayerId").value;
  if(email == "") {
    alert("Please enter the email");
    return;
  }else {
	if(playerId == "")//guest mode
	{
	  produceGuestAccount(email);
	  
	}else {//friend mode
	  produceFriendInvite(playerId, email);
	}
  }
}

function produceFriendInvite(playerId, recipientEmailAddress) {
  var server="http://5-dot-smg-server-rl.appspot.com/";
  var myPlayerId = getCookieValue("userId");
  var accessSignature = getCookieValue("accessSignature");
  var targetId = playerId;
  var url = server + "playerInfo?playerId=";
	  url += myPlayerId;
	  url += "&targetId=";
	  url += targetId;
	  url += "&accessSignature=";
	  url += accessSignature;
	  
	  console.log(url);
	  $.ajax({
	    url: url,
	    type: "GET",
	    success: function(data, textStatus, jqXHR) {
	      console.log("Access Success:" + JSON.stringify(data));
	      if(data["error"] != undefined) {
	        console.log(data["error"]);
	        console.log("The target playId is wrong");
	        alert("Please enter a right playerId");
	      }else {
	    	console.log("The target playerId is right");
	    	produceURL(targetId, recipientEmailAddress);
	    	//Begin to insert the new match
	    	window.handleAsynInviteModeButtonClick(targetId);
	      }
	    },
	    error: function(jqXHR, textStatus, errorThrown) {
	      console.log("Access Error:" + textStatus + " " + errorThrown);
	      console.log(jqXHR.responseText);
	      alert("Access Server Error");
	    }
	  });	
}

function produceURL(targetId, recipientEmailAddress) {
  var gameURL = window.location.href;
  gameURL += "&myPlayerId=";
  gameURL += targetId;
  sendURLByEmail(gameURL, recipientEmailAddress);
}

function produceTmpURL(guestPlayerId, recipientEmailAddress) {
  var gameURL = window.location.href;
  var guestPassword = "123456"
  gameURL += "&guestPlayerId=";
  gameURL += guestPlayerId;
  gameURL += "&password=";
  gameURL += guestPassword;
  sendURLByEmail(gameURL, recipientEmailAddress);
}

function sendURLByEmail(gameURL, recipientEmailAddress) {
  var xmlHttp;
  var message = {
    "subject": "Game Invitation",
	"content": gameURL,
	"email": recipientEmailAddress,
	"name": "GWT Container"
  }
  if(window.XMLHttpRequest) {
    xmlHttp = new window.XMLHttpRequest();
  }else {
	xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
  }
  xmlHttp.onreadystatechange = function() {
	alert(xmlHttp.readyState);
	alert(xmlHttp.status);
    if(xmlHttp.readyState==4 && xmlHttp.status==200) {
      console.log(xmlHttp.responseText);
      alert(xmlHttp.responseText);
    }
  }
  xmlHttp.open("POST", "sendemailservlet", true);
  xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
  xmlHttp.send(JSON.stringify(message));
}

function produceRandomString(num) {
  var  x="0123456789qwertyuioplkjhgfdsazxcvbnm";
  var  tmp = "";
  for(var i=0;i<num;i++)   {
    tmp += x.charAt(Math.ceil(Math.random()*100000000)%x.length);
  }
  return tmp;
}

function produceGuestAccount(recipientEmailAddress) {
  var server = "http://5-dot-smg-server-rl.appspot.com/";
  var email = produceRandomString(16) + "@";
  email += produceRandomString(6) + ".";
  email += "com";
  var password = "123456";
  var url = server + "user";
  var firstname = "";
  var lastname = "Guest";
  var nickname = "";
  var jsonObjForInsert = {
    'email': email,
    'password': password,
    'firstname': firstname,
    'lastname': lastname,
    'nickname': nickname};
  var jsonString = JSON.stringify(jsonObjForInsert);
  $.ajax({
    url: url,
	dataType: 'json',
	data: jsonString,
	type: "POST",
	success: function(data, textStatus, jqXHR) {
	  console.log("Access Server Success");
	  if(data["error"]==undefined) {
	    console.log("Insert Success:" + JSON.stringify(data));
	    produceTmpURL(data["userId"], recipientEmailAddress);
	    //begin to insert the new match
	    window.handleAsynInviteModeButtonClick(data["userId"]);
	  }else {
	    console.log("Insert Error:" + JSON.stringify(data));
	    produceGuestAccount(recipientEmailAddress);
	  }
	},
	error: function(jqXHR, textStatus, errorThrown) {
	  console.log("Access Server Error:" + errorThrown);   
	}
  });
}