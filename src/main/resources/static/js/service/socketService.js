app.service('socketService',function ($http) {
    this.sendDeviceStau = function (sessionId,deviceId,serverId) {
        console.log("socketService  "+sessionId);
        return $http.get('../socket/sendDeviceStau?sessionId='+sessionId+'&deviceId='+deviceId+'&serverId='+serverId);
    };

    this.sendSDGK = function (sessionId,deviceCtrlModeSet) {
        console.log("socketService  "+sessionId);
        return $http.post('../socket/sendSDGK?sessionId='+sessionId,deviceCtrlModeSet);
    };
    this.sendCSGK = function (sessionId,devicePlanModeSet) {
        console.log("socketService  "+sessionId);
        return $http.post('../socket/sendCSGK?sessionId='+sessionId,devicePlanModeSet);
    };
    this.sendDeviceRefresh = function () {
        console.log("socketService  发送刷新设备列表指令");
        return $http.get('../socket/sendDeviceRefresh');
    };
    this.sendDeviceByDeviceIdAndServerId = function (deviceId,serverId){
        console.log("socketService  发送刷新服务器："+serverId+" 下设备: "+deviceId+"指令");
        return $http.get('../socket/sendDeviceByDeviceIdAndServerId?deviceId='+deviceId+"&serverId="+serverId);
    }
    //启动socket链接
    this.openSocket = function (server) {
        console.log("socketService  发送启动服务器");
        return $http.post('../socket/openSocket',server);
    }
    //关闭socket链接
    this.closeSocket = function (server) {
        console.log("socketService  发送关闭服务器");
        return $http.post('../socket/closeSocket',server);
    }
});