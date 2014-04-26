/**
 * Created by aaronwong on 4/19/14.
 */


/*
 The variables below are used for testing
 */

// server address
var server="http://4-dot-smg-server.appspot.com/";
var accessSignature="ec977b4f6d29fbfa0786900d020b801d";
var testUserId="5696459148099584";
var testTargetId="5696459148099584";
var testPasswordForLogin="password";


/*
 The variables above are used for testing
 */

// Test for inserting a new user
$(document).ready(function() {

    // Test for Inserting a new user
    $("#insert").on("click", function(event) {
        // For test, need to modify every time
        var testEmail="yw1456@gmail.com";
        var testPassword="ahxhc2804";
        ajaxCallInsert(testEmail,testPassword);
    });

});

// Test for login a user
$(document).ready(function() {


    // Test for login a User
    $("#login").on("click",function(event){


        ajaxCallLoginByUserID(testUserId,testPasswordForLogin);
    });
});

// Test for getting user info
$(document).ready(function() {

    $("#gettingUserInfo").on("click",function(event){
       // var testUserId="5696459148099584";

        ajaxCallGetUserInfo(testUserId,accessSignature);

    });
});

// Test for getting playerinfo
$(document).ready(function() {

    $("#getPlayerInfo").on("click",function(event){
       // var testUserId="5696459148099584";

     //   var accessSignature="48f1f35f7c6342192a92a057d56077b9";
        ajaxCallGetPlayerInfo(testUserId,testTargetId,accessSignature);

    });
});

// Test for getting play history
$(document).ready(function() {

    $("#getHistory").on("click",function(event){
        //var testUserId="5696459148099584";
        //var testTargetId="5696459148099584";
       // var accessSignature="48f1f35f7c6342192a92a057d56077b9";
        ajaxCallGetHistory(testUserId,testTargetId,accessSignature);

    });
});

//Test for get all game info
$(document).ready(function() {

    $("#getAllGameInfo").on("click",function(event){
        ajaxCallGetAllGameInfo();
    });
});



// Test for insert a player
function ajaxCallInsert(email,password,firstname,lastname,nickname){

    var url=server+"user";
    firstname=arguments[1] ? arguments[1] : "*";
    lastname=arguments[2] ? arguments[2] : "*";
    nickname=arguments[3] ? arguments[3] : "*";
    
    // Create the call input
    var jsonObjForInsert = {'email': email,
        'password': password,
        'firstname': firstname,
        'lastname': lastname,
        'nickname': nickname};
    
    var jsonString = JSON.stringify(jsonObjForInsert);

    $.ajax({
        url: url,
        dataType: 'json',
        data: jsonString,
        type: "POST",
        success: function(data, textStatus, jqXHR){
            var insert=JSON.stringify(data);
            console.log("Insert_SUCCESS:" +insert);
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("Insert_ERROR: " + textStatus + " " + errorThrown);
        }
    });
}

//Login Call by using userId
function ajaxCallLoginByUserID(userId,password){

    var url=server+"user/";
    url+=userId;
    url+="?password=";
    url+=password;

    console.log(url);
    $.ajax({
        url: url,
        type: "GET",
        success: function(data, textStatus, jqXHR){
            console.log("Login_SUCCESS:" +JSON.stringify(data));
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("Login_ERROR: " + textStatus + " " + errorThrown);
            console.log(jqXHR.responseText);
        }
    });
}

//Login Call by using email
function ajaxCallLoginByUserID(email,password){

    var url=server+"user/";
    url+=email;
    url+="?password=";
    url+=password;

    console.log(url);
    $.ajax({
        url: url,
        type: "GET",
        success: function(data, textStatus, jqXHR){
            console.log("Login_SUCCESS:" +JSON.stringify(data));
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("Login_ERROR: " + textStatus + " " + errorThrown);
            console.log(jqXHR.responseText);
        }
    });
}

//Login Call
function ajaxCallGetUserInfo(userId,accessSignature){

    var url=server+"userinfo/";
    url+=userId;
    url+="?accessSignature=";
    url+=accessSignature;

    console.log(url);
    $.ajax({
        url: url,
        type: "GET",
        success: function(data, textStatus, jqXHR){
            console.log("Login_SUCCESS:" +JSON.stringify(data));
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("Login_ERROR: " + textStatus + " " + errorThrown);
            console.log(jqXHR.responseText);
        }
    });
}

function ajaxCallGetPlayerInfo(playerId,targetId,accessSignature){

    var url=server+"playerInfo?playerId=";
    url+=playerId;
    url+="&targetId=";
    url+=targetId;
    url+="&accessSignature=";
    url+=accessSignature;

    console.log(url);
    $.ajax({
        url: url,
        type: "GET",
        success: function(data, textStatus, jqXHR){
            console.log("Login_SUCCESS:" +JSON.stringify(data));
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("Login_ERROR: " + textStatus + " " + errorThrown);
            console.log(jqXHR.responseText);
        }
    });
}

function ajaxCallGetHistory(playerId,targetId,accessSignature){

    var url=server+"playerAllGame?playerId=";
    url+=playerId;
    url+="&targetId=";
    url+=targetId;
    url+="&accessSignature=";
    url+=accessSignature;

    console.log(url);
    $.ajax({
        url: url,
        type: "GET",
        success: function(data, textStatus, jqXHR){
            console.log("Login_SUCCESS:" +JSON.stringify(data));
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("Login_ERROR: " + textStatus + " " + errorThrown);
            console.log(jqXHR.responseText);
        }
    });
}

function ajaxCallGetAllGameInfo(){

    var url=server+"/gameinfo/all";

    console.log(url);
    $.ajax({
        url: url,
        type: "GET",
        success: function(data, textStatus, jqXHR){
            console.log("Login_SUCCESS:" +JSON.stringify(data));
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("Login_ERROR: " + textStatus + " " + errorThrown);
            console.log(jqXHR.responseText);
        }
    });
}





