app.service('serverService',function ($http) {
    this.findAll = function () {
        return $http.get('../server/findAll');
    };

    this.saveServer = function (server){
        return $http.post("../server/add",server);
    };

    this.deleteById = function (id){
        return $http.get('../server/deleteById?id='+id);
    };

    this.findByServerName = function (serverName){
        return $http.get('../server/findByServerName?serverName='+serverName);
    }
});