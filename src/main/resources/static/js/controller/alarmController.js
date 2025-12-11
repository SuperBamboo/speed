app.controller("alarmController",function ($scope,$controller,regionService,alarmService,regionDeviceTypeService){
    $controller("baseController", {$scope: $scope});

    //条件查询
    var isFirst = true;
    $scope.regionList = [];
    $scope.subRegionList = [];
    $scope.searchAlarm = {occurDate:getTwoMonthsAgo(),alarmDesc:getTodayDate()};
    $scope.searchAlarm1 = {occurDate:getTwoMonthsAgo(),alarmDesc:getTodayDate()};

    $scope.regionDeviceTypeList = [];
    $scope.allData = []; // 保存原始数据用于过滤
    $scope.selectedServer = null; // 选中的服务器对象

    $scope.updateCheck = function (id){
        alarmService.updateCheck(id).success(function (response){
            $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
        });
    };
    $scope.updateCheckAll = function (id){
        alarmService.updateCheckAll().success(function (response){
            $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
        });
    };

    $scope.initSearchAlarm = function (){
        $scope.searchAlarm = {ofRegionName:"全部",ofSubregionName:"全部",deviceName:"",occurDate:getTwoMonthsAgo(),alarmDesc:getTodayDate()};
        $scope.selectedServer = null;
        $scope.searchAlarm1 = {
            region: '',
            subregion: '',
            deviceName: '',
            occurDate: getTwoMonthsAgo(),
            alarmDesc: getTodayDate()
        };

        // 重置下拉列表为全部数据
        $scope.updateDropdownLists($scope.allData);
    };

    $scope.search = function (pageNo, pageSize) {
        if(isFirst){
            regionService.findAllRegion().success(function (response){
                $scope.regionList = response;
                isFirst = false;
                $scope.searchAlarm.ofRegionName = "全部";
                $scope.updateSubRegion();

            });
            $scope.loadData();
        }

        //此处更换为searchDevice1的条件查询
        if($scope.selectedServer!=null){
            $scope.searchAlarm.serverId = $scope.selectedServer.id;
            if($scope.searchAlarm1.region === ""){
                $scope.searchAlarm.ofRegionName = "全部";
            }else {
                $scope.searchAlarm.ofRegionName = $scope.searchAlarm1.region;
            }
            if( $scope.searchAlarm1.subregion === ""){
                $scope.searchAlarm.ofSubRegionName = "全部";
            }else {
                $scope.searchAlarm.ofSubRegionName = $scope.searchAlarm1.subregion;
            }
        }else {
            $scope.searchAlarm = {serverId:-1,ofRegionName:"全部",ofSubRegionName:"全部",deviceName:"",occurDate:getTwoMonthsAgo(),alarmDesc:getTodayDate()};
        }
        if($scope.searchAlarm1.deviceName !== ""){
            $scope.searchAlarm.deviceName = $scope.searchAlarm1.deviceName;
        }else {
            $scope.searchAlarm.deviceName = "";
        }
        if($scope.searchAlarm1.occurDate !== ""){
            $scope.searchAlarm.occurDate = $scope.searchAlarm1.occurDate;
        }else {
            $scope.searchAlarm.occurDate = "";
        }
        if($scope.searchAlarm1.alarmDesc !== ""){
            $scope.searchAlarm.alarmDesc = $scope.searchAlarm1.alarmDesc;
        }else {
            $scope.searchAlarm.alarmDesc = "";
        }
        $scope.searchAlarm.deviceName = $scope.searchAlarm1.deviceName;
        if(isWithinLastTwoMonths($scope.searchAlarm.occurDate) && isWithinLastTwoMonths($scope.searchAlarm.alarmDesc) && validateDateOrder($scope.searchAlarm.occurDate,$scope.searchAlarm.alarmDesc)){
            alarmService.search(pageNo, pageSize, $scope.searchAlarm).success(function (response) {
                $scope.list = response.list;
                $scope.paginationConf.totalItems = response.total;
            });
        }else {
            alert("日期输入有误！");
        }

    };

    $scope.updateSubRegion = function (){
        if($scope.searchAlarm.ofRegionName === "全部"){
            var allSubregionSet = new Set();
            $scope.regionList.forEach(function (region){
                region.subRegionNameList.forEach(function (subRegion){
                    allSubregionSet.add(subRegion);
                });
            });
            $scope.subRegionList = Array.from(allSubregionSet);
        } else {
            var selectedRegion = $scope.regionList.find(function (region){
                return region.regionName === $scope.searchAlarm.ofRegionName;
            });
            $scope.subRegionList = selectedRegion ? selectedRegion.subRegionNameList : [];
        }
        $scope.searchAlarm.ofSubregionName = $scope.subRegionList.length > 0 ? $scope.subRegionList[0]:null;
    };



    // 导出到 Excel 的函数
    $scope.exportToExcel = function() {
        alarmService.findAll().success(function (response) {
            if (response != null && response.length > 0) {
                var tableDataWithHeaders = [
                    {
                        报警编号: '报警编号',
                        设备名称: '设备名称',
                        区域: '区域',
                        子区: '子区',
                        设备类型: '设备类型',
                        供应商: '供应商',
                        经度: '经度',
                        纬度: '纬度',
                        发生日期: '发生日期',
                        发生时间: '发生时间',
                        报警类型: '报警类型',
                        报警描述: '报警描述'
                    }
                ];
                response.forEach(function (pushAlarm) {
                    tableDataWithHeaders.push({
                        报警编号: pushAlarm.alarmNo,
                        设备名称: pushAlarm.deviceName,
                        区域: pushAlarm.ofRegionName,
                        子区: pushAlarm.ofSubregionName,
                        设备类型: pushAlarm.deviceModel,
                        供应商: pushAlarm.supplier,
                        经度: pushAlarm.longitude,
                        纬度: pushAlarm.dimension,
                        发生日期: pushAlarm.occurDate,
                        发生时间: pushAlarm.occurTime,
                        报警类型: pushAlarm.alarmType,
                        报警描述: pushAlarm.alarmDesc
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

                saveAs(new Blob([s2ab(wbout)], {type: 'application/octet-stream'}), '报警信息.xlsx');

            }
        })

    }

    // 更新所有下拉列表数据-----------------------new code-----------------------------------------------
    $scope.updateDropdownLists = function(dataList) {
        // 服务器列表 - 去重，基于server对象
        $scope.serverList = dataList.reduce((acc, item) => {
            if (item.server && !acc.find(s => s.id === item.server.id)) {
                acc.push(item.server);
            }
            return acc;
        }, []).sort((a, b) => a.id - b.id);

        // 根据当前选择更新区域列表
        if ($scope.selectedServer) {
            var filteredData = dataList.filter(item => item.server && item.server.id === $scope.selectedServer.id);
            $scope.regionList1 = [...new Set(filteredData.map(item => item.region))].sort();
        } else {
            $scope.regionList1 = [...new Set(dataList.map(item => item.region))].sort();
        }

        // 根据当前选择更新子区域列表
        if ($scope.searchAlarm1.region) {
            var filteredData = dataList.filter(item =>
                item.region === $scope.searchAlarm1.region &&
                (!$scope.selectedServer || (item.server && item.server.id === $scope.selectedServer.id))
            );
            $scope.subRegionList1 = [...new Set(filteredData.map(item => item.subregion))].sort();
        } else if ($scope.selectedServer) {
            var filteredData = dataList.filter(item => item.server && item.server.id === $scope.selectedServer.id);
            $scope.subRegionList1 = [...new Set(filteredData.map(item => item.subregion))].sort();
        } else {
            $scope.subRegionList1 = [...new Set(dataList.map(item => item.subregion))].sort();
        }

        // 根据当前选择更新设备类型列表
        //updateDeviceTypeList(dataList);

    };

    // 单独的设备类型更新方法
    function updateDeviceTypeList(dataList) {
        if ($scope.searchAlarm1.subregion) {
            var filteredData = dataList.filter(item =>
                item.subregion === $scope.searchAlarm1.subregion &&
                (!$scope.searchAlarm1.region || item.region === $scope.searchAlarm1.region) &&
                (!$scope.selectedServer || (item.server && item.server.id === $scope.selectedServer.id))
            );
            $scope.deviceTypeList1 = [...new Set(filteredData.map(item => item.deviceType))].sort();
        } else if ($scope.searchAlarm1.region) {
            var filteredData = dataList.filter(item =>
                item.region === $scope.searchAlarm1.region &&
                (!$scope.selectedServer || (item.server && item.server.id === $scope.selectedServer.id))
            );
            $scope.deviceTypeList1 = [...new Set(filteredData.map(item => item.deviceType))].sort();
        } else if ($scope.selectedServer) {
            var filteredData = dataList.filter(item => item.server && item.server.id === $scope.selectedServer.id);
            $scope.deviceTypeList1 = [...new Set(filteredData.map(item => item.deviceType))].sort();
        } else {
            $scope.deviceTypeList1 = [...new Set(dataList.map(item => item.deviceType))].sort();
        }
    }

    // 监听服务器选择变化
    $scope.$watch('selectedServer', function(newVal, oldVal) {
        if (newVal !== oldVal) {
            if(newVal == null || newVal ==='' || newVal === ""){
                $scope.selectedServer = null;
                $scope.searchAlarm.serverId = -1;
            }
            $scope.searchAlarm1.region = '';
            $scope.searchAlarm1.subregion = '';
            //$scope.searchAlarm1.deviceType = ''; // 重置设备类型选择

            $scope.updateDropdownLists($scope.allData);
        }
    });

    // 监听区域选择变化
    $scope.$watch('searchAlarm1.region', function(newVal, oldVal) {
        if (newVal !== oldVal) {
            //if($scope.deviceId == null){
                $scope.searchAlarm1.subregion = '';
                //$scope.searchDevice1.deviceType = ''; // 重置设备类型选择
            //}
            $scope.updateDropdownLists($scope.allData);
        }
    });

    // 加载数据
    $scope.loadData = function() {
        // 实际使用时替换为：
        regionDeviceTypeService.findByCurrentUser().then(function(response) {
            $scope.allData = response.data;
            $scope.regionDeviceTypeList = response.data;
            $scope.processDropdownData($scope.allData);
        });
    };


    // 处理下拉列表数据
    $scope.processDropdownData = function(dataList) {
        // 服务器列表
        $scope.serverList = dataList.reduce((acc, item) => {
            if (item.server && !acc.find(s => s.id === item.server.id)) {
                acc.push(item.server);
            }
            return acc;
        }, []).sort((a, b) => a.id - b.id);

        $scope.regionList1 = [...new Set(dataList.map(item => item.region))].sort();
        $scope.subRegionList1 = [...new Set(dataList.map(item => item.subregion))].sort();
        //$scope.deviceTypeList1 = [...new Set(dataList.map(item => item.deviceType))].sort();
    };


    // 获取当天日期并显示在指定input中
    function getTodayDate() {
        const today = new Date();
        return today.toISOString().split('T')[0];
    }

    // 获取两个月前的日期并显示
    function getTwoMonthsAgo() {
        const today = new Date();
        const twoMonthsAgo = new Date(today);
        twoMonthsAgo.setMonth(today.getMonth() - 2);
        return twoMonthsAgo.toISOString().split('T')[0];
    }

    // 判断日期是否在最近两个月内（包含今天，向前推两个月）
    function isWithinLastTwoMonths(dateStr) {
        const inputDate = new Date(dateStr);
        const today = new Date();
        const twoMonthsAgo = new Date(today);
        twoMonthsAgo.setMonth(today.getMonth() - 2);

        // 清除时间部分，只比较日期
        inputDate.setHours(0, 0, 0, 0);
        today.setHours(0, 0, 0, 0);
        twoMonthsAgo.setHours(0, 0, 0, 0);

        return inputDate >= twoMonthsAgo && inputDate <= today;
    }

    // 判断两个日期输入框，b是否大于a
    function validateDateOrder(dateA, dateB) {
        return dateB >= dateA;
    }
});