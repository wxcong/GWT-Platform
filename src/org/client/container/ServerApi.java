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
  
  public static void register(Container container) {
    ServerApi.container = container;
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
	if($wnd.XMLHttpRequest){
	  xmlHttp = new XMLHttpRequest();
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
			  container.setPlayerIds(playerIds.isArray());
			  container.sendGameReady();
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
	    container.setMatchId((matchId.isString().stringValue())); 
		container.setPlayerIds(playerIds.isArray());
		container.updateUi(new JSONObject(), new JSONArray(), new JSONString("1"));
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
      JSONString gameId) {
    StringBuilder sb = new StringBuilder(serverName);
	sb.append("newMatch");
    String url = sb.toString();
	
    JSONObject postInfo = new JSONObject();
	postInfo.put("accessSignature", accessSignature);
	postInfo.put("playerIds", playerIds);
	postInfo.put("gameId", gameId);
	String message = postInfo.toString();
	sendMessageForInsertNewMatch(message, url);
  }
  
  public static native void sendMessageForInsertNewMatch(String message, String url) /*-{
    var xmlHttp;
	if($wnd.XMLHttpRequest){
	  xmlHttp = new XMLHttpRequest();
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
	      @org.client.container.ServerApi::httpMessageHandler(Ljava/lang/String;) (response);
	    }
	  }
	}  
    xmlHttp.open("POST", url, true);
    xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlHttp.send(message);
  }-*/;
  
  public static void httpMessageHandler(String message) {
    JSONObject response = JSONParser.parseStrict(message).isObject();
    String matchId = response.get("matchId").isString().stringValue();
    container.setMatchId(matchId);
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
	  xmlHttp = new XMLHttpRequest();
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
      String accessSignature) {
    StringBuilder sb = new StringBuilder(serverName);
	    sb.append("newMatch/")
	      .append(myPlayerId)
	      .append("?")
	      .append("accessSignature")
	      .append("=")
	      .append(accessSignature);
	    String url = sb.toString();
	    sendMessageForGetMatchInfo(url);
  }
  
  public static native void sendMessageForGetMatchInfo(String url) /*-{
  	var xmlHttp;
    if($wnd.XMLHttpRequest) {
      xmlHttp = new XMLHttpRequest();
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
          @org.client.container.ServerApi::___httpMessageHandler(Ljava/lang/String;) (response);
        }
      }
    }
    xmlHttp.open("GET", url, true);
    xmlHttp.send();
  }-*/;
  
  public static void ___httpMessageHandler(String message) {
    JSONObject response = JSONParser.parseStrict(message).isObject();
    if(response != null) {
	  JSONString matchId = response.get("matchId").isString();
	  JSONArray playerIds = response.get("playerIds").isArray();
	  container.setMatchId(matchId.stringValue());
	  container.setPlayerIds(playerIds);
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
      xmlHttp = new XMLHttpRequest();
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
