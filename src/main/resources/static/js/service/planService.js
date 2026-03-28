app.service('planService',function ($http) {
    this.findByDeviceId = function (deviceId,serverId){
        return $http.get('../plan/findByDeviceId?deviceId='+deviceId+'&serverId='+serverId);
    };
});