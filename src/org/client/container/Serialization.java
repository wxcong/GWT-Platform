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

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class Serialization {
    
    private final String TYPE = "type";
    private final String SET = "Set";
    private final String SET_TURN = "SetTurn";
    private final String SET_VISIBILITY = "SetVisibility"; 
    private final String DELETE = "Delete";
    private final String ATTEMPT_CHANGE_TOKENS= "AttemptChangeTokens";
    private final String SHUFFLE = "Shuffle";
    private final String END_GAME = "EndGame";
    private final String SET_RANDOM_INTEGER = "SetRandomInteger";
    private final String GAME_STATE = "gameState";
    
    public JSONArray serializeMove(List<Operation> move){

        JSONArray res = new JSONArray();        
        int i = 0;
        for (Operation operation: move) {
            
            JSONObject operationJSON = new JSONObject();
            if (operation instanceof Set) {
                Set set = (Set) operation;
                operationJSON.put(TYPE, new JSONString(SET));
                List<Object> tmpList = set.getFieldsNameAndValue();
                getOperationJSONObject(tmpList, operationJSON);
               // res.set(i, operationJSON);
            } else if (operation instanceof SetRandomInteger) {
                SetRandomInteger setRandomInteger = (SetRandomInteger) operation;
                operationJSON.put(TYPE, new JSONString(SET_RANDOM_INTEGER));
                List<Object> tmpList = setRandomInteger.getFieldsNameAndValue();
                getOperationJSONObject(tmpList, operationJSON);
            } else if (operation instanceof SetVisibility) {
                SetVisibility setVisibility = (SetVisibility) operation;
                operationJSON.put(TYPE, new JSONString(SET_VISIBILITY));
                List<Object> tmpList = setVisibility.getFieldsNameAndValue();
                getOperationJSONObject(tmpList, operationJSON);
            } else if (operation instanceof SetTurn) {
                SetTurn setTurn = (SetTurn) operation;
                operationJSON.put(TYPE, new JSONString(SET_TURN));
                List<Object> tmpList = setTurn.getFieldsNameAndValue();
                getOperationJSONObject(tmpList, operationJSON);  
            } else if (operation instanceof Delete) {
                Delete delete = (Delete) operation;
                operationJSON.put(TYPE, new JSONString(DELETE));
                List<Object> tmpList = delete.getFieldsNameAndValue();
                getOperationJSONObject(tmpList, operationJSON);
            } else if (operation instanceof AttemptChangeTokens) {
                AttemptChangeTokens attemptChangeTokens = (AttemptChangeTokens)operation;
                operationJSON.put(TYPE, new JSONString(ATTEMPT_CHANGE_TOKENS));
                List<Object> tmpList = attemptChangeTokens.getFieldsNameAndValue();
                getOperationJSONObject(tmpList, operationJSON);
            } else if (operation instanceof Shuffle) {
                Shuffle shuffle = (Shuffle) operation;
                operationJSON.put(TYPE, new JSONString(SHUFFLE));
                List<Object> tmpList = shuffle.getFieldsNameAndValue();
                getOperationJSONObject(tmpList, operationJSON);
            } else if (operation instanceof EndGame) {
                EndGame endGame = (EndGame) operation;
                operationJSON.put(TYPE, new JSONString(END_GAME));
                List<Object> tmList = endGame.getFieldsNameAndValue();
                getOperationJSONObject(tmList, operationJSON);
            } else {
                throw new IllegalArgumentException(
                        "The operation is not a GameApi type and operation = " + operation);
            }
            res.set(i, operationJSON);
            i++;
        }
        
        return res;
            
        
    }
    
    public JSONObject serializeState(Map<String, Object> state){
        JSONObject res = new JSONObject();
        JSONObject mapObject = new JSONObject();
        convertMapToJSONObject(state, mapObject);
        res.put(GAME_STATE, mapObject); 
        return res;
    }
    

    @SuppressWarnings("unchecked")
    public void getOperationJSONObject(List<Object> operationList, JSONObject operationJSON) {
        for(int i = 0; i< operationList.size();i++)
        {
            String key = (String)operationList.get(i);
            Object value = operationList.get(++i);
            
            if(value instanceof Integer  ){
               operationJSON.put(key, new JSONNumber((Integer)value));
            }else if(value instanceof Double){
               operationJSON.put(key, new JSONNumber((Double)value));
            }else if(value instanceof String){
               operationJSON.put(key, new JSONString((String)value));
            }else if(value instanceof Boolean){
               operationJSON.put(key, JSONBoolean.getInstance((Boolean)value));
            }else if(value instanceof List){
                JSONArray valueList = new JSONArray();
                convertListToJSONArray((List<Object>)value, valueList);
                operationJSON.put(key, valueList);
            }else if (value instanceof Map) {
                JSONObject mapObject = new JSONObject();
                convertMapToJSONObject((Map<String, Object>)value, mapObject);
                operationJSON.put(key, mapObject);   
            }else {
                throw new IllegalArgumentException(
                        "The operation.value is neither a basic type nor a list or map And object=" + value);
           } 
        }
      }
    
    @SuppressWarnings("unchecked")
    static void convertListToJSONArray(List<Object> javaList, JSONArray jsonArray){
        
        int i = 0;
        for (Object object : javaList) {
            if(object instanceof Integer  ){
                jsonArray.set(i, new JSONNumber((Integer)object));
            }else if(object instanceof Double){
                jsonArray.set(i, new JSONNumber((Double)object));
            }else if(object instanceof String){
                jsonArray.set(i, new JSONString((String)object));
            }else if(object instanceof Boolean){
                jsonArray.set(i, JSONBoolean.getInstance((Boolean)object));
            }else if(object instanceof List){
                JSONArray subJSONArry = new JSONArray();
                convertListToJSONArray((List<Object>)object, subJSONArry);
                jsonArray.set(i, subJSONArry);
            }else if(object instanceof Map){
                JSONObject subJsonObject = new JSONObject();
                convertMapToJSONObject((Map<String, Object>)object, subJsonObject);
                jsonArray.set(i, subJsonObject);
            }else {
                throw new IllegalArgumentException("object is not supported by JSON" + object);
            } 
            i++;
        }
    }
    
    @SuppressWarnings("unchecked")
    static void convertMapToJSONObject(Map<String,Object> javaMap, JSONObject jsonObject){
        
          
          for (String key : javaMap.keySet()) {
             Object object= javaMap.get(key);
             
             if(object instanceof Integer  ){
                 jsonObject.put(key, new JSONNumber((Integer)object));
             }else if(object instanceof Double){
                 jsonObject.put(key, new JSONNumber((Double)object));
             }else if(object instanceof String){
                 jsonObject.put(key, new JSONString((String)object));
             }else if(object instanceof Boolean){
                 jsonObject.put(key, JSONBoolean.getInstance((Boolean)object));
             }else if(object instanceof List){
                 JSONArray subJSONArry = new JSONArray();
                 convertListToJSONArray((List<Object>)object, subJSONArry);
                 jsonObject.put(key, subJSONArry);
             }else if(object instanceof Map){
                 JSONObject subJsonObject = new JSONObject();
                 convertMapToJSONObject((Map<String, Object>)object, subJsonObject);
                 jsonObject.put(key, subJsonObject);
             }else {
                 throw new IllegalArgumentException("object is not supported by JSON" + object);
             }    
          }
        }
        
   
}