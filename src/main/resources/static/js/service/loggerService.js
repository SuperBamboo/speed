app.service('loggerService',function ($http) {
    this.findAll = function () {
        return $http.get('../logger/findAll');
    };

    this.search = function (pageNo,pageSize,searchLogger) {
        return $http.post("../logger/search?pageNo="+pageNo+"&pageSize="+pageSize,searchLogger);
    };
});