package org.client.container;

import com.google.common.collect.Lists;
import com.google.gwt.json.client.*;
import com.google.gwt.user.client.Window;

import org.client.container.GameApi.Operation;
import org.client.container.GameApi.EndGame;
import org.client.container.GameApi.SetTurn;
import java.util.List;


public class Container {  
  
  private String matchId = null;
  private String gameId = null;
  private String myPlayerId = null;
  private String accessSignature = null;
  private JSONArray lastMove = null;
  private List<String> playerIds = Lists.newArrayList();
  
  private final Serialization serialization = new Serialization();
  private final Deserialization deserialization = new Deserialization();
  
  private String serverName = "http://4-dot-smg-server.appspot.com/";
	
  //for UI test
  public void setGameId(String gameId) {
    this.gameId = gameId;
  }
  
  //for UI test
  public void setMyPlayerId(String myPlayerId) {
    this.myPlayerId = myPlayerId;
  }
  
  //for UI test
  public void setAccessSignature(String accessSignature) {
    this.accessSignature = accessSignature;
  }
  
  public Container() {
	ServerApi.register(this);
    setListener(this);
  }
  
  public native void setListener(Container container) /*-{
	$wnd.addEventListener("message", 
	  function(event) {
	    container.@org.client.container.Container::messageListener(Ljava/lang/String;) (event.data);
	  },false);
  }-*/;
  
  public void messageListener(String message) {
    JSONValue messageValue = JSONParser.parseStrict(message);
	JSONObject messageObject = null;
	if(messageValue != null) {
	  messageObject = messageValue.isObject();
	}else {
	  throw new RuntimeException("message is null");
	}
    if(messageObject != null) {
      JSONValue requestValue = messageObject.get("request");
	  JSONString requestString = null;
	  String request = null;
	  if(requestValue != null) {
	    requestString = requestValue.isString();
	  }else {
		throw new RuntimeException("request is null");  
	  }
	  if(requestString != null) {
	    request = requestString.stringValue();
	  }else {
	    throw new RuntimeException("request is not JSONString");
	  }
      switch (request) {
	    case "SEND_MAKE_MOVE": {
		  JSONValue gameOverReasonValue = messageObject.get("gameOverReason");
		  String gameOverReason = null;
		  if(gameOverReasonValue != null) {
		    gameOverReason = gameOverReasonValue.isString().stringValue();
		  } 
	      sendMakeMove(messageObject.get("move").isArray(),
		      gameOverReason);
		  break;
		}
		default: {
	      throw new RuntimeException("Request not exists");
		}
	  }
	}else {
	  throw new RuntimeException("message is not JSONObject");
	}
  }
  
  public void sendEnterQueue() {
    ServerApi.sendEnterQueue(
      serverName,
      new JSONString(gameId),
      new JSONString(myPlayerId),
      new JSONString(accessSignature));
  }
  
  public void setPlayerIds(JSONArray playerIds) {
    for(int i=0; i<playerIds.size(); i++) {
      this.playerIds.add(playerIds.get(i).isString().stringValue());	
    }
  }
  
  public void setMatchId(String matchId) {
    this.matchId = matchId;
  }
  
  public JSONArray getLastMove() {
    return lastMove;
  }
  
  public void sendGameReady() {
	JSONArray _playerIds = new JSONArray();
	int index = 0;
	for(String playerId : playerIds) {
	  _playerIds.set(index, new JSONString(playerId));
	  index ++;
	}
    ServerApi.sendInsertNewMatch(
        serverName,
        new JSONString(accessSignature),
        _playerIds,
        new JSONString(gameId));
  }
  
  public void closeSocket() {
    ServerApi.closeSocket();
  }
  
  public void updateUi(JSONObject state, JSONArray lastMove, 
    JSONString lastMovePlayerId) {
	JSONObject message = new JSONObject();
	JSONArray _playerIds = new JSONArray();
	int index = 0;
	for(String playerId : playerIds) {
	  _playerIds.set(index, new JSONString(playerId));
	  index ++;
	}
	message.put("state", state);
	message.put("lastMove", lastMove);
	message.put("lastMovePlayerId", lastMovePlayerId);
	message.put("playerIds", _playerIds);
	/*
	if(isMyTurn(lastMove)) {
	  setTurnInfoDisplay("My Turn");
	  setTimeCountDown();
	}else {
	  setTurnInfoDisplay("Opp Turn");	
	  clearTimer();
	}
	*/
	sendMessage(message.toString());
  }
  
  private boolean isMyTurn(JSONArray lastMove) {
	List<Operation> move = deserialization.deserializeMove(lastMove);
    if(lastMove.size() == 0) {
      return false;
    }else {
      for(Operation operation : move) {
        if(operation instanceof SetTurn) {
          String playerId = ((SetTurn) operation).getPlayerId();
          if(playerId.equals(myPlayerId)) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  public void sentMyPlayerIdToGame() {
    sendMessage((new JSONObject().put("myPlayerId", new JSONString(myPlayerId))).toString());
  }
  
  public native void sendMessage(String message) /*-{
  	var iframe = $doc.getElementById("board");
    iframe.contentWindow.postMessage(message, "*");
  }-*/;

  public void sendMakeMove(JSONArray move, String gameOverReason) {
	//setTurnInfoDisplay("");
	//clearTimer();
	lastMove = move;
	JSONArray _playerIds = new JSONArray();
	int index = 0;
	for(String playerId : playerIds) {
	  _playerIds.set(index, new JSONString(playerId));
	  index ++;
	}
   	ServerApi.sendMakeMove(
        serverName,
		gameOverReason,
		new JSONString(accessSignature),
		_playerIds,
		matchId,
		move);
  }
  
  public void getMatchInfo() {
    ServerApi.getMatchInfo(
        serverName, 
        myPlayerId, 
        accessSignature);
  }
  
  public void updateGame() {
    ServerApi.getNewState(
        serverName,
        matchId,
        myPlayerId,
        accessSignature);
  }
  
  public void quitGame(String gameOverReason) {
    List<Operation> quitMove = Lists.newArrayList();
    String winPlayerId = null;
    winPlayerId = playerIds.get(0).equals(myPlayerId) ? playerIds.get(1) : playerIds.get(0);
    EndGame operation = new EndGame(winPlayerId);
    quitMove.add(operation);
    JSONArray _quitMove = serialization.serializeMove(quitMove);
    sendMakeMove(_quitMove, gameOverReason);
  }
  
  public native void setTimeCountDown() /*-{
  	var seconds = 60;
  	var span = seconds;
  	var lapse = 0;
  	$wnd.timer = setInterval(function() {
  	  lapse ++;
  	  $("#TimeCountDown").html(span - lapse);
  	  if(lapse == span) {
  	    clearInterval($wnd.timer);
  	    return;
  	  }
  	}, 1000);
  }-*/;
  
  public native void clearTimer() /*-{
  	if($wnd.timer != undefined) {
      clearInterval($wnd.timer);
      $("#TimeCountDown").html("");
  	}
  }-*/;
  
  public native void setTurnInfoDisplay(String turnInfo) /*-{ 
    $("#TurnInfo").html(turnInfo);
  }-*/;

}
