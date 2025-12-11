app.controller("loggerController",function ($scope,$controller,loggerService,userService){
    $controller("baseController", {$scope: $scope});
    $scope.searchLogger = {};

    $scope.userList =  [];

    $scope.init = function (){
        userService.findAll().success(function (response){
            response.unshift({username:"全部"});
            $scope.userList = response;
            $scope.searchLogger.username = "全部";
        });
    };

    $scope.initSearchLogger = function (){
        $scope.searchLogger = {username:"全部"};
    };

    $scope.findAll = function (){
        //查询所有
        loggerService.findAll().success(function (response){
            $scope.loggerList = response;
        })
    };

    $scope.search = function (pageNo, pageSize) {
        loggerService.search(pageNo, pageSize, $scope.searchLogger).success(function (response) {
                $scope.list = response.list;
                $scope.paginationConf.totalItems = response.total;
            });
    };

    $scope.exportToExcel = function() {
        loggerService.findAll().success(function (response) {
            if (response != null && response.length > 0) {
                var tableDataWithHeaders = [
                    {
                        ID: 'ID',
                        操作者: '操作者',
                        操作时间: '操作时间',
                        操作描述: '操作描述'
                    }
                ];
                response.forEach(function (logger) {
                    tableDataWithHeaders.push({
                        ID: logger.id,
                        操作者: logger.username,
                        操作时间: logger.time,
                        操作描述: logger.desc
                    });
                });

                var wb = XLSX.utils.book_new();
                var ws = XLSX.utils.json_to_sheet(tableDataWithHeaders, {skipHeader: true});
                XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
                var wbout = XLSX.write(wb, {bookType: 'xlsx', type: 'binary'});

                function s2ab(s) {
                    var buf = new ArrayBuffer(s.length);
                    var view = new Uint8Array(buf);
                    for (var i = 0; i < s.length; i++) view[i] = s.charCodeAt(i) & 0xFF;
                    return buf;
                }

                saveAs(new Blob([s2ab(wbout)], {type: 'application/octet-stream'}), '操作日志.xlsx');

            }
        })

    }
})