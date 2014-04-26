package org.client.container;

import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.Socket;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.appengine.channel.client.ChannelFactory.ChannelCreatedCallback;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.Window;

public class ServerApi {
	
  private static Socket socket = null;
  private static ChannelFactory channelFactory = null;
  private static Channel channel = null;
  private static Container container = null;
  private static boolean sendEmptyState = false;
  private static boolean isPlayerInfoDisplayed = false;
  
  public static void register(Container container) {
    ServerApi.container = container;
  }
  
  public static void getPlayerInfo(
      String serverName,
      String myPlayerId,
      String targetId,
      String accessSignature) {
    StringBuilder sb = new StringBuilder(serverName);
    sb.append("playerInfo?playerId=")
      .append(myPlayerId)
      .append("&")
      .append("targetId=")
      .append(targetId)
      .append("&")
      .append("accessSignature=")
      .append(accessSignature);
    String url = sb.toString();
    String obj = myPlayerId.equals(targetId) ? "my" : "opp";
    sendMessageForGetPlayerInfo(url, obj);
  }
  
  public static native void sendMessageForGetPlayerInfo(String url, String obj) /*-{
  	var xmlHttp;
    if($wnd.XMLHttpRequest) {
      xmlHttp = new $wnd.XMLHttpRequest();
    }else {
      xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");	
    }
    xmlHttp.onreadystatechange = function() {
      if(xmlHttp.readyState==4 && xmlHttp.status==200) {
        var response = xmlHttp.responseText;
        var resJson = JSON.parse(response);
        if(resJson["error"] != undefined) {
          alert(resJson["error"]);
        }else {
          alert(response);
          @org.client.container.ServerApi::messageHandlerForPlayerInfo(Ljava/lang/String;Ljava/lang/String;) (response, obj)
        }
      }
    }
    xmlHttp.open("GET", url, true);
    xmlHttp.send();
  }-*/;
  
  public static void messageHandlerForPlayerInfo(String response, String obj) {
    JSONValue responseValue = JSONParser.parseStrict(response);
    JSONObject responseObject = null;
    if(responseValue != null) {
      responseObject = responseValue.isObject();
    }else{
        throw new RuntimeException("response is null");
    } 
    String nickname = responseObject.get("nickname").isString().stringValue();
    String firstname = responseObject.get("firstname").isString().stringValue();
    String imageURL = responseObject.get("imageURL").isString().stringValue();
    setPlayerInfo(obj, nickname, firstname, imageURL);
  }
  
  public static native void setPlayerInfo(String obj, String nickname, String firstname, String imageURL) /*-{
    $doc.getElementById(obj + "profile").src = imageURL;
    $doc.getElementById(obj + "firstname").innerHTML = "Firstname: " + firstname;
    $doc.getElementById(obj + "nickname").innerHTML = "Nickname: " + nickname;
  }-*/;
  
  public static void sendEnterQueueAsyn(
		  String serverName,
		  JSONString gameId,
		  JSONString myPlayerId,
	      JSONString accessSignature) {
	    StringBuilder sb = new StringBuilder(serverName);
	    sb.append("queue");
	    String url = sb.toString();
	    JSONObject postInfo = new JSONObject();
	    postInfo.put("accessSignature", accessSignature);
	    postInfo.put("playerId", myPlayerId);
	    postInfo.put("gameId", gameId);
	    String message = postInfo.toString();
	    sendMessageForEnterQueueAsyn(message, url);
	  }
  
  public static native void sendMessageForEnterQueueAsyn(String message, String url) /*-{
  	var xmlHttp;
  	var _a = $wnd.XMLHttpRequest;
	if(_a){
	  xmlHttp = new $wnd.XMLHttpRequest();
	}else{
	  xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlHttp.onreadystatechange = function() {
	  if(xmlHttp.readyState==4 && xmlHttp.status==200) {
	    var response = xmlHttp.responseText;
	    var resJson = JSON.parse(response);
	    if(resJson["error"] != undefined) {
	      alert(resJson["error"]);
	    }
	    else {
	      @org.client.container.ServerApi::messageHandlerForAysnEnterQueue(Ljava/lang/String;) (response);
	    }
	  }
	}  
    xmlHttp.open("POST", url, true);
    xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlHttp.send(message);
  }-*/;
  
  public static void messageHandlerForAysnEnterQueue(String response) {
    JSONValue responseValue = JSONParser.parseStrict(response);
    JSONObject responseObject = null;
    if(responseValue != null) {
      responseObject = responseValue.isObject();
    }else {
      throw new RuntimeException("response is null");
    } 
    JSONValue playerIds = responseObject.get("playerIds");
    if(playerIds != null) {
      Window.alert("Enter the queue and get the match, so begins to insert a new match");
      container.setPlayerIds(playerIds.isArray());
      container.sendGameReady(0);
    }else {
      sendEmptyState = true;
      Window.alert("Enter the queue successfully and waiting for match");
    }
  }
  
	  
  public static void sendEnterQueue(
	  String serverName,
	  JSONString gameId,
	  JSONString myPlayerId,
      JSONString accessSignature) {
    StringBuilder sb = new StringBuilder(serverName);
    sb.append("queue");
    String url = sb.toString();
    JSONObject postInfo = new JSONObject();
    postInfo.put("accessSignature", accessSignature);
    postInfo.put("playerId", myPlayerId);
    postInfo.put("gameId", gameId);
    String message = postInfo.toString();
    sendMessageForEnterQueue(message, url);
  }
  
  public static native void sendMessageForEnterQueue(String message, String url) /*-{
  	var xmlHttp;
  	var _a = $wnd.XMLHttpRequest;
	if(_a){
	  xmlHttp = new $wnd.XMLHttpRequest();
	}else{
	  xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlHttp.onreadystatechange = function() {
	  alert(xmlHttp.readyState);
	  alert(xmlHttp.status);
	  if(xmlHttp.readyState==4 && xmlHttp.status==200) {
	    var response = xmlHttp.responseText;
	    var resJson = JSON.parse(response);
	    if(resJson["error"] != undefined) {
	      alert(resJson["error"]);
	    }
	    else {
	      @org.client.container.ServerApi::channelBuilder(Ljava/lang/String;) (response);
	    }
	  }
	}  
    xmlHttp.open("POST", url, true);
    xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlHttp.send(message);
  }-*/;
  
  public static void channelBuilder(String response) {
    JSONValue responseValue = JSONParser.parseStrict(response);
    JSONObject responseObject = null;
    if(responseValue != null) {
      responseObject = responseValue.isObject();
    }else {
      throw new RuntimeException("response is null");
    }
    
	String channelTokens = responseObject.get("channelToken").isString().stringValue();
	JSONValue playerIds = responseObject.get("playerIds");
	buildChannel(channelTokens, playerIds);
  }

  public static void buildChannel(String token, final JSONValue playerIds) {
	ChannelFactory.createChannel(token, new ChannelCreatedCallback() {
	  @Override
	  public void onChannelCreated(final Channel result) {	
	    socket = result.open(new SocketListener() {	
		  @Override
		  public void onOpen() {
		    channel = result;
			Window.alert("Channel Opened!");
			if(playerIds != null) {
			  Window.alert("Receive PlayeIds");
			  container.setPlayerIds(playerIds.isArray());
			  container.sendGameReady(0);
			}
		  }
			
	      @Override
		  public void onMessage(String message) {
		    channelMessageHandler(message);
		  }
			
		  @Override
		  public void onError(SocketError error) {
			Window.alert("Channel error: " + error.getCode() + " : " + error.getDescription());
		  }
			
		  @Override
		  public void onClose() {
			Window.alert("Channel closed!");
		  }	
		});
      }
    });
  }
  
  public static void closeSocket() {
    socket.close();
  }
  
  public static void channelMessageHandler(String message) {
    JSONObject response = JSONParser.parseStrict(message).isObject();
	if(response.get("error") == null) {
      JSONValue matchId = response.get("matchId");
	  JSONValue playerIds = response.get("playerIds");
	  if(matchId != null && playerIds != null) {
		Window.alert("Receive the matchId and playerIds and then begin to create the initial move");
	    container.setMatchId((matchId.isString().stringValue())); 
		container.setPlayerIds(playerIds.isArray());
		container.updateUi(new JSONObject(), new JSONArray(), new JSONString("1"));
	    container.getOppInfoFromServer();
	    container.getMyInfoFromServer();
	  }else {
		JSONValue _state = response.get("state");
		if(_state!=null) {
		  JSONObject state = _state.isObject();
		  JSONArray lastMove = response.get("lastMove").isArray();
		  JSONString lastMovePlayerId = new JSONString("1");
		  container.updateUi(state, lastMove, lastMovePlayerId);
		}
	  }
	}else {
	  throw new RuntimeException("error");
    }
  }
  
  public static void sendInsertNewMatch(
      String serverName,
      JSONString accessSignature,
      JSONArray playerIds,
      JSONString gameId,
      int type) {
    StringBuilder sb = new StringBuilder(serverName);
	sb.append("newMatch");
    String url = sb.toString();
	
    JSONObject postInfo = new JSONObject();
	postInfo.put("accessSignature", accessSignature);
	postInfo.put("playerIds", playerIds);
	postInfo.put("gameId", gameId);
	String message = postInfo.toString();
	Window.alert("The postInfo is" + message);
	sendMessageForInsertNewMatch(message, url, type);
	Window.alert("insert new match");
  }
  
  public static native void sendMessageForInsertNewMatch(String message, String url, int type) /*-{
    var xmlHttp;
	if($wnd.XMLHttpRequest){
	  xmlHttp = new $wnd.XMLHttpRequest();
	}else{
	  xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlHttp.onreadystatechange = function() {
	  if(xmlHttp.readyState==4 && xmlHttp.status==200) {
	    var response = xmlHttp.responseText;
	    var resJson = JSON.parse(response);
	    if(resJson["error"] != undefined) {
	      alert(resJson["error"]);
	    }
	    else {
	      @org.client.container.ServerApi::httpMessageHandler(Ljava/lang/String;I) (response, type)
	    }
	  }
	}  
    xmlHttp.open("POST", url, true);
    xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlHttp.send(message);
  }-*/;
  
  public static void httpMessageHandler(String message, int type) {
    JSONObject response = JSONParser.parseStrict(message).isObject();
    String matchId = response.get("matchId").isString().stringValue();
    Window.alert("Receive the matchId:" + matchId);
    container.setMatchId(matchId);
    Window.alert("Get and set the matchId, then produce the initial move");
    if(type==1) {
      container.updateUi(new JSONObject(), new JSONArray(), new JSONString("1"));
    }
    container.getOppInfoFromServer();
    container.getMyInfoFromServer();
    isPlayerInfoDisplayed = true;
  }
  
  public static void sendMakeMove(
      String serverName,
      String gameOverReason,
      JSONString accessSignature,
      JSONArray playerIds,
      String matchId,
      JSONArray move) {
      StringBuilder sb = new StringBuilder(serverName);
	  sb.append("matches/").append(matchId);
	  String url = sb.toString();
	  JSONObject postInfo = new JSONObject();
	  postInfo.put("accessSignature", accessSignature);
	  postInfo.put("playerIds", playerIds);
	  postInfo.put("operations", move);
	  if(gameOverReason != null) {
	    postInfo.put("gameOverReason", new JSONString(gameOverReason));
	  }
	  String message = postInfo.toString();
	  sendMessageForMakeMove(message, url);
  }
  
  public static native void sendMessageForMakeMove(String message, String url) /*-{
  	var xmlHttp;
	if($wnd.XMLHttpRequest){
	  xmlHttp = new $wnd.XMLHttpRequest();
	}else{
	  xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlHttp.onreadystatechange = function() {
	  if(xmlHttp.readyState==4 && xmlHttp.status==200) {
	    var response = xmlHttp.responseText;
	    var resJson = JSON.parse(response);
	    if(resJson["error"] != undefined) {
	      alert(resJson["error"]);
	    }
	    else {
	      @org.client.container.ServerApi::_httpMessageHandler(Ljava/lang/String;) (response);
	    }
	  }
	}  
    xmlHttp.open("POST", url, true);
    xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlHttp.send(message);
  }-*/;
  
  public static void _httpMessageHandler(String message) {
	Window.alert("End the Game");
    JSONObject response = JSONParser.parseStrict(message).isObject();
	if(response != null) {
	  JSONValue _state = response.get("state");
	  if(_state != null) {
	    JSONObject state = _state.isObject();
	    JSONArray lastMove = response.get("lastMove").isArray();
	    JSONString lastMovePlayerId = new JSONString("1");
	    container.updateUi(state, lastMove, lastMovePlayerId);
	  }
	}
  }
  
  public static void getMatchInfo(
      String serverName,
      String myPlayerId,
      String accessSignature,
      String gameId) {
    StringBuilder sb = new StringBuilder(serverName);
	    sb.append("newMatch/")
	      .append(myPlayerId)
	      .append("?")
	      .append("accessSignature")
	      .append("=")
	      .append(accessSignature)
	      .append("&")
	      .append("gameId")
	      .append("=")
	      .append(gameId);
	    String url = sb.toString();
	    Window.alert("Prepare to get the match Info in the server API");
	    Window.alert(url);
	    sendMessageForGetMatchInfo(url);
  }
  
  public static native void sendMessageForGetMatchInfo(String url) /*-{
  	var xmlHttp;
    if($wnd.XMLHttpRequest) {
      xmlHttp = new $wnd.XMLHttpRequest();
    }else {
      xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");	
    }
    xmlHttp.onreadystatechange = function() {
      if(xmlHttp.readyState==4 && xmlHttp.status==200) {
        var response = xmlHttp.responseText;
        var resJson = JSON.parse(response);
        if(resJson["error"] != undefined) {
          alert(resJson["error"]);
        }else {
          alert(response);
          @org.client.container.ServerApi::___httpMessageHandler(Ljava/lang/String;) (response);
        }
      }
    }
    xmlHttp.open("GET", url, false);
    xmlHttp.send();
  }-*/;
  
  public static void ___httpMessageHandler(String message) {
    JSONObject response = JSONParser.parseStrict(message).isObject();
    if(response != null) {
	  JSONString matchId = response.get("matchId").isString();
	  JSONArray playerIds = response.get("playerIds").isArray();
	  container.setMatchId(matchId.stringValue());
	  container.setPlayerIds(playerIds);
	  if(sendEmptyState) {
	    container.updateUi(new JSONObject(), new JSONArray(), new JSONString("1"));
	  }
	  if(!isPlayerInfoDisplayed) {
	    container.getOppInfoFromServer();
	    container.getMyInfoFromServer();
	    isPlayerInfoDisplayed = true;
	  }
	}else {
	  throw new RuntimeException("Response is null");
	}	  
  }
  
  public static void getNewState(
      String serverName,
      String matchId,
      String myPlayerId,
      String accessSignature) {
    StringBuilder sb = new StringBuilder(serverName);
	sb.append("state/")
	  .append(matchId)
	  .append("?")
	  .append("playerId")
	  .append("=")
	  .append(myPlayerId)
	  .append("&")
	  .append("accessSignature")
	  .append("=")
	  .append(accessSignature);
	  String url = sb.toString();
	  sendMessageForGetNewState(url);
  }
  
  public static native void sendMessageForGetNewState(String url) /*-{
  	var xmlHttp;
    if($wnd.XMLHttpRequest) {
      xmlHttp = new $wnd.XMLHttpRequest();
    }else {
      xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");	
    }
    xmlHttp.onreadystatechange = function() {
      if(xmlHttp.readyState==4 && xmlHttp.status==200) {
        var response = xmlHttp.responseText;
        var resJson = JSON.parse(response);
        if(resJson["error"] != undefined) {
          alert(resJson["error"]);
        }else {
          @org.client.container.ServerApi::__httpMessageHandler(Ljava/lang/String;) (response);
        }
      }
    }
    xmlHttp.open("GET", url, true);
    xmlHttp.send();
  }-*/;
  
  public static void __httpMessageHandler(String message) {
    JSONObject response = JSONParser.parseStrict(message).isObject();
	if(response != null) {
      JSONString matchId = response.get("matchId").isString();
	  JSONObject state = response.get("state").isObject();
	  JSONArray lastMove = response.get("lastMove").isArray();
	  JSONString lastMovePlayerId = new JSONString("1");
	  container.updateUi(state, lastMove, lastMovePlayerId);
	}else {
	  throw new RuntimeException("Response is null");
	}
  }
}
