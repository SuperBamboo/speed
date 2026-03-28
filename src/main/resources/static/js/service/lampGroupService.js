app.service('lampGroupService',function ($http) {
    this.findByDeviceId = function (deviceId,serverId){
        return $http.get('../lampGroup/findByDeviceId?deviceId='+deviceId+'&serverId='+serverId);
    };
});