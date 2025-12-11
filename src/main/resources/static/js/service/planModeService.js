app.service('planModeService',function ($http) {
    this.findByDeviceId = function (deviceId,serverId) {
        return $http.get('../planMode/findByDeviceId?deviceId='+deviceId+'&serverId='+serverId);
    };
});