app.service('parameterService',function ($http) {
    this.findByDeviceId = function (deviceId,serverId) {
        return $http.get('../parameter/findByDeviceId?deviceId='+deviceId+'&serverId='+serverId);
    };
});