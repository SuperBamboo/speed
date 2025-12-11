app.service('applyService',function ($http) {
    this.findAll = function () {
        return $http.get('../apply/findAll');
    };
    this.updateFlag = function (apply){
        return $http.post('../apply/updateFlag',apply);
    };
    this.findNewApply10Size = function (){
        return $http.get('../apply/findNewApply10Size');
    };
});