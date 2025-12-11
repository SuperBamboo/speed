app.service('portService',function ($http) {
    this.findByDeviceId = function (deviceId,serverId) {
        return $http.get('../port/findByDeviceId?deviceId='+deviceId+'&serverId='+serverId);
    };
});