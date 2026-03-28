app.service('ctrlModeTypeService',function ($http) {
    this.findByDeviceId = function (deviceId,serverId){
        return $http.get('../ctrlModeType/findByDeviceId?deviceId='+deviceId+'&serverId='+serverId);
    };
});