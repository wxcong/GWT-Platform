package org.client.container;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;

public class ContainerEntryPoint implements EntryPoint{	
  private static Container container = null;
  private static String myPlayerId = null;
  private static String gameId = null;
  private static boolean isControllerGetPlayerId = false;
  private static boolean isContainerGetGameId = false;
  private static boolean isGetMatchInfo = false;
	
  @Override
  public void onModuleLoad() {
    container = new Container(); 
    exportJSNIFunction();
    gameLoad();
    //gameLoadAsynNotInvite();
    //gameLoadAsyn();
  }
  
  private native void gameLoadAsynNotInvite() /*-{
  	  var href = $wnd.location.href;
      var startAddress = href.indexOf("address");
      var _startAddress = startAddress + 8;

      var startHeight = href.indexOf("&height");
      var _startHeight = startHeight + 8;

      var startWidth = href.indexOf("&width");
      var _startWidth = startWidth + 7;

      var startGameId = href.indexOf("&gameId");
      var _startGameId = startGameId + 8;

      var address;
      var height;
      var width;
      var gameId;

      if(startAddress!=-1) {
        address = href.substring(_startAddress, startHeight);
        address = "js/Games/" + address;
      }

      if(startHeight!=-1) {
        height = href.substring(_startHeight, startWidth);
      }

      if(startWidth!=-1) {
        width = href.substring(_startWidth, startGameId);
      }

      if(startGameId!=-1) {
        gameId = href.substring(_startGameId);
      }else {
        alert("gameId is undefined");
      }
  	
      
    function getCookieValue(name){
      var name = escape(name);
      var allcookies = $doc.cookie;
      name += "=";
      var pos = allcookies.indexOf(name);
      if (pos != -1){
        var start = pos + name.length;
        var end = allcookies.indexOf(";",start);
        if (end == -1) end = allcookies.length;
          var value = allcookies.substring(start,end);
          return unescape(value);                  
        }     
      else return "";
    }
    
    var playerId = getCookieValue("userId");
    var accessSignature = getCookieValue("accessSignature");
    if(playerId=="" || accessSignature=="") {
      alert("Please login first");
      return;
    }
    
    $doc.getElementById("aenter").href = "javascript:window.handleAEnterButtonClick('"+gameId+"')";  
    $doc.getElementById("refresh").href = "javascript:window.handleRefreshButtonClick()";
    $doc.getElementById("quit").href = "javascript:window.handleEndButtonClick('Quit')";

    @org.client.container.ContainerEntryPoint::setMyPlayerId(Ljava/lang/String;) (playerId);
    @org.client.container.ContainerEntryPoint::setGameId(Ljava/lang/String;) (gameId);
    @org.client.container.ContainerEntryPoint::setIdsInContainer(Ljava/lang/String;Ljava/lang/String;) (playerId, accessSignature);
    $doc.getElementById("board").width = width;
    $doc.getElementById("board").height = height;
    $doc.getElementById("board").src = address;
  }-*/;
  
  public static void invokeEnterQueueAsyn(String gameId) {
    setIdsInController(myPlayerId);
    container.setGameId(gameId);
    container.sendEnterQueueAsyn();
  }
  
  private native void gameLoadAsyn() /*-{
  	  var href = $wnd.location.href;
      var startAddress = href.indexOf("address");
      var _startAddress = startAddress + 8;

      var startHeight = href.indexOf("&height");
      var _startHeight = startHeight + 8;

      var startWidth = href.indexOf("&width");
      var _startWidth = startWidth + 7;

      var startGameId = href.indexOf("&gameId");
      var _startGameId = startGameId + 8;

      var address;
      var height;
      var width;
      var gameId;

      if(startAddress!=-1) {
        address = href.substring(_startAddress, startHeight);
        address = "js/Games/" + address;
      }

      if(startHeight!=-1) {
        height = href.substring(_startHeight, startWidth);
      }

      if(startWidth!=-1) {
        width = href.substring(_startWidth, startGameId);
      }

      if(startGameId!=-1) {
        gameId = href.substring(_startGameId);
      }else {
        alert("gameId is undefined");
      }
  	
      
    function getCookieValue(name){
      var name = escape(name);
      var allcookies = $doc.cookie;
      name += "=";
      var pos = allcookies.indexOf(name);
      if (pos != -1){
        var start = pos + name.length;
        var end = allcookies.indexOf(";",start);
        if (end == -1) end = allcookies.length;
          var value = allcookies.substring(start,end);
          return unescape(value);                  
        }     
      else return "";
    }
    
    var playerId = getCookieValue("userId");
    var accessSignature = getCookieValue("accessSignature");
    if(playerId=="" || accessSignature=="") {
      alert("Please login first");
      return;
    }
     
    $doc.getElementById("invite").href = "javascript:window.handleInviteButtonClick()";
    $doc.getElementById("refresh").href = "javascript:window.handleRefreshButtonClick()";
    $doc.getElementById("quit").href = "javascript:window.handleEndButtonClick('Quit')";
    
    @org.client.container.ContainerEntryPoint::setMyPlayerId(Ljava/lang/String;) (playerId);
    @org.client.container.ContainerEntryPoint::setGameId(Ljava/lang/String;) (gameId);
    @org.client.container.ContainerEntryPoint::setIdsInContainer(Ljava/lang/String;Ljava/lang/String;) (playerId, accessSignature);
  	$doc.getElementById("board").width = width;
    $doc.getElementById("board").height = height;
    $doc.getElementById("board").src = address;
  }-*/;
  
  public static void refresh() {
   if(!isControllerGetPlayerId) {
	setIdsInController(myPlayerId); 
	isControllerGetPlayerId = true;
   }
   if(!isContainerGetGameId) {
	container.setGameId(gameId);
	Window.alert("gameId is:" + gameId);
	isContainerGetGameId = true;
   }
   if(!isGetMatchInfo) {
	 Window.alert("To Get the match Info");
	 container.getMatchInfo();
     isGetMatchInfo = true;
   }
   container.updateGame();
  }
  
  public static native void inviteFriend() /*-{
    var oppId = $doc.getElementById("opp-player-id").value; 
    alert("oppId is:" + oppId);
	if(oppId == "" || oppId == undefined || oppId == null) {
	  alert("Please enter oppId");
	  return;
	}
	@org.client.container.ContainerEntryPoint::setPlayerIdsForContainer(Ljava/lang/String;) (oppId);
	alert("invoke insert new match");
	@org.client.container.ContainerEntryPoint::invokeInsertNewMatch() ();
  }-*/;
  
  public static void setPlayerIdsForContainer(String oppId) {
    setIdsInController(myPlayerId);
    isControllerGetPlayerId = true;
	container.setGameId(gameId);
	isContainerGetGameId = true;
    container.setPlayerIds(oppId);
    Window.alert("Ids have been setted");
  }
  
  public static void invokeInsertNewMatch() {
	 //1 represents invite
    container.sendGameReady(1);
  }
  
    private native void gameLoad() /*-{
      var href = $wnd.location.href;
      var startAddress = href.indexOf("address");
      var _startAddress = startAddress + 8;

      var startHeight = href.indexOf("&height");
      var _startHeight = startHeight + 8;

      var startWidth = href.indexOf("&width");
      var _startWidth = startWidth + 7;

      var startGameId = href.indexOf("&gameId");
      var _startGameId = startGameId + 8;

      var address;
      var height;
      var width;
      var gameId;

      if(startAddress!=-1) {
        address = href.substring(_startAddress, startHeight);
        address = "js/Games/" + address;
      }

      if(startHeight!=-1) {
        height = href.substring(_startHeight, startWidth);
      }

      if(startWidth!=-1) {
        width = href.substring(_startWidth, startGameId);
      }

      if(startGameId!=-1) {
        gameId = href.substring(_startGameId);
      }else {
        alert("gameId is undefined");
      }
      
      function getCookieValue(name){
        var name = escape(name);
        var allcookies = $doc.cookie;
        name += "=";
        var pos = allcookies.indexOf(name);
        if (pos != -1){
          var start = pos + name.length;
          var end = allcookies.indexOf(";",start);
          if (end == -1) end = allcookies.length;
          var value = allcookies.substring(start,end);
          return unescape(value);                  
        }     
        else return "";
      }
    
      var playerId = getCookieValue("userId");
      var accessSignature = getCookieValue("accessSignature");
      if(playerId=="" || accessSignature=="") {
        alert("Please login first");
        return;
      }
      
      $doc.getElementById("enter").href = "javascript:window.handleEnterButtonClick('"+gameId+"')";
      alert($doc.getElementById("enter").href);
      
      $doc.getElementById("quit").href = "javascript:window.handleEndButtonClick('Quit')";
      
      @org.client.container.ContainerEntryPoint::setMyPlayerId(Ljava/lang/String;) (playerId);
      @org.client.container.ContainerEntryPoint::setIdsInContainer(Ljava/lang/String;Ljava/lang/String;) (playerId, accessSignature);
      $doc.getElementById("board").width = width;
      $doc.getElementById("board").height = height;
      $doc.getElementById("board").src = address;
    }-*/;
  
	private static native void exportJSNIFunction() /*-{
	  $wnd.handleAEnterButtonClick = @org.client.container.ContainerEntryPoint::invokeEnterQueueAsyn(Ljava/lang/String;);
	  $wnd.handleRefreshButtonClick = @org.client.container.ContainerEntryPoint::refresh();
	  $wnd.handleInviteButtonClick = @org.client.container.ContainerEntryPoint::inviteFriend();
      $wnd.handleEnterButtonClick = @org.client.container.ContainerEntryPoint::invokeEnterQueue(Ljava/lang/String;);
      $wnd.handleEndButtonClick = @org.client.container.ContainerEntryPoint::invokeEndGame(Ljava/lang/String;);
	}-*/;
	
	private static native void sendMessage(String message) /*-{
	  var iframe = $doc.getElementById("board");
	  iframe.contentWindow.postMessage(message, "*");
	}-*/;
  
	private static void invokeEndGame(String gameOverReason) {
	  container.quitGame(gameOverReason);	
	}
	
	private static void invokeCloseSocket() {
	  container.closeSocket();
	}
	
	private static void invokeEnterQueue(String gameId) {
	  Window.alert("%%%%%%%");
      setIdsInController(myPlayerId);
	  container.setGameId(gameId);
	  Window.alert("aaa");
	  container.sendEnterQueue();
	  Window.alert("bbb");
	}
	
	public static void setMyPlayerId(String myPlayerId) {
      ContainerEntryPoint.myPlayerId = myPlayerId; 
	}
	
	public static void setGameId(String gameId) {
      ContainerEntryPoint.gameId = gameId;
	}
	
	public static void setIdsInContainer(String playerId, String accessSignature) {	
	  container.setMyPlayerId(playerId);
	  container.setAccessSignature(accessSignature);
	}
	
	private static native void setIdsInController(String myPlayerId) /*-{
	  var message = {
	    "myPlayerId" : myPlayerId
	  };
	  @org.client.container.ContainerEntryPoint::sendMessage(Ljava/lang/String;) (JSON.stringify(message));
	}-*/;
}
