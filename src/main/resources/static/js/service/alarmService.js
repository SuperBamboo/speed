app.service('alarmService',function ($http) {
    this.findAll = function () {
        return $http.get('../pushAlarm/findAll');
    };
    this.search = function (pageNo,pageSize,searchAlarm) {
        return $http.post("../pushAlarm/search?pageNo="+pageNo+"&pageSize="+pageSize,searchAlarm);
    };
    this.findById = function (deviceId){
        return $http.get('../device/findById?deviceId='+deviceId);
    };
    this.findAllByDeviceId = function (deviceId,serverId){
      return $http.get('../pushAlarm/findAllByDeviceId?deviceId='+deviceId+'&serverId='+serverId);
    };
    this.updateCheck = function (id){
        return $http.get('../pushAlarm/updateCheck?id='+id);
    };
    this.updateCheckAll = function (id){
        return $http.get('../pushAlarm/updateCheckAll');
    };
    this.findNewAlarm10Size = function (){
        return $http.get('../pushAlarm/findNewAlarm10Size');
    };
});