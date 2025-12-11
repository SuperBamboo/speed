app.service('userInfoService',function ($http) {
    this.getCurrentUser = function () {
        return $http.get('../getCurrentUser');
    };
    this.checkPassword = function (username,password) {
        return $http.get('../checkPassword?username='+username+'&password='+password);
    };
    this.updateUserPassword = function (user) {
        return $http.post('../updateUserPassword',user);
    };
});