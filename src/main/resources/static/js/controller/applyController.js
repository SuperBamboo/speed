app.controller("applyController",function ($scope,$location,sessionService,applyService,displayService,portService,warningToneService,socketService,planParamService){
    $scope.sessionId = null;

    $scope.sessionId = $location.absUrl().split('=')[1];
    $scope.applyList = [];
    $scope.portList = [];
    $scope.portList1 = [];
    $scope.planModeList = [];

    $scope.selectedApply = {};
    $scope.selectedWarningToneNo = "";
    $scope.selectedPlanModeNo = "";

    $scope.entity = {save: "false", auto: "true"};
    $scope.entity1 = {save: "false", auto: "true"};

    $scope.findAll = function (){
        //查询所有
        applyService.findAll().success(function (response){
            $scope.applyList = response;
            //获取共享数据
            $scope.sessionId = sessionService.getSessionId();
        })
    };

    $scope.initData1 = function (){
        //更新flag
        applyService.updateFlag($scope.selectedApply).success(function (response){
            $scope.findAll();
        });
    };

    $scope.selectedDisplayNo = "";
    $scope.selectedDisplay = {};
    $scope.displayList = [];

    $scope.parseApply = function (apply){
        $scope.selectedApply = apply;
        if($scope.selectedApply.applyType === 1){
            //手动管控

            $("#card1_sdgk").css('display', 'flex');
            $("#card1_csgk").css('display', 'none');

            $("#send_sdgk").css('display', 'flex');
            $("#cancel_sdgk").css('display', 'none');

            $scope.selectedWarningToneNo = $scope.selectedApply.deviceControlModeApply.warningTone;
            $scope.selectedDisplayNo = $scope.selectedApply.deviceControlModeApply.displayNo;

            portService.findByDeviceId($scope.selectedApply.deviceId,$scope.selectedApply.serverId).success(function (response){
                $scope.portList = angular.copy(response);
                $scope.portList1 = angular.copy(response);
                if ($scope.portList != null && $scope.portList.length > 0) {
                    var relayStauBinary = $scope.selectedApply.deviceControlModeApply.relay1;
                    var relayStau1Binary = $scope.selectedApply.deviceControlModeApply.relay2;
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
                warningToneService.findByDeviceId($scope.selectedApply.deviceId,$scope.selectedApply.serverId).success(function (response) {
                    $scope.warningToneList = response;
                });
                displayService.findByDeviceId($scope.selectedApply.deviceId,$scope.selectedApply.serverId).success(function (response){
                    $scope.displayList = response;
                    for(var x = 0;x<$scope.displayList.length;x++){
                        if($scope.displayList[x].displayNo === $scope.selectedDisplayNo){
                            $scope.selectedDisplay = $scope.displayList[x];
                        }
                    }
                });
            });

        }else if($scope.selectedApply.applyType === 2){
            //手动程控
            $("#card1_sdgk").css('display', 'none');
            $("#card1_csgk").css('display', 'flex');

            $("#send_csgk").css('display', 'flex');
            $("#cancel_csgk").css('display', 'none');

            $scope.selectedPlanModeNo = $scope.selectedApply.devicePlanModeApply.planNo;

            planParamService.findByDeviceIdAndPlanModeNo($scope.selectedApply.deviceId,$scope.selectedPlanModeNo,$scope.selectedApply.serverId).success(function (response){
               $scope.planParamList = response;
            });

        }else if($scope.selectedApply.applyType === 3){
            //取消手动管控

            $("#card1_sdgk").css('display', 'flex');
            $("#card1_csgk").css('display', 'none');

            $("#send_sdgk").css('display', 'none');
            $("#cancel_sdgk").css('display', 'flex');


        }else if($scope.selectedApply.applyType === 4){
            //取消手动程控
            $("#card1_sdgk").css('display', 'none');
            $("#card1_csgk").css('display', 'flex');

            $("#send_csgk").css('display', 'none');
            $("#cancel_csgk").css('display', 'flex');
        }
    }

    /**
     * 手动管控下发
     */
    $scope.sendSDGK = function (serverId,a){
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
        deviceControlModeSet.deviceId = $scope.selectedApply.deviceId;
        if(a === 0){
            //下发取消手动管控
            deviceControlModeSet.value = "0";
        }else {
            //下发手动管控
            deviceControlModeSet.value = "1";
            deviceControlModeSet.ctrlNo = $scope.selectedApply.deviceControlModeApply.ctrlNo;
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

            if($scope.selectedApply.deviceControlModeApply.relay1!=null && $scope.selectedApply.deviceControlModeApply.relay1 !=="" && $scope.selectedApply.deviceControlModeApply.relay1 !=="null"){
                const relayStau = $scope.selectedApply.deviceControlModeApply.relay1;
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
                        relayStauBinary = relayStauBinary + $scope.selectedApply.deviceControlModeApply.relay1.charAt(8 - i);
                    }
                }
                deviceControlModeSet.relay1 = parseInt(relayStauBinary, 2).toString();
            }

            if($scope.selectedApply.deviceControlModeApply.relay2!=null && $scope.selectedApply.deviceControlModeApply.relay2 !=="" && $scope.selectedApply.deviceControlModeApply.relay2 !=="null") {
                const relayStau1 = $scope.selectedApply.deviceControlModeApply.relay2;
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
                        relayStau1Binary = relayStau1Binary + $scope.selectedApply.deviceControlModeApply.relay2.charAt(8 - i);
                    }
                }
                deviceControlModeSet.relay2 = parseInt(relayStau1Binary, 2).toString();
            }

            if($scope.selectedApply.deviceControlModeApply.displayNo!=null && $scope.selectedApply.deviceControlModeApply.displayNo !=="" && $scope.selectedApply.deviceControlModeApply.displayNo !=="null") {
                deviceControlModeSet.displayNo = $scope.selectedDisplayNo;
            }
            if($scope.selectedApply.deviceControlModeApply.warningTone!=null && $scope.selectedApply.deviceControlModeApply.warningTone !=="" && $scope.selectedApply.deviceControlModeApply.warningTone !=="null"){
                deviceControlModeSet.warningTone = $scope.selectedWarningToneNo;
            }
            if($scope.selectedApply.deviceControlModeApply.volume!=null && $scope.selectedApply.deviceControlModeApply.volume !=="" && $scope.selectedApply.deviceControlModeApply.volume !=="null"){
                deviceControlModeSet.volume = $scope.selectedApply.deviceControlModeApply.volume;
            }
            if($scope.selectedApply.deviceControlModeApply.playInteval!=null && $scope.selectedApply.deviceControlModeApply.playInteval !=="" && $scope.selectedApply.deviceControlModeApply.playInteval !=="null"){
                deviceControlModeSet.playInteval = $scope.selectedApply.deviceControlModeApply.playInteval;
            }
            if($scope.selectedApply.deviceControlModeApply.playNumbers!=null && $scope.selectedApply.deviceControlModeApply.playNumbers !=="" && $scope.selectedApply.deviceControlModeApply.playNumbers !=="null"){
                deviceControlModeSet.playNumbers = $scope.selectedApply.deviceControlModeApply.playNumbers;
            }
            if($scope.selectedApply.deviceControlModeApply.holdTime!=null && $scope.selectedApply.deviceControlModeApply.holdTime !=="" && $scope.selectedApply.deviceControlModeApply.holdTime !=="null"){
                deviceControlModeSet.holdTime = $scope.selectedApply.deviceControlModeApply.holdTime;
            }
        }

        socketService.sendSDGK($scope.sessionId,deviceControlModeSet).success(function (response){
            //更新flag
            applyService.updateFlag($scope.selectedApply).success(function (response){
               $scope.findAll();
            });
        });
    }

    $scope.sendCSGK = function (serverId,a){
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
        devicePlanModeSet.deviceId = $scope.selectedApply.deviceId;

        if(a === 0){
            //取消程式控制
            devicePlanModeSet.value = "0";
        }else {
            //下发程式控制
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
            devicePlanModeSet.holdTime = $scope.selectedApply.devicePlanModeApply.holdTime;
            devicePlanModeSet.intervalTime = $scope.selectedApply.devicePlanModeApply.intervalTime;
            devicePlanModeSet.repetitions = $scope.selectedApply.devicePlanModeApply.repetitions;
            devicePlanModeSet.sysTime = $scope.selectedApply.devicePlanModeApply.sysTime;
            for (var i = 0;i<$scope.planParamList.length;i++){
                devicePlanModeSet.planParamList.push({planParamNo:$scope.planParamList[i].planParamNo,defaultValue:$scope.planParamList[i].defaultValue});
            }
        }
        socketService.sendCSGK($scope.sessionId,devicePlanModeSet).success(function (response){
            //更新flag
            applyService.updateFlag($scope.selectedApply).success(function (response){
                $scope.findAll();
            });
        });
    }

    $scope.needShowPort = function (port) {
        return port.portNo >= 1 && port.portNo <= 6;
    }
})