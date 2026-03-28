app.service('phaseService',function ($http) {
    this.findByDeviceId = function (deviceId,serverId){
        return $http.get('../phase/findByDeviceId?deviceId='+deviceId+'&serverId='+serverId);
    };
});