app.service('userService',function ($http) {
    this.findAll = function () {
        return $http.get('../user/findAll');
    };

    this.saveUser = function (user){
        return $http.post("../user/add",user);
    };

    this.findById = function (id){
        return $http.get('../user/findById?id='+id);
    };

    this.deleteById = function (id){
        return $http.get('../user/deleteById?id='+id);
    };

    this.update1User = function (user){
        return $http.post('../user/update',user);
    };

    this.update2User = function (userId,regionDeviceTypeList){
        return $http.post('../user/updateUserRegion?userId='+userId,regionDeviceTypeList);
    };

    this.findByUsername = function (username){
        return $http.get('../user/findByUsername?username='+username);
    }
});