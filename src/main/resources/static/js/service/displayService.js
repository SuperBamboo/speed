app.service('displayService',function ($http) {
    this.findByDeviceId = function (deviceId,serverId) {
        return $http.get('../display/findByDeviceId?deviceId='+deviceId+'&serverId='+serverId);
    };
});