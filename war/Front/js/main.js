/**
 * Created by Tao on 4/22/14.
 */
$(document).ready(function() {
    $("a[name='developerInfo']").hover(function() {
        var gameId = this.id;
        gameId = gameId.substring(1);
        setGameIndex(gameId);
        getdeveloperInfo();
      });

      $("button[name='ratingButton']").popover({placement:'top'});


      $("#myprofile").hover(function(){
          var playerId=getCookieValue("userId");
          var accessSignature=getCookieValue("accessSignature");
          var targetId=playerId;
          ajaxCallGetPlayerInfo(playerId,targetId,accessSignature)
          $("#myprofile").popover({placement:"right"});
      });
      
      $("#oppprofile").hover(function(){
    	  var oppPlayerId = window.oppPlayerId;
          var playerId=getCookieValue("userId");
          var accessSignature=getCookieValue("accessSignature");
          var targetId=oppPlayerId;
          ajaxCallGetPlayerInfo(playerId,targetId,accessSignature);
          $("#oppprofile").popover({placement:"left"});
      });
	
      function ajaxCallGetPlayerInfo(playerId,targetId,accessSignature){
          var server="http://5-dot-smg-server-rl.appspot.com/";
          var url=server+"playerInfo?playerId=";
          url+=playerId;
          url+="&targetId=";
          url+=targetId;
          url+="&accessSignature=";
          url+=accessSignature;

          console.log(url);
          $.ajax({
              url: url,
              type: "GET",
              success: function(data, textStatus, jqXHR){
                  console.log("Login_SUCCESS:" +JSON.stringify(data));
                  data = JSON.parse(data);
                  var nickname = data["nickname"];
                  var lastname = data["lastname"];
                  var firstname = data["firstname"];
                  if (playerId == targetId) {
                      document.getElementById("myprofile").dataset['content'] = "PlayerId:" + playerId + "\n" + "NickName:" + nickname + "\n" + "FirstName:" + firstname + "\n" + "LastName:" + lastname;
                  }else{
                      document.getElementById("oppprofile").dataset['content'] = "PlayerId:" + playerId + "\n" + "NickName:" + nickname + "\n" + "FirstName:" + firstname + "\n" + "LastName:" + lastname;
                  }
              },
              error: function(jqXHR, textStatus, errorThrown){
                  console.log("Login_ERROR: " + textStatus + " " + errorThrown);
                  console.log(jqXHR.responseText);
              }
          });
      }
      $("#oppprofile").popover({placement:"left"}); 
	
    var imageURL = getCookieValue("imageURL");
    var email = getCookieValue("email");
    if(email != "" && imageURL != ""){
        $("a[name=user-name]").html("<span class=\"fa fa-user\"></span>" + email + "<b class=\"caret\"></b>");
        $("img[name=user-image]").attr("src", imageURL);
        $("ul[name=usericon]").hide();
    }
})