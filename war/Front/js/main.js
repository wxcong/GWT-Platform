/**
 * Created by Tao on 4/22/14.
 */
$(document).ready(function() {
    var imageURL = getCookieValue("imageURL");
    var email = getCookieValue("email");
    if(email != "" && imageURL != ""){
        $("a[name=user-name]").html("<span class=\"fa fa-user\"></span>" + email + "<b class=\"caret\"></b>");
        $("img[name=user-image]").attr("src", imageURL);
        $("ul[name=usericon]").hide();
    }
})