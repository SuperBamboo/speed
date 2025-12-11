app.service('warningToneService',function ($http) {
    this.findByDeviceId = function (deviceId,serverId) {
        return $http.get('../warningTone/findByDeviceId?deviceId='+deviceId+'&serverId='+serverId);
    };
});