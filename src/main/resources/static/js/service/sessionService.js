app.factory('sessionService',function (){
    var sessionId = null;
    return{
        getSessionId: function (){
          return sessionStorage.getItem("sessionId");
        },
        setSessionId: function (value){
            sessionStorage.setItem("sessionId",value);
        },
        clearSession: function (){
          sessionStorage.removeItem("sessionId");
        }
    }
})