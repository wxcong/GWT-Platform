package org.client.container;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

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
  }
  
  private static native void gameLoadAsynNotInvite() /*-{
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
    
    $doc.getElementById("refresh").href = "javascript:window.handleRefreshButtonClick()";
    $doc.getElementById("quit").href = "javascript:window.handleEndButtonClick('Quit')";
    
    $doc.getElementById("game-board").innerHTML = "<iframe id='board' frameborder='no'></iframe>";
    $doc.getElementById("board").width = width;
    $doc.getElementById("board").height = height;
    $doc.getElementById("board").src = address;

    @org.client.container.ContainerEntryPoint::setMyPlayerId(Ljava/lang/String;) (playerId);
    @org.client.container.ContainerEntryPoint::setGameId(Ljava/lang/String;) (gameId);
    @org.client.container.ContainerEntryPoint::setIdsInContainer(Ljava/lang/String;Ljava/lang/String;) (playerId, accessSignature);
    
    @org.client.container.ContainerEntryPoint::invokeEnterQueueAsyn(Ljava/lang/String;) (gameId);
  }-*/;
  
  public static void invokeEnterQueueAsyn(String gameId) {
    setIdsInController(myPlayerId);
    container.setGameId(gameId);
    container.sendEnterQueueAsyn();
  }
  
  private static native void gameLoadAsyn(String oppPlayerId) /*-{
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
    
    $doc.getElementById("game-board").innerHTML = "<iframe id='board' frameborder='no'></iframe>";
    $doc.getElementById("board").width = width;
    $doc.getElementById("board").height = height;
    $doc.getElementById("board").src = address;
     
    $doc.getElementById("refresh").href = "javascript:window.handleRefreshButtonClick()";
    $doc.getElementById("quit").href = "javascript:window.handleEndButtonClick('Quit')";
    
    @org.client.container.ContainerEntryPoint::setMyPlayerId(Ljava/lang/String;) (playerId);
    @org.client.container.ContainerEntryPoint::setGameId(Ljava/lang/String;) (gameId);
    @org.client.container.ContainerEntryPoint::setIdsInContainer(Ljava/lang/String;Ljava/lang/String;) (playerId, accessSignature);
    
    @org.client.container.ContainerEntryPoint::inviteFriend(Ljava/lang/String;) (oppPlayerId);
  }-*/;
  
  public static void refresh() {
//	if(!isControllerGetPlayerId) {
//	setIdsInController(myPlayerId); 
//	isControllerGetPlayerId = true;
//  }
   if(!isContainerGetGameId) {
	container.setGameId(gameId);
	isContainerGetGameId = true;
   }
   if(!isGetMatchInfo) {
	 container.getMatchInfo();
     isGetMatchInfo = true;
   }
   container.updateGame();
  }
  
  public static native void inviteFriend(String oppPlayerId) /*-{
    var oppId = oppPlayerId;
	if(oppId == "" || oppId == undefined || oppId == null) {
	  alert("Please enter oppId");
	  return;
	}
	@org.client.container.ContainerEntryPoint::setPlayerIdsForContainer(Ljava/lang/String;) (oppId);
	@org.client.container.ContainerEntryPoint::invokeInsertNewMatch() ();
  }-*/;
  
  public static void setPlayerIdsForContainer(String oppId) {
    setIdsInController(myPlayerId);
    isControllerGetPlayerId = true;
	container.setGameId(gameId);
	isContainerGetGameId = true;
    container.setPlayerIds(oppId);
  }
  
  public static void invokeInsertNewMatch() {
	 //1 represents invite
    container.sendGameReady(1);
  }
  
    private static native void gameLoad() /*-{
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
      
      $doc.getElementById("quit").href = "javascript:window.handleEndButtonClick('Quit')";
      
      $doc.getElementById("game-board").innerHTML = "<iframe id='board' frameborder='no'></iframe>";
      $doc.getElementById("board").width = width;
      $doc.getElementById("board").height = height;
      $doc.getElementById("board").src = address;
      
      @org.client.container.ContainerEntryPoint::setMyPlayerId(Ljava/lang/String;) (playerId);
      @org.client.container.ContainerEntryPoint::setIdsInContainer(Ljava/lang/String;Ljava/lang/String;) (playerId, accessSignature);
      
      @org.client.container.ContainerEntryPoint::invokeEnterQueue(Ljava/lang/String;) (gameId);
    }-*/;
  
	private static native void exportJSNIFunction() /*-{
	  $wnd.setMyPlayerIdExported = @org.client.container.ContainerEntryPoint::setMyPlayerId(Ljava/lang/String;);
	  $wnd.setGameIdExported = @org.client.container.ContainerEntryPoint::setGameId(Ljava/lang/String;);
	  $wnd.setIdsInContainerExported = @org.client.container.ContainerEntryPoint::setIdsInContainer(Ljava/lang/String;Ljava/lang/String;);
	  $wnd.handleSynAutoModeButtonClick = @org.client.container.ContainerEntryPoint::gameLoad();
	  $wnd.handleAsynInviteModeButtonClick = @org.client.container.ContainerEntryPoint::gameLoadAsyn(Ljava/lang/String;);
	  $wnd.handleAsynAutoModeButtonClick = @org.client.container.ContainerEntryPoint::gameLoadAsynNotInvite();
	  $wnd.handleAEnterButtonClick = @org.client.container.ContainerEntryPoint::invokeEnterQueueAsyn(Ljava/lang/String;);
	  $wnd.handleRefreshButtonClick = @org.client.container.ContainerEntryPoint::refresh();
      $wnd.handleEndButtonClick = @org.client.container.ContainerEntryPoint::invokeEndGame(Ljava/lang/String;);
	}-*/;
	
	private static native void sendMessage(String message) /*-{
	  var iframe = $doc.getElementById("board");
	  iframe.contentWindow.postMessage(message, "*");
	}-*/;
  
	private static void invokeEndGame(String gameOverReason) {
	  final String gameOverReason_ = gameOverReason;
	  String message = new String("If you quit, you will lose this match. Do you want to do this?");
	  final String option1 = "Yes";
	  final String option2 = "No";
	  List<String> options = Lists.newArrayList();
	  options.add(option1);
	  options.add(option2);
	  new PopupChoices(message, options, 
		 new OptionChosen(){
	    @Override
	    public void optionChosen(String option) {
	      if(option.equals(option1)) { 
	        container.quitGame(gameOverReason_);	
	      }else {
	      }
	    }
	  }).center();
	}
	
	static interface OptionChosen {
	  void optionChosen(String option);
    }
	
	static class PopupChoices extends DialogBox {
		  private Button firstChoice;

		  public PopupChoices(String mainText, List<String> options, final OptionChosen optionChosen) {
		    super(false, true);
		    setText(mainText);
		    setAnimationEnabled(true);
		    HorizontalPanel buttons = new HorizontalPanel();
		    for (String option : options) {
		      final String optionF = option;
		      Button btn = new Button(option);
		      if (firstChoice == null) {
		        firstChoice = btn;
		      }
		      btn.addClickHandler(new ClickHandler() {
		        @Override
		        public void onClick(ClickEvent event) {
		          hide();
		          optionChosen.optionChosen(optionF);
		        }
		      });
		      buttons.add(btn);
		    }
		    setWidget(buttons);
		  }
		  @Override
		  public void center() {
		    super.center();
		    firstChoice.setFocus(true);
		  }
		}
	
	private static void invokeCloseSocket() {
	  container.closeSocket();
	}
	
	private static void invokeEnterQueue(String gameId) {
      //setIdsInController(myPlayerId);
	  container.setGameId(gameId);
	  container.sendEnterQueue();
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
