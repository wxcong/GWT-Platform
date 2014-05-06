/**
 * Created by Tao on 4/30/14.
 */
function getdeveloperInfo(){
    var gameId = gameIndex;
    ajaxCallGetDeveloperInfo(gameId);
    $("a[name='developerInfo']").popover({placement:'right'});
}

function ajaxCallGetDeveloperInfo(gameId){
    var server="http://5-dot-smg-server-rl.appspot.com/";
    var url = server + "games/" + gameId;
    $.ajax({
        url: url,
        type: "GET",
        success: function(data, textStatus, jqXHR){
            data = JSON.parse(data);
            console.log("Login_SUCCESS:" + JSON.stringify(data));
            if(data["error"] == undefined) {
                developerId=data["developerId"];
                uploadData=data["postDate"];
                //$("#"+gameId).children("a[id='developerInfo']").data('content', "DeveloperId: " + developerId);
                document.getElementById("_" + gameId).dataset['content'] = "DeveloperId:\n" + developerId + "\n" + "Post Date:\n" + uploadData ;
            }else {
                alert(data["error"]);
            }
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("Login_ERROR: " + textStatus + " " + errorThrown);
            console.log(jqXHR.responseText);
        }
    });
}