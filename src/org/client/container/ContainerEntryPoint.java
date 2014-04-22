package org.client.container;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;

public class ContainerEntryPoint implements EntryPoint{	
  private static Container container = null;
  private static String myPlayerId = null;
	
  @Override
  public void onModuleLoad() {
    container = new Container(); 
    //The followings are the temporary UI Components Just for testing
    exportJSNIFunction();
  }
  
	private native void exportJSNIFunction() /*-{
      $wnd.handleAnchorClick = @org.client.container.ContainerEntryPoint::gameLoad(Ljava/lang/String;Ljava/lang/String;I);
      $wnd.handleEnterButtonClick = @org.client.container.ContainerEntryPoint::invokeEnterQueue(Ljava/lang/String;);
      $wnd.handleEndButtonClick = @org.client.container.ContainerEntryPoint::invokeEndGame(Ljava/lang/String;);
      $wnd.handleCloseButtonClick = @org.client.container.ContainerEntryPoint::invokeCloseSocket();
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
      setIdsInController(myPlayerId);
	  container.setGameId(gameId);
	  container.sendEnterQueue();
	}
	
	private static native void setControlButtons(String gameId) /*-{
	  var enter = $doc.getElementById("enter");
	  var quit = $doc.getElementById("quit");
	  var close = $doc.getElementById("closeSocket");
	  enter.innerHTML = "<button type='button' onclick='window.handleEnterButtonClick(" + gameId  + ")'>enter</button>";
	  quit.innerHTML = "<button type='button' onclick='window.handleEndButtonClick(" + "'Quit'" + ")'>quit</button>";
	  close.innerHTML = "<button type='button' onclick='window.handleCloseButtonClick()'>close</button>";
	}-*/;
	
	private static native void gameLoad(String address, String gameId, int index) /*-{
      function getCookieValue(name){  
        var name = escape(name);  
        var allcookies = document.cookie;          
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
	  var playerId = getCookieValue("playerId");
	  var accessSignature = getCookieValue("accessSignature");
	  if(playerId=="" || accessSignature=="") {
        return;
	  }else {
	  	@org.client.container.ContainerEntryPoint::setMyPlayerId(Ljava/lang/String;) (playerId);
	  	@org.client.container.ContainerEntryPoint::setIdsInContainer(Ljava/lang/String;Ljava/lang/String;) (playerId, accessSignature);
	  }
	  var game = $doc.getElementById("game");
	  game.innerHTML = "<iframe id='board' style='width:600px;height:400px' " + "src=" + address + ">" + "</iframe>";
	  @org.client.container.ContainerEntryPoint::setControlButtons(Ljava/lang/String;) (gameId);
	}-*/;
	
	public static void setMyPlayerId(String myPlayerId) {
      ContainerEntryPoint.myPlayerId = myPlayerId; 
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
