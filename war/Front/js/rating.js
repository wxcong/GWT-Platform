/**
 * Created by Tao on 4/28/14.
 */
var gameIndex;
function setGameIndex(index) {
    gameIndex = index;
}

function getRating(rating){
    var gameId = gameIndex;
    var playerId = getCookieValue("userId");
    var accessSignature = getCookieValue("accessSignature");
    ajaxCallRating(gameId, playerId, accessSignature, rating);
}

function ajaxCallRating(gameId, playerId, accessSignature, rating){
    var server="http://5-dot-smg-server-rl.appspot.com/";
    var url = server + "gameinfo/rating";
    console.log(url);

    // Create the call input
    var jsonObjForRating = {
        'gameId': gameId,
        'playerId': playerId,
        'accessSignature': accessSignature,
        'rating': rating
    };

    var jsonString = JSON.stringify(jsonObjForRating);
    $.ajax({
        url: url,
        dataType: "json",
        data: jsonString,
        type: "POST",
        success: function(data, textStatus, jqXHR){
            alert(JSON.stringify(data));
            var rating=JSON.stringify(data["rating"]);
            alert(rating);
            rating = Math.round(rating*100)/100
            console.log("Insert_SUCCESS:" + rating);
            $("#gameId").children("li[className='rating']").html(rating);
            deleteCookie("rating", "/");
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("Insert_ERROR: " + textStatus + " " + errorThrown);
        }
    });
}

