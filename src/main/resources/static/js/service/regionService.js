app.service("regionService",function ($http){
    this.findAllRegion = function () {
        return $http.get('../region/findAllRegion');
    };
    this.findAllRegionAndSubRegionAndDevice = function (){
        return $http.get('../region/findAllServerAndRegionAndSubRegionAndDevice');
    };
});