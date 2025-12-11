app.service('regionDeviceTypeService',function ($http) {
    this.findAll = function () {
        return $http.get('../regionDeviceType/findAll');
    };

    this.findByUserId = function (uid) {
        return $http.get("../regionDeviceType/findByUserId?uid="+uid);
    };

    this.findByCurrentUser = function () {
        return $http.get("../regionDeviceType/findByCurrentUser");
    };
});