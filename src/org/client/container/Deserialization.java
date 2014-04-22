package org.client.container;

import java.util.List;
import java.util.Map;

import org.client.container.GameApi.AttemptChangeTokens;
import org.client.container.GameApi.Delete;
import org.client.container.GameApi.EndGame;
import org.client.container.GameApi.Operation;
import org.client.container.GameApi.Set;
import org.client.container.GameApi.SetRandomInteger;
import org.client.container.GameApi.SetTurn;
import org.client.container.GameApi.SetVisibility;
import org.client.container.GameApi.Shuffle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class Deserialization {
  private final String TYPE = "type";
  private final String SET = "Set";
  private final String SET_TURN = "SetTurn";
  private final String SET_VISIBILITY = "SetVisibility"; 
  private final String DELETE = "Delete";
  private final String ATTEMPT_CHANGE_TOKENS= "AttemptChangeTokens";
  private final String SHUFFLE = "Shuffle";
  private final String END_GAME = "EndGame";
  private final String SET_RANDOM_INTEGER = "SetRandomInteger";
  private final String KEY = "key";
  private final String VALUE = "value";
  private final String VISIBLE_TO_PLAYER_IDS = "visibleToPlayerIds";
  private final String ALL = "ALL";
  private final String FROM = "from";
  private final String TO = "to";
  private final String PLAYER_ID = "playerId";
  private final String NUMBER_OF_SECONDS_FOR_TURN = "numberOfSecondsForTurn";
  private final String PLAYER_ID_TO_TOKEN_CHANGE = "playerIdToTokenChange";
  private final String PLAYER_ID_TO_NUMBER_OF_TOKENS_IN_POT = "playerIdToNumberOfTokensInPot";
  private final String KEYS = "keys";
  private final String PLAYER_ID_TO_SCORE = "playerIdToScore";
  private final String WINNER_PLAYER_ID = "winnerPlayerId";
	
  public List<Operation> deserializeMove(JSONArray move) {
	int size = move.size();
	List<Operation> resMove = Lists.newArrayList();
    for(int i=0; i<size; i++) {
      JSONObject operationJSON = move.get(i).isObject();
      Operation operation = null;
      if(operationJSON != null) {
        String type = operationJSON.get(TYPE).isString().stringValue();
        switch (type) {
          case SET: {
        	String key = operationJSON.get(KEY).isString().stringValue();
            JSONValue value = operationJSON.get(VALUE);
            JSONValue visibleToPlayerIds = operationJSON.get(VISIBLE_TO_PLAYER_IDS);
            JSONString str;
            JSONArray arr;
            if((str = visibleToPlayerIds.isString()) != null
            	 && str.stringValue().equals(ALL)) {
              //operation = new Set(key, value);
                operation = new Set(key,parseJSONValue(value) );
            }else if((arr = visibleToPlayerIds.isArray()) != null) {
              List<String> visibleToPlayerIdsList = Lists.newArrayList();
              for(int j=0;j<arr.size();j++) {
                visibleToPlayerIdsList.add(arr.get(j).isString().stringValue());
              }
              operation = new Set(key, parseJSONValue(value), visibleToPlayerIdsList);
            }else {
              throw new RuntimeException();
            }
            break;
          }
          case SET_TURN: {
            String playerId = operationJSON.get(PLAYER_ID).isString().stringValue();
            int numberOfSecondsForTurn = 
            		(int) operationJSON.get(NUMBER_OF_SECONDS_FOR_TURN).isNumber().doubleValue();
            operation = new SetTurn(playerId, numberOfSecondsForTurn);
            break;
          }
          case SET_VISIBILITY: {
        	String key = operationJSON.get(KEY).isString().stringValue();
        	JSONValue visibleToPlayerIds = operationJSON.get(VISIBLE_TO_PLAYER_IDS);
        	JSONString str;
        	JSONArray arr;
        	if((str = visibleToPlayerIds.isString()) != null
        		 && str.stringValue().equals(ALL)) {
        	  operation = new SetVisibility(key);
        	}else if((arr = visibleToPlayerIds.isArray()) != null) {
        	  List<String> visibleToPlayerIdsList = Lists.newArrayList();
        	  for(int j=0;j<arr.size();j++) {
        	    visibleToPlayerIdsList.add(arr.get(j).isString().stringValue());
        	  }
        	  operation = new SetVisibility(key, visibleToPlayerIdsList);
        	}else {
        	  throw new RuntimeException();
        	}
        	break;
          }
          case DELETE: {
        	String key = operationJSON.get(KEY).isString().stringValue();
            operation = new Delete(key);
            break;
          }
          case ATTEMPT_CHANGE_TOKENS: {
        	JSONObject playerIdToTokenChangeJSON = 
        			operationJSON.get(PLAYER_ID_TO_TOKEN_CHANGE).isObject();
        	JSONObject playerIdToNumberOfTokensInPotJSON = 
        			operationJSON.get(PLAYER_ID_TO_NUMBER_OF_TOKENS_IN_POT).isObject();
        	Map<String, Integer> playerIdToTokenChange = Maps.newHashMap();
        	Map<String, Integer> playerIdToNumberOfTokensInPot = Maps.newHashMap();
        	java.util.Set<String> keySet1 = 
        			playerIdToTokenChangeJSON.keySet();
        	java.util.Set<String> keySet2 = 
        			playerIdToNumberOfTokensInPotJSON.keySet();
        	for(String key : keySet1) {
        	  int value = (int) playerIdToTokenChangeJSON.get(key).isNumber().doubleValue();
        	  playerIdToTokenChange.put(key, value);
        	}
        	for(String key : keySet2) {
        	  int value = (int) playerIdToNumberOfTokensInPotJSON.get(key).isNumber().doubleValue();
        	  playerIdToNumberOfTokensInPot.put(key, value);
        	}
            operation = new AttemptChangeTokens(
            		playerIdToTokenChange, playerIdToNumberOfTokensInPot); 
            break;
          }
          case SHUFFLE: {
        	List<String> keys = Lists.newArrayList();
        	JSONArray keysJSON = operationJSON.get(KEYS).isArray();
        	for(int j=0;j<keysJSON.size();j++) {
        	  keys.add(keysJSON.get(j).isString().stringValue());
        	}
            operation = new Shuffle(keys);
            break;
          }
          case END_GAME: {
            JSONValue playerIdToScoreJSONValue = operationJSON.get(PLAYER_ID_TO_SCORE); 
            JSONValue winnerPlayerIdJSONValue = operationJSON.get(WINNER_PLAYER_ID);
            if(playerIdToScoreJSONValue != null) {
              JSONObject playerIdToScoreJSONObject = 
            		  playerIdToScoreJSONValue.isObject();
              Map<String, Integer> playerIdToScore = Maps.newHashMap();
              java.util.Set<String> keySet = playerIdToScoreJSONObject.keySet();
              for(String key : keySet) {
            	int value = (int) playerIdToScoreJSONObject.get(key).isNumber().doubleValue();
                playerIdToScore.put(key, value);
              }
              operation = new EndGame(playerIdToScore);
            }else if(winnerPlayerIdJSONValue != null) {
              JSONString winnerPlayerIdJSONNumber = 
            		  winnerPlayerIdJSONValue.isString();
              String winnerPlayerId = winnerPlayerIdJSONNumber.stringValue();
              operation = new EndGame(winnerPlayerId);
            }else {
              throw new RuntimeException();
            }
            break;
          }
          case SET_RANDOM_INTEGER: {
          	String key = operationJSON.get(KEY).isString().stringValue();
          	int from = (int) operationJSON.get(FROM).isNumber().doubleValue();
          	int to = (int) operationJSON.get(TO).isNumber().doubleValue();
          	operation = new SetRandomInteger(key, from, to);
          	break;
          }
          default: {
            throw new RuntimeException();
          }
        }
      }else {
        throw new RuntimeException();
      }
      resMove.add(operation);
    }
    return resMove;
  }
  
  public Map<String, Object> deserializeState(JSONObject state) {
    java.util.Set<String> keySet = state.keySet();
    Map<String, Object> map = Maps.newHashMap();
    JSONValue value = null;
    for(String key : keySet) {
      value = state.get(key);
      map.put(key, parseJSONValue(value));
    }
    return map;
  }
  
  private Object parseJSONValue(JSONValue value) {
    JSONNumber number = null;
    JSONBoolean bool = null;
    JSONString string = null;
    JSONNull nil = null;
    JSONArray array = null;
    JSONObject object = null;
    if((number = value.isNumber()) != null) {
      return (int) number.doubleValue();
    }else if((bool = value.isBoolean()) != null) {
      return (boolean) bool.booleanValue();
    }else if((string = value.isString()) != null) {
      return (String) string.stringValue();
    }else if((nil = value.isNull()) != null) {
      return null;
    }else if((array = value.isArray()) != null) {
      List<Object> res = Lists.newArrayList();
      for(int i=0;i<array.size();i++) {
        res.add(parseJSONValue(array.get(i)));
      }
      return res;
    }else if((object = value.isObject()) != null) {
      Map<String, Object> res = Maps.newHashMap();
      java.util.Set<String> keySet = object.keySet();
      for(String key : keySet) {
        res.put(key, parseJSONValue(object.get(key)));
      }
      return res;
    }else {
      throw new RuntimeException();
    }
  }
}

