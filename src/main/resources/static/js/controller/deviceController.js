app.controller('deviceController', function ($scope, $controller, deviceService, sessionService,displayService,parameterService, planParamService,socketService, $location, $timeout,planModeService, alarmService, warningToneService,regionService, regionDeviceTypeService,deviceCtrlModeService, portService) {
    $controller("baseController", {$scope: $scope});

    $scope.sessionId = null;

    $scope.deviceStau = null;

    $scope.sessionId = "";

    //下发取消管控
    $scope.sendCancelGK = function (serverId){
        if($scope.deviceStau!=null){
            if($scope.deviceStau.tempCtrl!=="无" && $scope.deviceStau.tempCtrl!=="未执行"){
                //先下发取消程控
                const deviceControlModeSet = {
                    deviceId: $scope.deviceStau.deviceId,
                    serverId: serverId,
                    value: "0",
                    ctrlNo: "",
                    save: "",
                    auto: "",
                    relay1: "",
                    relay2: "",
                    warningTone: "",
                    playInteval: "",
                    playNumbers: "",
                    holdTime: ""
                };
                socketService.sendSDGK($scope.sessionId,deviceControlModeSet).success(function (response){
                    //取消按钮显示
                    $("#cancelGK").css('display','none');
                });
            }else {
                if($scope.deviceStau.tempPlan!=="无" && $scope.deviceStau.tempPlan!=="未执行"){
                    const devicePlanModeSet = {
                        deviceId: $scope.deviceStau.deviceId,
                        serverId: serverId,
                        value: "0",
                        planNo: "",
                        save: "",
                        auto: "",
                        holdTime: "",
                        intervalTime:"",
                        repetitions:"",
                        sysTime:"",
                        planParamList:[]
                    };
                    socketService.sendCSGK($scope.sessionId,devicePlanModeSet).success(function (response){
                        //取消按钮显示
                        $("#cancelGK").css('display','none');
                    });
                }
            }
        }
    }

    var messageListener = function (e) {
        $timeout(function () {
            switch (e.data.type) {
                case 1://查询设备状态
                    $scope.deviceStau = e.data.data;
                    if(($scope.deviceStau.tempCtrl!== null && $scope.deviceStau.tempCtrl !=="无" && $scope.deviceStau.tempCtrl!=="未执行") || ($scope.deviceStau.tempPlan!==null && $scope.deviceStau.tempPlan!=="无" && $scope.deviceStau.tempPlan!=="未执行")){
                        $("#cancelGK").css('display','flex');
                    }else {
                        $("#cancelGK").css('display','none');
                    }
                    console.log("deviceController :" + e.data.data.toString());
                    break;
                case 2:
                    $scope.alarmList = e.data.data;
                    break;
                default:
                    break;
            }
        })
    }




    $scope.selectedDevice = {deviceId: null};
    //查询参数
    $scope.findParameterByDeviceId = function (deviceId,serverId) {
        //查询选择测设备
        $scope.findDeviceByDeviceId(deviceId,serverId);

        parameterService.findByDeviceId(deviceId,serverId).success(function (response) {
            $timeout(function () {
                $scope.parameterList = response;
            })
        })
    }

    $scope.findDeviceByDeviceId = function (deviceId,serverId) {
        deviceService.findById(deviceId,serverId).success(function (response) {
            $timeout(function () {
                $scope.selectedDevice = response;
            })
        })
    }

    $scope.deviceCtrlModeList = [];
    $scope.planModeList = [];
    $scope.warningToneList = [];
    $scope.portList = [];
    $scope.portList1 = [];
    $scope.planParamList = [];
    $scope.selectedDeviceCtrlModeNo = "";
    $scope.selectedPlanModeNo = "";
    $scope.selectedDeviceCtrlMode = {holdTimeHours:'',holdTimeMinutes:'',holdTimeSeconds:''};
    $scope.selectedPlanMode = {};
    $scope.selectedWarningToneNo = "0";

    const updateOther = function (a) {
        setCheckFalse();
        $scope.selectedDeviceCtrlMode = $scope.deviceCtrlModeList[a - 1];
        $scope.selectedDeviceCtrlModeNo = $scope.deviceCtrlModeList[a - 1].deviceCtrlModeNo;
        $scope.selectedWarningToneNo = $scope.deviceCtrlModeList[a - 1].warningToneNo;
        $scope.selectedDisplayNo = $scope.deviceCtrlModeList[a - 1].displayNo;
        for(var x = 0;x<$scope.displayList.length;x++){
            if($scope.displayList[x].displayNo === $scope.selectedDisplayNo){
                $scope.selectedDisplay = $scope.displayList[x];
            }
        }

        //获取持续时间时分秒
        var time1 = $scope.selectedDeviceCtrlMode.holdTime;
        if(time1 === 0 || time1 ==='0'){
            $scope.selectedDeviceCtrlMode.holdTimeHours = 0;
            $scope.selectedDeviceCtrlMode.holdTimeMinutes = 0;
            $scope.selectedDeviceCtrlMode.holdTimeSeconds = 0;
        }else {
            var time = parseInt(time1, 10); // 转换为十进制整数
            $scope.selectedDeviceCtrlMode.holdTimeSeconds = time % 60;
            $scope.selectedDeviceCtrlMode.holdTimeMinutes = (time / 60) % 60;
            $scope.selectedDeviceCtrlMode.holdTimeHours = (time / 60) / 60;
        }
        //更新显示颜色

        if ($scope.portList != null && $scope.portList.length > 0) {
            var relayStauBinary = $scope.deviceCtrlModeList[a - 1].relayStau;
            var relayStau1Binary = $scope.deviceCtrlModeList[a - 1].relayStau1;
            for (var i = 0; i < $scope.portList.length; i++) {
                if ("1" === $scope.portList[i].portNo) {
                    if (relayStauBinary.charAt(7) === '1') {
                        $scope.portList[i].selected = true;
                    }
                    if (relayStau1Binary.charAt(7) === '1') {
                        $scope.portList1[i].selected = true;
                    }
                } else if ("2" === $scope.portList[i].portNo) {
                    if (relayStauBinary.charAt(6) === '1') {
                        $scope.portList[i].selected = true;
                    }
                    if (relayStau1Binary.charAt(6) === '1') {
                        $scope.portList1[i].selected = true;
                    }
                } else if ("3" === $scope.portList[i].portNo) {
                    if (relayStauBinary.charAt(5) === '1') {
                        $scope.portList[i].selected = true;
                    }
                    if (relayStau1Binary.charAt(5) === '1') {
                        $scope.portList1[i].selected = true;
                    }
                } else if ("4" === $scope.portList[i].portNo) {
                    if (relayStauBinary.charAt(4) === '1') {
                        $scope.portList[i].selected = true;
                    }
                    if (relayStau1Binary.charAt(4) === '1') {
                        $scope.portList1[i].selected = true;
                    }
                } else if ("5" === $scope.portList[i].portNo) {
                    if (relayStauBinary.charAt(3) === '1') {
                        $scope.portList[i].selected = true;
                    }
                    if (relayStau1Binary.charAt(3) === '1') {
                        $scope.portList1[i].selected = true;
                    }
                } else if ("6" === $scope.portList[i].portNo) {
                    if (relayStauBinary.charAt(2) === '1') {
                        $scope.portList[i].selected = true;
                    }
                    if (relayStau1Binary.charAt(2) === '1') {
                        $scope.portList1[i].selected = true;
                    }
                }
            }
        }
    };

    $scope.displayList = [];
    $scope.selectedDisplay = {};
    $scope.selectedDisplayNo = "";

    $scope.findDeviceCtrlModeByDeviceId = function (deviceId,serverId) {
        $scope.findDeviceByDeviceId(deviceId,serverId);
        deviceCtrlModeService.findByDeviceId(deviceId,serverId).success(function (response) {
            $timeout(function () {
                $scope.deviceCtrlModeList = response;
                if ($scope.deviceCtrlModeList != null && $scope.deviceCtrlModeList.length > 0) {
                    $scope.selectedDeviceCtrlMode = $scope.deviceCtrlModeList[0];
                    $scope.selectedDeviceCtrlModeNo = $scope.deviceCtrlModeList[0].deviceCtrlModeNo;
                    $scope.selectedWarningToneNo = $scope.deviceCtrlModeList[0].warningToneNo;
                    $scope.selectedDisplayNo = $scope.deviceCtrlModeList[0].displayNo;
                    $("#no_sdgk").css('display', 'none');
                    $("#have_sdgk").css('display', 'flex');
                    //发送查询portList
                    portService.findByDeviceId(deviceId,$scope.deviceCtrlModeList[0].serverId).success(function (response) {
                        $scope.portList = angular.copy(response);
                        $scope.portList1 = angular.copy(response);
                        if ($scope.portList != null && $scope.portList.length > 0) {
                            var relayStauBinary = $scope.deviceCtrlModeList[0].relayStau;
                            var relayStau1Binary = $scope.deviceCtrlModeList[0].relayStau1;
                            for (var i = 0; i < $scope.portList.length; i++) {
                                if ("1" === $scope.portList[i].portNo) {
                                    if (relayStauBinary.charAt(7) === '1') {
                                        $scope.portList[i].selected = true;
                                    }
                                    if (relayStau1Binary.charAt(7) === '1') {
                                        $scope.portList1[i].selected = true;
                                    }
                                } else if ("2" === $scope.portList[i].portNo) {
                                    if (relayStauBinary.charAt(6) === '1') {
                                        $scope.portList[i].selected = true;
                                    }
                                    if (relayStau1Binary.charAt(6) === '1') {
                                        $scope.portList1[i].selected = true;
                                    }
                                } else if ("3" === $scope.portList[i].portNo) {
                                    if (relayStauBinary.charAt(5) === '1') {
                                        $scope.portList[i].selected = true;
                                    }
                                    if (relayStau1Binary.charAt(5) === '1') {
                                        $scope.portList1[i].selected = true;
                                    }
                                } else if ("4" === $scope.portList[i].portNo) {
                                    if (relayStauBinary.charAt(4) === '1') {
                                        $scope.portList[i].selected = true;
                                    }
                                    if (relayStau1Binary.charAt(4) === '1') {
                                        $scope.portList1[i].selected = true;
                                    }
                                } else if ("5" === $scope.portList[i].portNo) {
                                    if (relayStauBinary.charAt(3) === '1') {
                                        $scope.portList[i].selected = true;
                                    }
                                    if (relayStau1Binary.charAt(3) === '1') {
                                        $scope.portList1[i].selected = true;
                                    }
                                } else if ("6" === $scope.portList[i].portNo) {
                                    if (relayStauBinary.charAt(2) === '1') {
                                        $scope.portList[i].selected = true;
                                    }
                                    if (relayStau1Binary.charAt(2) === '1') {
                                        $scope.portList1[i].selected = true;
                                    }
                                }
                            }
                        }
                        warningToneService.findByDeviceId(deviceId,serverId).success(function (response) {
                            $scope.warningToneList = response;
                        });
                        displayService.findByDeviceId(deviceId,serverId).success(function (response){
                            $scope.displayList = response;
                            for(var x = 0;x<$scope.displayList.length;x++){
                                if($scope.displayList[x].displayNo === $scope.selectedDisplayNo){
                                    $scope.selectedDisplay = $scope.displayList[x];
                                }
                            }
                        });
                    })
                } else {
                    //没有手动管控
                    $("#no_sdgk").css('display', 'flex');
                    $("#have_sdgk").css('display', 'none');

                }
            })
        })
    }

    $scope.needShowPort = function (port) {
        return port.portNo >= 1 && port.portNo <= 6;
    }


    $scope.changeCard = function (n) {
        if (n === 1) {
            $("#card_li_1").addClass('active');
            $("#card1").css('display', 'flex');

            $("#card_li_2").removeClass('active');
            $("#card2").css('display', 'none');
            $("#card_li_3").removeClass('active');
            $("#card3").css('display', 'none');
        }
        if (n === 2) {
            //发送查询设备状态
            window.addEventListener('message', messageListener);
            if ($scope.selectedDevice !== null) {
                $timeout(function () {
                    socketService.sendDeviceStau($scope.sessionId, $scope.selectedDevice.deviceId,$scope.selectedDevice.serverId).success(function (response) {

                    });
                })
            }
            $("#card_li_2").addClass('active');
            $("#card2").css('display', 'flex');

            $("#card_li_1").removeClass('active');
            $("#card1").css('display', 'none');
            $("#card_li_3").removeClass('active');
            $("#card3").css('display', 'none');
        }
        if (n === 3) {
            $("#card_li_3").addClass('active');
            $("#card3").css('display', 'flex');

            $("#card_li_1").removeClass('active');
            $("#card1").css('display', 'none');
            $("#card_li_2").removeClass('active');
            $("#card2").css('display', 'none');

            $scope.search1(1, 10);
        }
    }

    $scope.changeGKCard = function (n) {
        if (n === 1) {
            $("#card_sdgk").addClass('active');
            $("#card1_sdgk").css('display', 'flex');

            $("#card_csgk").removeClass('active');
            $("#card1_csgk").css('display', 'none');
        }
        if (n === 2) {
            //查询
            if($scope.selectedDevice!=null){
                planModeService.findByDeviceId($scope.selectedDevice.deviceId,$scope.selectedDevice.serverId).success(function (response){
                    $scope.planModeList = response;
                    if($scope.planModeList!=null && $scope.planModeList.length>0){
                        $scope.selectedPlanMode = $scope.planModeList[0];
                        $scope.selectedPlanModeNo = $scope.planModeList[0].planModeNo;
                        planParamService.findByDeviceIdAndPlanModeNo($scope.selectedDevice.deviceId,$scope.selectedPlanModeNo,$scope.selectedDevice.serverId).success(function (response){
                           $scope.planParamList = response;
                        });
                        $("#no_csgk").css('display', 'none');
                        $("#have_csgk").css('display', 'flex');
                    }else {
                        $("#no_csgk").css('display', 'flex');
                        $("#have_csgk").css('display', 'none');
                    }
                })
            }
            $("#card_csgk").addClass('active');
            $("#card1_csgk").css('display', 'flex');

            $("#card_sdgk").removeClass('active');
            $("#card1_sdgk").css('display', 'none');
        }
    }


    $scope.searchDevice = {};
    $scope.searchDevice1 = {};
    $scope.regionList = [];
    var isFirst = true;

    $scope.regionDeviceTypeList = [];
    $scope.allData = []; // 保存原始数据用于过滤
    $scope.selectedServer = null; // 选中的服务器对象


    $scope.initSearchDevice = function (){
      $scope.searchDevice = {serverId:-1,ofRegionId:"全部",ofSubRegionId:"全部",deviceType:"全部",deviceName:""};
        $scope.selectedServer = null;
        $scope.searchDevice1 = {
            region: '',
            subregion: '',
            deviceType: '',
            deviceName: ''
        };
        // 重置下拉列表为全部数据
        $scope.updateDropdownLists($scope.allData);
    };
    //条件查询
    $scope.search = function (pageNo, pageSize) {
        if(isFirst){
            //获取共享数据
            $scope.sessionId = sessionService.getSessionId();

            //regionService.findAllRegion().success(function (response){
                //$scope.regionList = response;
                isFirst = false;
                $scope.searchDevice.ofRegionId = "全部";
                $scope.searchDevice.deviceType = "全部";
               // $scope.updateSubRegion();

            //});

            //deviceService.findAllDeviceType().success(function (response){
                //$scope.deviceTypeList = response;
            //});

            //$scope.initSearchDevice();
            $scope.loadData();
            /*regionDeviceTypeService.findByCurrentUser().success(function (response) {
                $scope.regionDeviceTypeList = response;
            })*/

        }
        //此处更换为searchDevice1的条件查询
        if($scope.selectedServer!=null){
            $scope.searchDevice.serverId = $scope.selectedServer.id;
            if($scope.searchDevice1.region === ""){
                $scope.searchDevice.ofRegionId = "全部";
            }else {
                $scope.searchDevice.ofRegionId = $scope.searchDevice1.region;
            }
            if( $scope.searchDevice1.subregion === ""){
                $scope.searchDevice.ofSubRegionId = "全部";
            }else {
                $scope.searchDevice.ofSubRegionId = $scope.searchDevice1.subregion;
            }
            if($scope.searchDevice1.deviceType === ""){
                $scope.searchDevice.deviceType = "全部";
            }else {
                $scope.searchDevice.deviceType = $scope.searchDevice1.deviceType;
            }
            $scope.searchDevice.deviceName = $scope.searchDevice1.deviceName;
        }else {
            $scope.searchDevice = {serverId:-1,ofRegionId:"全部",ofSubRegionId:"全部",deviceType:"全部",deviceName:""};
        }

        deviceService.search(pageNo, pageSize, $scope.searchDevice).success(function (response) {
            $timeout(function () {
                $scope.list = response.list;
                $scope.paginationConf.totalItems = response.total;
            })
        });

    }
    $scope.updateSubRegion = function (){
        if($scope.searchDevice.ofRegionId === "全部"){
            var allSubregionSet = new Set();
            $scope.regionList.forEach(function (region){
                region.subRegionNameList.forEach(function (subRegion){
                    allSubregionSet.add(subRegion);
                });
            });
            $scope.subRegionList = Array.from(allSubregionSet);
        } else {
            var selectedRegion = $scope.regionList.find(function (region){
                return region.regionName === $scope.searchDevice.ofRegionId;
            });
            $scope.subRegionList = selectedRegion ? selectedRegion.subRegionNameList : [];
        }
        $scope.searchDevice.ofSubRegionId = $scope.subRegionList.length > 0 ? $scope.subRegionList[0]:null;
    };

    $scope.searchAlarm = {};
    $scope.alarmList = [];
    //条件查询
    $scope.search1 = function (pageNo, pageSize) {
        if ($scope.selectedDevice.deviceId != null) {
            $scope.searchAlarm.deviceId = $scope.selectedDevice.deviceId;
            $scope.searchAlarm.serverId = $scope.selectedDevice.serverId;
            alarmService.search(pageNo, pageSize, $scope.searchAlarm).success(function (response) {
                $timeout(function () {
                    $scope.alarmList = response.list;
                    $scope.paginationConf1.totalItems = response.total;
                })

            });
        }
    }

    $scope.initData = function () {
        //关闭弹窗时初始化变量
        $scope.selectedDevice = null;
        $scope.deviceStau = null;
        $scope.changeCard(1);
        window.removeEventListener('message', messageListener);
    }
    $scope.initData1 = function () {
        //关闭弹窗时初始化变量
        $scope.selectedDevice = null;
        $scope.entity = {save: "false", auto: "false"};
        $scope.changeGKCard(1);
    }

    $scope.initWatch= function (){
        $scope.selectedDeviceCtrlModeNo = '';
        $scope.selectedPlanModeNo = '';
    }

    //监视
    $scope.$watch('selectedDeviceCtrlModeNo', function (newValue, oldValue) {
        //console.log("newValue：" + newValue);
        if (newValue != null && '' !== newValue && "" !== newValue) {
            updateOther(newValue);
            console.log("updateOther执行了。。。")
        }
    });

    $scope.$watch('selectedPlanModeNo', function (newValue, oldValue) {
        //console.log("selectedPlanModeNo newValue：" + newValue);
        if (newValue != null && ''!==newValue && ""!==newValue) {
            $scope.selectedPlanMode = $scope.planModeList[newValue-1];
            $scope.selectedPlanModeNo = $scope.planModeList[newValue-1].planModeNo;
            //获取持续时间时分秒
            var time1 = $scope.selectedPlanMode.holdTime;
            if(time1 === 0 || time1 ==='0'){
                $scope.selectedPlanMode.holdTimeHours = 0;
                $scope.selectedPlanMode.holdTimeMinutes = 0;
                $scope.selectedPlanMode.holdTimeSeconds = 0;
            }else {
                var time = parseInt(time1, 10); // 转换为十进制整数
                $scope.selectedPlanMode.holdTimeSeconds = time % 60;
                $scope.selectedPlanMode.holdTimeMinutes = (time / 60) % 60;
                $scope.selectedPlanMode.holdTimeHours = (time / 60) / 60;
            }

            //获取间隔时间时分秒
            var time2 = $scope.selectedPlanMode.intervalTime;
            if(time2 === 0 || time2 ==='0'){
                $scope.selectedPlanMode.intervalTimeHours = 0;
                $scope.selectedPlanMode.intervalTimeMinutes = 0;
                $scope.selectedPlanMode.intervalTimeSeconds = 0;
            }else {
                var time_2 = parseInt(time2, 10); // 转换为十进制整数
                $scope.selectedPlanMode.intervalTimeSeconds = time_2 % 60;
                $scope.selectedPlanMode.intervalTimeMinutes = (time_2 / 60) % 60;
                $scope.selectedPlanMode.intervalTimeHours = (time_2 / 60) / 60;
            }
            planParamService.findByDeviceIdAndPlanModeNo($scope.selectedDevice.deviceId,$scope.selectedPlanModeNo,$scope.selectedDevice.serverId).success(function (response){
                $scope.planParamList = response;
            });
        }
        //循环positionList 找到对应的经纬度

    });

    //清空所有的复选框
    const setCheckFalse = function () {
        for (var i = 0; i < $scope.portList.length; i++) {
            $scope.portList[i].selected = false;
        }
        for (var i = 0; i < $scope.portList1.length; i++) {
            $scope.portList1[i].selected = false;
        }
    }

    $scope.entity = {save: "false", auto: "false"};
    $scope.entity1 = {save: "false", auto: "false"};

    $scope.errorMsg = {flag:false,message:''};
    $scope.errorMsg1 = {flag:false,message:''};

    //下发手动管控
    $scope.sendSDGK = function (serverId) {

        const deviceControlModeSet = {
            deviceId: "",
            serverId: serverId,
            value: "1",
            ctrlNo: "",
            save: "",
            auto: "",
            relay1: "",
            relay2: "",
            displayNo: "",
            warningTone: "",
            volume: "",
            playInteval: "",
            playNumbers: "",
            holdTime: ""
        }
        deviceControlModeSet.deviceId = $scope.selectedDevice.deviceId;
        deviceControlModeSet.serverId = $scope.selectedDevice.serverId;
        deviceControlModeSet.value = "1";
        deviceControlModeSet.ctrlNo = $scope.selectedDeviceCtrlModeNo;
        if ($scope.entity.save == "false") {
            deviceControlModeSet.save = "0";
        } else {
            deviceControlModeSet.save = "1";
        }
        if ($scope.entity.auto == "false") {
            deviceControlModeSet.auto = "0";
        } else {
            deviceControlModeSet.auto = "1";
        }

        if($scope.selectedDeviceCtrlMode.relayStauValidity === '1'){
            const relayStau = $scope.selectedDeviceCtrlMode.relayStau;
            let relayStauBinary = relayStau.substring(0, 2);
            for (let i = 6; i > 0; i--) {
                for (let j = $scope.portList.length - 1; j >= 0; j--) {
                    if ($scope.portList[j].portNo == i) {
                        if ($scope.portList[j].selected == true) {
                            relayStauBinary = relayStauBinary + "1";
                        } else {
                            relayStauBinary = relayStauBinary + "0";
                        }
                    }
                }
                if (i + relayStauBinary.length !== 9) {
                    //目标端口不存在从原始数据上抽取
                    relayStauBinary = relayStauBinary + $scope.selectedDeviceCtrlMode.relayStau.charAt(8 - i);
                }
            }
            deviceControlModeSet.relay1 = parseInt(relayStauBinary, 2).toString();
        }


        if($scope.selectedDeviceCtrlMode.relayStau1Validity === '1' ){
            const relayStau1 = $scope.selectedDeviceCtrlMode.relayStau1;
            let relayStau1Binary = relayStau1.substring(0, 2);
            for (let i = 6; i > 0; i--) {
                for (let j = $scope.portList1.length - 1; j >= 0; j--) {
                    if ($scope.portList1[j].portNo == i) {
                        if ($scope.portList1[j].selected == true) {
                            relayStau1Binary = relayStau1Binary + "1";
                        } else {
                            relayStau1Binary = relayStau1Binary + "0";
                        }
                    }
                }
                if (i + relayStau1Binary.length !== 9) {
                    //目标端口不存在从原始数据上抽取
                    relayStau1Binary = relayStau1Binary + $scope.selectedDeviceCtrlMode.relayStau1.charAt(8 - i);
                }
            }
            deviceControlModeSet.relay2 = parseInt(relayStau1Binary, 2).toString();
        }
        if($scope.selectedDeviceCtrlMode.displayNoValidity === '1'){
            deviceControlModeSet.displayNo = $scope.selectedDisplayNo;
        }
        if($scope.selectedDeviceCtrlMode.warningToneNoValidity === '1'){
            deviceControlModeSet.warningTone = $scope.selectedWarningToneNo;
        }
        if($scope.selectedDeviceCtrlMode.volumeValidity === '1'){
            deviceControlModeSet.volume = $scope.selectedDeviceCtrlMode.volume;
        }
        if($scope.selectedDeviceCtrlMode.playIntevalValidity === '1'){
            let regex = /^(1?[0-9]|2[0-5])$/;
            // 测试字符串
            if(regex.test($scope.selectedDeviceCtrlMode.playInteval)){
                deviceControlModeSet.playInteval = $scope.selectedDeviceCtrlMode.playInteval;
                $scope.initErrorMsg();
            }else {
                $scope.errorMsg.flag = true;
                $scope.errorMsg.message = '语音播报间隔1-25';
                return;
            }
        }
        if($scope.selectedDeviceCtrlMode.playNumbersValidity === '1'){
            // 正则表达式
            let regex = /^(0|[1-9]\d{0,2})$/;
            // 测试字符串
            if(regex.test($scope.selectedDeviceCtrlMode.playNumbers)){
                $scope.initErrorMsg();
                deviceControlModeSet.playNumbers = $scope.selectedDeviceCtrlMode.playNumbers;
            }else {
                $scope.errorMsg.flag = true;
                $scope.errorMsg.message = '语音播报次数小于等于999';
                return;
            }
        }
        if($scope.selectedDeviceCtrlMode.holdTimeValidity === '1'){
            // 正则表达式
            var regex = /^(0|[1-9]|1\d|2[0-3])$/;   //[0-23]
            var regex1 = /^[0-5]?[0-9]$/;   //[0-59]
            // 测试字符串
            if(regex.test($scope.selectedDeviceCtrlMode.holdTimeHours) && regex1.test($scope.selectedDeviceCtrlMode.holdTimeMinutes) && regex1.test($scope.selectedDeviceCtrlMode.holdTimeSeconds)){
                var a = (+$scope.selectedDeviceCtrlMode.holdTimeHours) * 3600 + (+$scope.selectedDeviceCtrlMode.holdTimeMinutes) * 60 + (+$scope.selectedDeviceCtrlMode.holdTimeSeconds);
                if(86400<a){
                    deviceControlModeSet.holdTime = '86400';
                }else {
                    deviceControlModeSet.holdTime = a.toString();
                }
                $scope.initErrorMsg();
            }else {
                $scope.errorMsg.flag = true;
                $scope.errorMsg.message = '持续时间输入不合法';
                return;
            }

        }

        //alert(deviceControlModeSet.holdTime);
        socketService.sendSDGK($scope.sessionId,deviceControlModeSet).success(function (response){
            $scope.initErrorMsg();
            $scope.initWatch();
            $("#editModal1").modal('hide');
        });

    };

    $scope.initErrorMsg = function (){
        $scope.errorMsg.flag = false;
        $scope.errorMsg.message = '';
    };

    $scope.initErrorMsg1 = function (){
        $scope.errorMsg1.flag = false;
        $scope.errorMsg1.message = '';
    };

    //发送程式管控
    $scope.sendCSGK = function (serverId){

        //合法性校验
        // 正则表达式
        var regex = /^(0|[1-9]|1\d|2[0-3])$/;   //[0-23]
        var regex1 = /^[0-5]?[0-9]$/;   //[0-59]
        // 测试字符串
        if(!regex.test($scope.selectedPlanMode.holdTimeHours) || !regex1.test($scope.selectedPlanMode.holdTimeMinutes) || !regex1.test($scope.selectedPlanMode.holdTimeSeconds)){
            $scope.errorMsg1.flag = true;
            $scope.errorMsg1.message = '持续时间输入不合法';
            return;
        }
        if(!regex.test($scope.selectedPlanMode.intervalTimeHours) || !regex1.test($scope.selectedPlanMode.intervalTimeMinutes) || !regex1.test($scope.selectedPlanMode.intervalTimeSeconds)){
            $scope.errorMsg1.flag = true;
            $scope.errorMsg1.message = '间隔时间输入不合法';
            return;
        }
        let regex2 = /^(0?[1-9]|[1-9][0-9]{1,2})$/;  //[1,999]
        // 测试字符串
        if(!regex2.test($scope.selectedPlanMode.repetitions)){
            $scope.errorMsg1.flag = true;
            $scope.errorMsg1.message = '重复次数需大于0';
            return;
        }
        let regex3 = /^\d+$/;   //正整数包括0
        if(!regex3.test($scope.selectedPlanMode.sysTime)){
            $scope.errorMsg1.flag = true;
            $scope.errorMsg1.message = '同步参数错误';
            return;
        }
        const devicePlanModeSet = {
            deviceId: "",
            serverId: serverId,
            value: "1",
            planNo: "",
            save: "",
            auto: "",
            holdTime: "",
            intervalTime:"",
            repetitions:"",
            sysTime:"",
            planParamList:[]
        };
        devicePlanModeSet.deviceId = $scope.selectedDevice.deviceId;
        devicePlanModeSet.serverId = $scope.selectedDevice.serverId;
        devicePlanModeSet.value = "1";
        devicePlanModeSet.planNo = $scope.selectedPlanModeNo;
        if ($scope.entity1.save == "false") {
            devicePlanModeSet.save = "0";
        } else {
            devicePlanModeSet.save = "1";
        }
        if ($scope.entity1.auto == "false") {
            devicePlanModeSet.auto = "0";
        } else {
            devicePlanModeSet.auto = "1";
        }
        //devicePlanModeSet.holdTime = $scope.selectedPlanMode.holdTime;
        var a = (+$scope.selectedPlanMode.holdTimeHours) * 3600 + (+$scope.selectedPlanMode.holdTimeMinutes) * 60 + (+$scope.selectedPlanMode.holdTimeSeconds);
        if(86400<a){
            devicePlanModeSet.holdTime = '86400';
        }else {
            devicePlanModeSet.holdTime = a.toString();
        }

        //devicePlanModeSet.intervalTime = $scope.selectedPlanMode.intervalTime;
        var b = (+$scope.selectedPlanMode.intervalTimeHours) * 3600 + (+$scope.selectedPlanMode.intervalTimeMinutes) * 60 + (+$scope.selectedPlanMode.intervalTimeSeconds);
        if(86400<b){
            devicePlanModeSet.intervalTime = '86400';
        }else {
            devicePlanModeSet.intervalTime = b.toString();
        }
        devicePlanModeSet.repetitions = $scope.selectedPlanMode.repetitions;
        if("1" === devicePlanModeSet.repetitions || '1' === devicePlanModeSet.repetitions || 1 === devicePlanModeSet.repetitions){
            //重复次数为1的时候持续时间设置为0
            devicePlanModeSet.intervalTime = '0';
        }else {
            if (b<=a){
                $scope.errorMsg1.flag = true;
                $scope.errorMsg1.message = '间隔时间需大于持续时间';
                return;
            }
        }

        devicePlanModeSet.sysTime = $scope.selectedPlanMode.sysTime;
        for (var i = 0;i<$scope.planParamList.length;i++){
            if($scope.planParamList[i].defaultValue === '' || $scope.planParamList[i].defaultValue === ""){
                $scope.errorMsg1.flag = true;
                $scope.errorMsg1.message = $scope.planParamList[i].planParamNo +" "+ $scope.planParamList[i].planParamName+' 不能为空';
                return;
            }else {
                devicePlanModeSet.planParamList.push({planParamNo:$scope.planParamList[i].planParamNo,defaultValue:$scope.planParamList[i].defaultValue});
            }
        }
        //至此校验完全结束
        socketService.sendCSGK($scope.sessionId,devicePlanModeSet).success(function (response){
            $scope.initData1();
            $scope.initErrorMsg1();
            $scope.initWatch();
            $("#editModal1").modal('hide');
        });

    };

    //发送刷新设备列表
    $scope.sendDeviceRefresh = function (){
        const isConfirmed = confirm('你确定要刷新设备列表吗？');
        // 根据用户选择执行操作
        if (isConfirmed) {
            // 在这里写你的下一步操作代码
            socketService.sendDeviceRefresh().success(function (response){

            });
        } else {
            // 在这里写取消操作的代码
        }

    };

    // 导出到 Excel 的函数（设备列表）
    $scope.exportToExcel = function() {
        deviceService.findAll().success(function (response) {
            if (response != null && response.length > 0) {
                var tableDataWithHeaders = [
                    {
                        设备ID: '设备ID',
                        设备名称: '设备名称',
                        区域: '区域',
                        子区: '子区',
                        设备类型: '设备类型',
                        供应商: '供应商',
                        检测日期: '检测日期',
                        经度: '经度',
                        纬度: '纬度'
                    }
                ];
                response.forEach(function (device) {
                    tableDataWithHeaders.push({
                        设备ID: device.deviceId,
                        设备名称: device.deviceName,
                        区域: device.ofRegionId,
                        子区: device.ofSubRegionId,
                        设备类型: device.deviceModel,
                        供应商: device.supplier,
                        检测日期: device.serviceDate,
                        经度: device.longitude,
                        纬度: device.dimension
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

                saveAs(new Blob([s2ab(wbout)], {type: 'application/octet-stream'}), '设备信息.xlsx');

            }
        })

    }

    //根据选中设备导出相关报警信息
    $scope.exportToExcelByDeviceId = function (){
        alarmService.findAllByDeviceId($scope.selectedDevice.deviceId,$scope.selectedDevice.serverId).success(function (response){
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

                saveAs(new Blob([s2ab(wbout)], {type: 'application/octet-stream'}), $scope.selectedDevice.deviceName+'报警信息.xlsx');

            }
        });
    }

    // 更新所有下拉列表数据-----------------------new code-----------------------------------------------
    // 更新所有下拉列表数据
    // 更新所有下拉列表数据
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
        if ($scope.searchDevice1.region) {
            var filteredData = dataList.filter(item =>
                item.region === $scope.searchDevice1.region &&
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
        updateDeviceTypeList(dataList);

        // 如果当前选择的设备类型不在新的列表中，则清空选择
        if ($scope.searchDevice1.deviceType && !$scope.deviceTypeList1.includes($scope.searchDevice1.deviceType)) {
            $scope.searchDevice1.deviceType = '';
        }
    };

    // 单独的设备类型更新方法
    function updateDeviceTypeList(dataList) {
        if ($scope.searchDevice1.subregion) {
            var filteredData = dataList.filter(item =>
                item.subregion === $scope.searchDevice1.subregion &&
                (!$scope.searchDevice1.region || item.region === $scope.searchDevice1.region) &&
                (!$scope.selectedServer || (item.server && item.server.id === $scope.selectedServer.id))
            );
            $scope.deviceTypeList1 = [...new Set(filteredData.map(item => item.deviceType))].sort();
        } else if ($scope.searchDevice1.region) {
            var filteredData = dataList.filter(item =>
                item.region === $scope.searchDevice1.region &&
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
                $scope.searchDevice.serverId = -1;
            }
            if($scope.deviceId == null){
                $scope.searchDevice1.region = '';
                $scope.searchDevice1.subregion = '';
                $scope.searchDevice1.deviceType = ''; // 重置设备类型选择
            }
            $scope.updateDropdownLists($scope.allData);
        }
    });

    // 监听区域选择变化
    $scope.$watch('searchDevice1.region', function(newVal, oldVal) {
        if (newVal !== oldVal) {
            if($scope.deviceId == null){
                $scope.searchDevice1.subregion = '';
                $scope.searchDevice1.deviceType = ''; // 重置设备类型选择
            }
            $scope.updateDropdownLists($scope.allData);
        }
    });

    // 监听子区域选择变化
    $scope.$watch('searchDevice1.subregion', function(newVal, oldVal) {
        if (newVal !== oldVal) {
            if($scope.deviceId==null){
                $scope.searchDevice1.deviceType = ''; // 重置设备类型选择
            }
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
        $scope.deviceTypeList1 = [...new Set(dataList.map(item => item.deviceType))].sort();
    };


    // 页面加载初始化
    //$scope.initSearchDevice();
    //$scope.loadData();

    $scope.getDeviceTypeName = function(type) {
        const deviceTypeMap = {
            "0": '测速',
            "1": '显示屏',
            "2": '警闪灯',
            "3": '移动灯',
            "4": '信号灯'
        };
        return deviceTypeMap[type] || type; // 如果找不到映射，返回原始值
    };

    //发送刷新设备
    $scope.sendDeviceByDeviceIdAndServerId = function (deviceId,serverId){
      socketService.sendDeviceByDeviceIdAndServerId(deviceId,serverId).success(function (response) {

      });
    };

    window.onload = function () {
        console.log("window.onload 执行了。。。")
        window.addEventListener('message', messageListener);

        var str = $location.absUrl().split('=')[1];
        if(str!=null && str!==''&& str.includes("deviceId")){
            $timeout(function() {
                // 先分割问号，取查询参数部分
                const queryString = $location.absUrl().split('?')[1];

                // 再按&分割成各个参数
                const params = queryString.split('&');

                // 提取每个参数的值
                const sessionId = params[0].split('=')[1];  // sessionId的值
                const deviceId = params[1].split('=')[1];   // deviceId的值
                const serverId = params[2].split('=')[1];   // serverId的值

                console.log('sessionId:', sessionId);
                console.log('deviceId:', deviceId);
                console.log('serverId:', serverId);
                //这是点击了主页坐标点过来的
                $scope.deviceId = $location.absUrl().split('=')[2];
                $scope.serverId = $location.absUrl().split('=')[3];
                deviceService.findById(deviceId,serverId).success(function (response){
                    $scope.searchDevice = response;
                    $scope.selectedServer = response.server;
                    $scope.searchDevice1.serverId = serverId;
                    $scope.searchDevice1.region = response.ofRegionId;
                    $scope.searchDevice1.subregion = response.ofSubRegionId;
                    $scope.searchDevice1.deviceType = response.deviceType;
                    $scope.searchDevice1.deviceName = response.deviceName;
                    if($scope.searchDevice.deviceType === "0"){
                        $scope.searchDevice.deviceType = "测速";
                    }else if($scope.searchDevice.deviceType === "1"){
                        $scope.searchDevice.deviceType = "显示屏";
                    }else if($scope.searchDevice.deviceType === "2"){
                        $scope.searchDevice.deviceType = "警闪灯";
                    }else if($scope.searchDevice.deviceType === "3"){
                        $scope.searchDevice.deviceType = "移动灯";
                    }
                    $scope.search(1,10);
                    $scope.sessionId = str.split("&deviceId")[0];
                });
            }, 100);

        }else {
            $scope.sessionId = str;
        }
    }
})