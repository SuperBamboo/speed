app.service('planParamService',function ($http) {
    this.findByDeviceIdAndPlanModeNo = function (deviceId,planModeNo,serverId) {
        return $http.get('../planParam/findByDeviceIdAndPlanModeNo?deviceId='+deviceId+"&planModeNo="+planModeNo+"&serverId="+serverId);
    };
});