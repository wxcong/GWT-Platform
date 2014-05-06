/**
 * Created by aaronwong on 4/19/14.
 */


/*
 The variables below are used for testing
 */

// server address
var server="http://5-dot-smg-server-rl.appspot.com/";
var accessSignature="300cb2705f8b752f41804ba9d9d71028";
var testUserId="5696459148099584";
var testEmail="yv59@nyu.edu";
var testTargetId="5696459148099584";
var testPasswordForLogin="password";


/*
 The variables above are used for testing
 */

$(document).ready(function() {



      var playerId = getCookieValue("userId");
      var email = getCookieValue("email");
      console.log(email);
      var accessSignature = getCookieValue("accessSignature");

      ajaxCallGetUserInfo(playerId,accessSignature);
      ajaxCallGetHistory(playerId,playerId,accessSignature)


    $("#name_submit").on("click",function(event){
        var new_firstname = $("#firstname").val();
        console.log(new_firstname);
        var new_lastname = $("#lastname").val();
        console.log(new_lastname);
        var new_nickname = $("#nickname").val();
        var playerId = getCookieValue("userId");
        //var email = getCookieValue("email");
        var accessSignature = getCookieValue("accessSignature");
        ajaxCallUpdateUserInfo(playerId,accessSignature,new_firstname,new_lastname,new_nickname);


    });

});


function ajaxCallUpdateUserInfo(userId,accessSignature,firstname,lastname,nickname){
    var url=server+"userinfo/"+userId;
//    firstname=arguments[1] ? arguments[1] : "*";
//    lastname=arguments[2] ? arguments[2] : "*";
//    nickname=arguments[3] ? arguments[3] : "*";

    // Create the call input
    var jsonObjForInsert = {"accessSignature": accessSignature,
        "email":"xx@gmail.com",
        "password":"123456",
        'firstname': firstname,
        'lastname': lastname,
        'nickname': nickname,
        "imageURL":"https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQFoEwN8--oHyn7Nlx3j2hOSlCwqYfs3lddvHZWCV5rSrdLzKgmfPRvCL8E"
        };

    var jsonString = JSON.stringify(jsonObjForInsert);
    console.log(jsonString);

    $.ajax({
        url: url,
        dataType: 'json',
        data: jsonString,
        type: "PUT",
        success: function(data, textStatus, jqXHR){
            var insert=JSON.stringify(data);
           // ajaxCallGetUserInfo(userId,accessSignature);
            ajaxCallGetPlayerInfo(userId,userId,accessSignature);
            console.log("Update_SUCCESS:" +insert);
        },
        error: function(jqXHR, textStatus, errorThrown){
            //console.log( + textStatus + " " + errorThrown);
            console.log("Update_ERROR: "+jqXHR.responseText);
        }
    });

}



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

$(document).ready(function() {
    // Test for login a User
    $("#loginEmail").on("click",function(event){
        ajaxCallLoginByEmail(testEmail,testPasswordForLogin);
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

//Login Call
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

function ajaxCallLoginByEmail(email,password){

    var url=server+"user/?email=";
    url+=email;
    url+="&password=";
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

    //var result;

    //console.log(url);

    $.ajax({
        url: url,
        type: "GET",
        success: function(data, textStatus, jqXHR){
            console.log("GetUserInfo_SUCCESS:" +data);
            data = JSON.parse(data);
            $("#profile").attr("src",data["imageURL"]);
            //console.log(data["firstName"]);
            $("#display_nickname").text("Hello! "+data["email"]);
            $("#top_name").text("Hello! "+data["email"]);
            //$("#inbox").append("<div class="+"btn-group col-md-3"+">hello world</div>")

     },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("Login_ERROR: " + textStatus + " " + errorThrown);
            console.log(jqXHR.responseText);
        }
    });
   // console.log("outer"+result);
   // return result;
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
            console.log("GetPlayerInfo_SUCCESS:" +JSON.stringify(data));
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
            data = JSON.parse(data);
            //$("#here_table").append("<div>"+data+"</div>")
            //var count=Object.keys(data).length;
            //var content = "<table>"
            var content;
            for(var key in data){
                console.log(key);
                var row_content=fillInTable(key,data[key]);
                content += '<tr>'  + row_content+'</tr>';
            }
            //content += "</table>"

            $('#here_table').append(content);
            console.log("Login_SUCCESS:" +JSON.stringify(data));

        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("Login_ERROR: " + textStatus + " " + errorThrown);
            console.log(jqXHR.responseText);
        }
    });
}

function fillInTable(key,data){
    console.log(data);
    var content="<td>"+key+"</td>";
    var count=Object.keys(data).length;
    for(var key in data){
        content += '<td>'
        content += data[key];
        content += '</td>'

    }
    return content

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





