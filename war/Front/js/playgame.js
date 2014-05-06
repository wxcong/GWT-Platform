/**
 * Created by Tao on 4/25/14.
 */
function playgame(address, height, width, gameId) {
    var info;
    info = address;
    location.href = "play.html?" + "address=" + address + "&height=" + height + "&width=" + width + "&gameId=" + gameId;
}

function checkLogin(){
    var playerId = getCookieValue("userId");
    var accessSignature = getCookieValue("accessSignature");
    if(playerId=="" || accessSignature=="") {
        $('#loginModal').modal();
        return;
    }else{	
        playgame('cheat.html', '500', '600', '4663171621060608');
    }
}