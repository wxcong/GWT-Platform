package org.client.container;

import java.util.List;
import java.util.Map;

import org.client.container.GameApi.Operation;
import org.client.container.GameApi.VerifyMoveDone;
import org.client.container.GameApi.Game;
import org.client.container.GameApi.Container;
import org.client.container.GameApi.UpdateUI;
import org.client.container.GameApi.EndGame;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.google.gwt.json.client.*;

/**
 * API:Interface for GameContainer 
 * @author Xiaocong Wang
 *
 */
public class Controller implements Container{
  private String myPlayerId = null;
  private String lastMovePlayerId = "0";
  private final List<String> playerIds = Lists.newArrayList();
  private final List<Map<String, Object>> playersInfo = Lists.newArrayList();
  private Map<String, Object> lastState = Maps.newHashMap();
  private Map<String, Integer> playerIdToNumberOfTokensInPot = Maps.newHashMap();
  
  private Game game = null;
  private int playerNumber;
  
  private final Deserialization deserialization = new Deserialization();
  private final Serialization serialization = new Serialization();
  
  public Controller(Game game, int playerNumber) {
	setListener();
	this.game = game;
	this.playerNumber = playerNumber;
  }
  
  @Override
  public void sendVerifyMoveDone(VerifyMoveDone verifyMoveDone) {
  }
  
  @Override
  public void sendGameReady() {
  }
	
  public native void sendMessage(String message) /*-{
  	$wnd.parent.postMessage(message, "*");
  }-*/;

  public native void setListener() /*-{
  	$wnd.addEventListener("message", function(event) {
      this.@org.client.container.Controller::messageListener(Ljava/lang/String;) (event.data);
    },false);
  }-*/;
  
  @Override
  public void sendMakeMove(List<Operation> operations) {
	JSONObject message = new JSONObject();
	message.put("request", new JSONString("SEND_MAKE_MOVE"));
    message.put("move", serialization.serializeMove(operations));
    for(Operation operation : operations) {
      if(operation instanceof EndGame) {
        message.put("gameOverReason", new JSONString("Over"));
      }
    }
    sendMessage(message.toString());
  }
  
  public void updateUi(UpdateUI updateUi) {
    game.sendUpdateUI(updateUi);
  }

  public void messageListener(String message) {
    JSONObject response = JSONParser.parseStrict(message).isObject();
	  if(response != null) {
		if(response.get("myPlayerId") != null) {
		  myPlayerId = response.get("myPlayerId").isString().stringValue();
		  return;
		}
	    Map<String, Object> state = 
	    		deserialization.deserializeState(response.get("state").isObject());
		List<Operation> lastMove = 
				deserialization.deserializeMove(response.get("lastMove").isArray());
		String lastMovePlayerId = 
				response.get("lastMovePlayerId").isString().stringValue();
		JSONArray _playerIds = response.get("playerIds").isArray();
		for(int i=0;i<_playerIds.size();i++) {
		  playerIds.add(_playerIds.get(i).isString().stringValue());
		}
	    for(int i=0; i<playerIds.size(); i++) {
	       playersInfo.add(ImmutableMap.<String, Object>of("playerId", playerIds.get(i)));
	    }
		UpdateUI updateUi = new UpdateUI(
		    myPlayerId, 
		    playersInfo,
		    state, 
		    lastState,
		    lastMove,
		    lastMovePlayerId,
		    playerIdToNumberOfTokensInPot);
		updateUi(updateUi);
		lastState = state;
	  }else {
	    throw new RuntimeException("JSON Parse Error!");
	  }
  }
}
