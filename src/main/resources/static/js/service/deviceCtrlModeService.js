app.service('deviceCtrlModeService',function ($http) {
    this.findByDeviceId = function (deviceId,serverId) {
        return $http.get('../deviceCtrlMode/findByDeviceId?deviceId='+deviceId+'&serverId='+serverId);
    };
});