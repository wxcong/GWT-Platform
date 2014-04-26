/**
 * Created by Tao on 4/22/14.
 */
function ajaxCallGetAllGameInfo(){
    var server="http://4-dot-smg-server.appspot.com/";
    var url=server+"/gameinfo/all";

    console.log(url);
    $.ajax({
        url: url,
        type: "GET",
        success: function(data, textStatus, jqXHR){
            console.log("Login_SUCCESS:" + data);
            dataset = JSON.parse(data);
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("Login_ERROR: " + textStatus + " " + errorThrown);
            console.log(jqXHR.responseText);
        }
    });
}