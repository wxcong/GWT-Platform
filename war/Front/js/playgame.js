/**
 * Created by Tao on 4/25/14.
 */
function playgame(address, height, width, gameId) {
    var info;
    info = address;
    location.href = "play.html?" + "address=" + address + "&height=" + height + "&width=" + width + "&gameId=" + gameId;
}