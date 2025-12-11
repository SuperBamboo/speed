app.controller("baseController",function ($scope,$location) {

    //分页控件配置 onchange ：当页码变更后自动触发的方法
    $scope.paginationConf = {
        currentPage : 1,
        totalItems : 10,
        itemsPerPage : 10,
        perPageOptions : [10,20,30,40,50],
        onChange : function () {
            $scope.reloadList();
        }
    };

    $scope.paginationConf1 = {
        currentPage : 1,
        totalItems : 10,
        itemsPerPage : 10,
        perPageOptions : [10,20,30,40,50],
        onChange : function () {
            $scope.reloadList1();
        }
    };


    //刷新列表
    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    };

    $scope.reloadList1 = function () {
        $scope.search1($scope.paginationConf1.currentPage,$scope.paginationConf1.itemsPerPage);
    };

    //用户选择的id集合
    $scope.selectIds=[];

    //勾选时候调用
    $scope.updateSelection = function ($event,id) {
        if($event.target.checked){
            $scope.selectIds.push(id);
        }else {
            //移除集合的元素
            var index = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(index,1);
        }

    };

    $scope.jsonToString = function (jsonString,key) {
        var json = JSON.parse(jsonString);

        var value = "";

        for(var i = 0;i<json.length;i++){
            if(i==0){
                value += json[i][key];
            }else {

                value += ","+json[i][key];
            }
        }

        return value;
    }
});