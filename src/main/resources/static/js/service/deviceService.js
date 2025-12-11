app.service('deviceService',function ($http) {
    this.findAll = function () {
        return $http.get('../device/findAll');
    };
    this.findAllAndFlag = function () {
        return $http.get('../device/findAllAndFlag');
    };
    this.search = function (pageNo,pageSize,searchDevice) {
        return $http.post("../device/search?pageNo="+pageNo+"&pageSize="+pageSize,searchDevice);
    };
    this.findById = function (deviceId,serverId){
        return $http.get('../device/findById?deviceId='+deviceId+'&serverId='+serverId);
    };
    this.findAllDeviceType = function (){
        return $http.get('../device/findAllDeviceType');
    };
});