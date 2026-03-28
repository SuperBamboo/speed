app// 自定义指令：监听input事件
    .directive('ngInput', ['$timeout', function($timeout) {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function(scope, element, attrs, ngModel) {
                var searchTimer = null;
                var lastValue = '';

                element.on('input', function() {
                    var value = element.val() || '';

                    // 直接检查内容长度，不管是否在输入法状态
                    if(value.length >= 2) {
                        // 避免重复触发相同的搜索
                        if(value === lastValue) {
                            return;
                        }
                        lastValue = value;

                        // 取消之前的定时器
                        if(searchTimer) {
                            $timeout.cancel(searchTimer);
                        }

                        // 设置新的定时器
                        searchTimer = $timeout(function() {
                            scope.$apply(function() {
                                // 调用控制器中的方法
                                scope.onInputChange(value);
                            });
                        }, 300);
                    } else {
                        // 内容少于2个字符，清空搜索结果
                        lastValue = '';
                        if(searchTimer) {
                            $timeout.cancel(searchTimer);
                        }
                        scope.$apply(function() {
                            scope.onInputChange('');
                        });
                    }
                });

                // 清理
                scope.$on('$destroy', function() {
                    element.off('input');
                    if(searchTimer) {
                        $timeout.cancel(searchTimer);
                    }
                });
            }
        };
    }]).controller("alarmController",function ($scope,$controller,$q,$sce,$timeout,regionService,alarmService,regionDeviceTypeService,deviceService){
    $controller("baseController", {$scope: $scope});

    //条件查询
    var isFirst = true;
    $scope.regionList = [];
    $scope.subRegionList = [];
    $scope.searchAlarm = {occurDate:getTodayDate(),alarmDesc:getTodayDate()};
    $scope.searchAlarm1 = {alarmType:-1,occurDate:getTodayDate(),alarmDesc:getTodayDate()};

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
        $scope.searchText = '';
        $scope.searchDeviceByServerIdAndDeviceId = null;
        $scope.searchAlarm = {ofRegionName:"全部",ofSubregionName:"全部",deviceName:"",occurDate:getTodayDate(),alarmDesc:getTodayDate()};
        $scope.selectedServer = null;
        $scope.searchAlarm1 = {
            region: '',
            subregion: '',
            deviceName: '',
            alarmType:-1,
            occurDate: getTodayDate(),
            alarmDesc: getTodayDate()
        };

        // 重置下拉列表为全部数据
        $scope.updateDropdownLists($scope.allData);
    };

    $scope.initData2 = function () {
        $scope.searchText = '';
        $scope.searchDeviceByServerIdAndDeviceId = null;
        $scope.showFields = {
            alarmNo: true,      // 报警编号
            deviceId: true,      // 设备编号
            deviceName: true,   // 设备名称
            region: true,       // 区域
            subregion: true,    // 子区
            deviceModel: true,  // 设备类型/规格型号
            supplier: true,     // 供应商
            longitude: true,    // 经度
            dimension: true,    // 纬度
            occurDateTime: true,    // 发生日期
            alarmDesc: true     // 报警描述
        };
    }

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
                $scope.searchAlarm.ofSubregionName = "全部";
            }else {
                $scope.searchAlarm.ofSubregionName = $scope.searchAlarm1.subregion;
            }
        }else {
            $scope.searchAlarm = {serverId:-1,ofRegionName:"全部",ofSubregionName:"全部",deviceName:"",occurDate:getTodayDate(),alarmDesc:getTodayDate()};
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

    $scope.searchDeviceByServerIdAndDeviceId = null;

    // 导出到 Excel 的函数
    $scope.exportToExcel = function() {
        // 先检查是否有勾选字段
        var hasSelectedField = false;
        for (var key in $scope.showFields) {
            if ($scope.showFields[key]) {
                hasSelectedField = true;
                break;
            }
        }

        if (!hasSelectedField) {
            alert('请至少选择一个要导出的字段');
            return;
        }

        // 获取查询参数
        var searchParams = {
            serverId: $scope.selectedServer ? $scope.selectedServer.id : null,
            region: $scope.searchAlarm1.region,
            subregion: $scope.searchAlarm1.subregion,
            deviceName: $scope.searchAlarm1.deviceName,
            alarmType: $scope.searchAlarm1.alarmType !== '-1' ? $scope.searchAlarm1.alarmType : null,
            startDate: $scope.searchAlarm1.occurDate,
            endDate: $scope.searchAlarm1.alarmDesc  // 注意这里第二个日期字段绑定了alarmDesc，可能需要修改
        };

        if($scope.selectedServer!=null){
            $scope.searchAlarm.serverId = $scope.selectedServer.id;
            if($scope.searchAlarm1.region === ""){
                $scope.searchAlarm.ofRegionName = "全部";
            }else {
                $scope.searchAlarm.ofRegionName = $scope.searchAlarm1.region;
            }
            if( $scope.searchAlarm1.subregion === ""){
                $scope.searchAlarm.ofSubregionName = "全部";
            }else {
                $scope.searchAlarm.ofSubregionName = $scope.searchAlarm1.subregion;
            }
        }else {
            $scope.searchAlarm = {serverId:-1,ofRegionName:"全部",ofSubregionName:"全部",deviceName:"",occurDate:$scope.searchAlarm1.occurDate,alarmDesc:$scope.searchAlarm1.alarmDesc};
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

        if($scope.searchDeviceByServerIdAndDeviceId == null ){
            if($scope.searchText == null || $scope.searchText === ''){
                //按照原方法查询
                //用户没有没有选择设备模糊查询下拉框按照原样


                if(isWithinLastTwoMonths($scope.searchAlarm.occurDate) && isWithinLastTwoMonths($scope.searchAlarm.alarmDesc) && validateDateOrder($scope.searchAlarm.occurDate,$scope.searchAlarm.alarmDesc)){
                    // 根据查询条件获取数据
                    alarmService.search(-1,-1,$scope.searchAlarm).success(function (response) {
                        if (response != null && response.list.length > 0) {
                            // 创建新的工作簿
                            var wb = XLSX.utils.book_new();

                            // 创建空的工作表
                            var ws = XLSX.utils.aoa_to_sheet([]);

                            // 获取选中的字段
                            var selectedFields = [];
                            var fieldKeys = [];

                            // 定义字段映射
                            var fieldMap = {
                                'alarmNo': '报警编号',
                                'deviceId': '设备编号',
                                'deviceName': '设备名称',
                                'region': '区域',
                                'subregion': '子区',
                                'deviceModel': '规格型号',
                                'supplier': '供应商',
                                'longitude': '经度',
                                'dimension': '纬度',
                                'occurDateTime': '发生时间',
                                'alarmDesc': '报警描述'
                            };

                            // 收集选中的字段
                            for (var key in $scope.showFields) {
                                if ($scope.showFields[key] && fieldMap[key]) {
                                    selectedFields.push(fieldMap[key]);
                                    fieldKeys.push(key);
                                }
                            }

                            // 添加表头
                            XLSX.utils.sheet_add_aoa(ws, [selectedFields], {origin: "A1"});

                            // 添加数据行
                            var dataRows = [];
                            response.list.forEach(function (pushAlarm) {
                                var dataRow = [];
                                fieldKeys.forEach(function(key) {
                                    dataRow.push(getAlarmFieldValue(pushAlarm, key));
                                });
                                dataRows.push(dataRow);
                            });

                            // 从A2开始添加数据
                            XLSX.utils.sheet_add_aoa(ws, dataRows, {origin: "A2"});

                            // 设置列宽
                            var wscols = [];
                            for (var i = 0; i < selectedFields.length; i++) {
                                wscols.push({wch: 15});
                            }
                            ws['!cols'] = wscols;

                            // 将工作表添加到工作簿
                            XLSX.utils.book_append_sheet(wb, ws, '报警信息');

                            // 生成文件名
                            var fileName = generateFileName(searchParams,null);

                            // 导出文件
                            var wbout = XLSX.write(wb, {bookType: 'xlsx', type: 'binary'});

                            function s2ab(s) {
                                var buf = new ArrayBuffer(s.length);
                                var view = new Uint8Array(buf);
                                for (var i = 0; i < s.length; i++) view[i] = s.charCodeAt(i) & 0xFF;
                                return buf;
                            }

                            saveAs(new Blob([s2ab(wbout)], {type: 'application/octet-stream'}), fileName);

                        } else {
                            alert('没有符合条件的数据可导出');
                        }
                    }).error(function() {
                        alert('导出失败，请重试');
                    });
                }else {
                    alert("日期输入有误！");
                }

            }else{
                //提示用户需要选择某一项
                alert("请选择需要导出的设备！");
                return;
            }

        }else {
            if(isWithinLastTwoMonths($scope.searchAlarm.occurDate) && isWithinLastTwoMonths($scope.searchAlarm.alarmDesc) && validateDateOrder($scope.searchAlarm.occurDate,$scope.searchAlarm.alarmDesc)){
                // 根据查询条件获取数据
                alarmService.findAllByDeviceIdAndDate($scope.searchDeviceByServerIdAndDeviceId.deviceId,$scope.searchDeviceByServerIdAndDeviceId.serverId,$scope.searchAlarm.occurDate,$scope.searchAlarm.alarmDesc).success(function (response) {
                    if (response != null && response.length > 0) {
                        var deviceName = response[0].deviceName;
                        // 创建新的工作簿
                        var wb = XLSX.utils.book_new();

                        // 创建空的工作表
                        var ws = XLSX.utils.aoa_to_sheet([]);

                        // 获取选中的字段
                        var selectedFields = [];
                        var fieldKeys = [];

                        // 定义字段映射
                        var fieldMap = {
                            'alarmNo': '报警编号',
                            'deviceId': '设备编号',
                            'deviceName': '设备名称',
                            'region': '区域',
                            'subregion': '子区',
                            'deviceModel': '规格型号',
                            'supplier': '供应商',
                            'longitude': '经度',
                            'dimension': '纬度',
                            'occurDateTime': '发生时间',
                            'alarmDesc': '报警描述'
                        };

                        // 收集选中的字段
                        for (var key in $scope.showFields) {
                            if ($scope.showFields[key] && fieldMap[key]) {
                                selectedFields.push(fieldMap[key]);
                                fieldKeys.push(key);
                            }
                        }

                        // 添加表头
                        XLSX.utils.sheet_add_aoa(ws, [selectedFields], {origin: "A1"});

                        // 添加数据行
                        var dataRows = [];
                        response.forEach(function (pushAlarm) {
                            var dataRow = [];
                            fieldKeys.forEach(function(key) {
                                dataRow.push(getAlarmFieldValue(pushAlarm, key));
                            });
                            dataRows.push(dataRow);
                        });

                        // 从A2开始添加数据
                        XLSX.utils.sheet_add_aoa(ws, dataRows, {origin: "A2"});

                        // 设置列宽
                        var wscols = [];
                        for (var i = 0; i < selectedFields.length; i++) {
                            wscols.push({wch: 15});
                        }
                        ws['!cols'] = wscols;

                        // 将工作表添加到工作簿
                        XLSX.utils.book_append_sheet(wb, ws, '报警信息');

                        // 生成文件名
                        var fileName = generateFileName(searchParams,deviceName);

                        // 导出文件
                        var wbout = XLSX.write(wb, {bookType: 'xlsx', type: 'binary'});

                        function s2ab(s) {
                            var buf = new ArrayBuffer(s.length);
                            var view = new Uint8Array(buf);
                            for (var i = 0; i < s.length; i++) view[i] = s.charCodeAt(i) & 0xFF;
                            return buf;
                        }

                        saveAs(new Blob([s2ab(wbout)], {type: 'application/octet-stream'}), fileName);

                    } else {
                        alert('没有符合条件的数据可导出');
                    }
                })
            }else {
                alert("日期输入有误！");
            }

        }

    };

    // 获取报警字段值
    function getAlarmFieldValue(alarm, fieldName) {
        switch(fieldName) {
            case 'alarmNo':
                return alarm.alarmNo;
            case 'deviceId':
                return alarm.deviceId;
            case 'deviceName':
                return alarm.deviceName;
            case 'region':
                return alarm.ofRegionName;
            case 'subregion':
                return alarm.ofSubregionName;
            case 'deviceModel':
                return alarm.deviceModel;
            case 'supplier':
                return alarm.supplier;
            case 'longitude':
                return alarm.longitude;
            case 'dimension':
                return alarm.dimension;
            case 'occurDateTime':  // 合并日期和时间
                return formatDateTime(alarm.occurDate, alarm.occurTime);
            case 'alarmDesc':
                return alarm.alarmDesc;
            default:
                return '';
        }
    }

    // 新增：格式化日期时间函数
    function formatDateTime(dateStr, timeStr) {
        if (!dateStr || !timeStr) {
            return '';
        }

        // 将日期字符串从 YYYYMMDD 转换为 YYYY/MM/DD
        var formattedDate = '';
        if (dateStr.length === 8) {
            formattedDate = dateStr.substring(0, 4) + '/' +
                dateStr.substring(4, 6) + '/' +
                dateStr.substring(6, 8);
        } else {
            formattedDate = dateStr;
        }

        // 将时间字符串从 HHMMSS 转换为 HH:MM:SS
        var formattedTime = '';
        if (timeStr.length === 6) {
            formattedTime = timeStr.substring(0, 2) + ':' +
                timeStr.substring(2, 4) + ':' +
                timeStr.substring(4, 6);
        } else {
            formattedTime = timeStr;
        }

        return formattedDate + ' ' + formattedTime;
    }


    // 生成文件名
    function generateFileName(searchParams,deviceName) {
        var fileName = '报警信息';
        if (searchParams.region) {
            fileName += '_' + searchParams.region;
        }
        if (searchParams.subregion) {
            fileName += '_' + searchParams.subregion;
        }
        if (searchParams.deviceName) {
            fileName += '_' + searchParams.deviceName;
        }
        if(deviceName){
            fileName += '_' +deviceName;
        }
        fileName += '_' + new Date().getTime() + '.xlsx';
        return fileName;
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
            $scope.searchText = '';
            $scope.searchDeviceByServerIdAndDeviceId = null;
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
            $scope.searchText = '';
            $scope.searchDeviceByServerIdAndDeviceId = null;
            //}
            $scope.updateDropdownLists($scope.allData);
        }
    });

    // 监听区域选择变化
    $scope.$watch('searchAlarm1.subregion', function(newVal, oldVal) {
        if (newVal !== oldVal) {

            $scope.searchText = '';
            $scope.searchDeviceByServerIdAndDeviceId = null;
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


    $scope.searchText = '';
    $scope.devices = [];
    $scope.searching = false;
    $scope.showDropdown = false;
    $scope.selectedIndex = -1;

    var blurTimer = null;
    var lastSearchText = '';
    var mouseOverIndex = -1;

    // input事件处理
    $scope.onInputChange = function(value) {
        // 更新ngModel
        $scope.searchText = value;

        // 显示下拉框
        $scope.showDropdown = true;

        // 重置选中索引
        $scope.selectedIndex = -1;
        mouseOverIndex = -1;

        // 检查长度
        if(!value || value.length < 2) {
            $scope.devices = [];
            return;
        }

        // 避免重复搜索相同内容
        if(value === lastSearchText) {
            return;
        }
        lastSearchText = value;

        // 执行搜索
        searchDevicesFromDB(value);
    };

    // 从数据库搜索设备
    function searchDevicesFromDB(keyword) {
        $scope.searching = true;
        let searchDevice = {serverId:-1,ofRegionId:"全部",ofSubRegionId:"全部"};
        if($scope.selectedServer!=null){
            searchDevice.serverId = $scope.selectedServer.id;
            if($scope.searchAlarm1.region === ""){
                searchDevice.ofRegionId = "全部";
            }else {
                searchDevice.ofRegionId = $scope.searchAlarm1.region;
            }
            if( $scope.searchAlarm1.subregion === ""){
                searchDevice.ofSubRegionId = "全部";
            }else {
                searchDevice.ofSubRegionId = $scope.searchAlarm1.subregion;
            }
        }else {
            searchDevice = {serverId:-1,ofRegionId:"全部",ofSubRegionId:"全部"};
        }
        deviceService.findByConditionLike(searchDevice,keyword).success(function (response) {
            // 成功回调
            $scope.devices = response;  // 假设返回的是设备列表

            $scope.searching = false;
            // 如果还有结果，保持下拉框显示
            if($scope.devices.length > 0) {
                $scope.showDropdown = true;
            }
        })


    }

    // 获取焦点时的处理
    $scope.onFocus = function() {
        $scope.showDropdown = true;

        // 如果当前有输入内容，重新搜索
        if($scope.searchText && $scope.searchText.length >= 2) {
            searchDevicesFromDB($scope.searchText);
        }
    };

    // 设置鼠标悬停的选中索引
    $scope.setSelectedIndex = function(index) {
        mouseOverIndex = index;
        $scope.selectedIndex = index;
    };

    // 清除鼠标悬停的选中索引
    $scope.clearSelectedIndex = function() {
        mouseOverIndex = -1;
        $scope.selectedIndex = -1;
    };



    // 选择设备
    $scope.selectDevice = function(device, $event) {
        // 阻止事件冒泡，避免触发blur事件
        if($event) {
            $event.preventDefault();
            $event.stopPropagation();
        }

        // 只显示设备名称
        $scope.searchText = device.deviceName;
        $scope.searchDeviceByServerIdAndDeviceId = {deviceId:device.deviceId,serverId:device.serverId};
        $scope.devices = [];
        $scope.showDropdown = false;
        $scope.selectedIndex = -1;
        mouseOverIndex = -1;
        lastSearchText = device.deviceName;

        // 让输入框失去焦点
        $timeout(function() {
            var input = document.querySelector('input[ng-input]');
            if(input) {
                input.blur();
            }
        }, 10);

        console.log('选中设备:', device);

        // 可以在这里触发其他业务逻辑，比如加载设备详细信息
        $scope.onDeviceSelected(device);
    };

    // 设备选中后的回调（可选）
    $scope.onDeviceSelected = function(device) {
        // 这里可以添加选中设备后的业务逻辑
        // 比如：显示设备详细信息、更新其他表单字段等
        console.log('设备ID:', device.deviceId);
        console.log('设备类型:', device.deviceType);
        console.log('所属区域:', device.ofRegionId);
    };

    // 键盘事件处理
    $scope.onKeyDown = function($event) {
        var keyCode = $event.keyCode;

        // 只处理特殊键
        if(keyCode === 40 || keyCode === 38 || keyCode === 13 || keyCode === 27) {
            $event.preventDefault();

            switch(keyCode) {
                case 40: // 向下
                    if($scope.devices.length > 0) {
                        $scope.selectedIndex = Math.min($scope.selectedIndex + 1, $scope.devices.length - 1);
                        scrollToSelected();
                    }
                    break;
                case 38: // 向上
                    if($scope.devices.length > 0) {
                        $scope.selectedIndex = Math.max($scope.selectedIndex - 1, -1);
                        scrollToSelected();
                    }
                    break;
                case 13: // 回车
                    if($scope.selectedIndex >= 0 && $scope.selectedIndex < $scope.devices.length) {
                        $scope.selectDevice($scope.devices[$scope.selectedIndex], $event);
                    }
                    break;
                case 27: // ESC
                    $scope.showDropdown = false;
                    $scope.selectedIndex = -1;
                    mouseOverIndex = -1;
                    break;
            }
        }
    };

    // 滚动到选中项
    function scrollToSelected() {
        $timeout(function() {
            var selected = document.querySelector('.dropdown-item.active');
            if(selected) {
                selected.scrollIntoView({ block: 'nearest' });
            }
        }, 10);
    }

    // 隐藏下拉框
    $scope.hideDropdown = function() {
        if(blurTimer) {
            $timeout.cancel(blurTimer);
        }
        blurTimer = $timeout(function() {
            $scope.showDropdown = false;
            $scope.selectedIndex = -1;
            mouseOverIndex = -1;
        }, 200);
    };

    // 清理资源
    $scope.$on('$destroy', function() {
        if(blurTimer) $timeout.cancel(blurTimer);
    });
});