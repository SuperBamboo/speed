app.controller('jsd_paramController', function ($scope,$timeout,socketService,$location,warningToneService) {



    $scope.sessionId = null;
    $scope.deviceId = null;
    $scope.serverId = -1;

    // 语音提示列表
    $scope.warningToneList = [];
    $scope.warningToneNames = [];
    $scope.wxxdList = [];
    $scope.jsdZDYSDIndexList = [];
    $scope.jsdZDYSDLightList = [{index:0,name:"关闭显示"},{index:1,name:"亮度1"},{index:2,name:"亮度2"},{index:3,name:"亮度3"},{index:4,name:"亮度4"},{index:5,name:"亮度5"},{index:6,name:"亮度6"},{index:7,name:"最大亮度"}];


    $scope.currentDYLX = 0;
    $scope.currentJSDPowerDY = null;

    //警闪灯自定义闪灯触发规则选项
    $scope.currentJSDZDYSDCFGZIndex = 0;
    $scope.currentJSDZDYSDCFGZ = null;

    //语音触发规则选项
    $scope.currentJSDVoiceIndex = 0;
    $scope.currentJSDVoiceSys = null;

    //通道触发规则选项
    $scope.currentJSDTDCFGZIndex = 0;
    $scope.currentJSTDCFGZ = null;

    //自定义闪灯
    $scope.currentJSDZDYSDIndex = 0;
    $scope.currentJSTZDYSDSys = null;

    //时段方案
    $scope.currentJSDSDFAIndex = 0;
    $scope.currentJSTSDFASysList = null;

    $scope.jsdSys = null;

    // 重新查询参数
    $scope.reReadParams = function() {
        if ($scope.sessionId && $scope.deviceId && $scope.serverId) {
            //$scope.showStatus('正在查询参数...', 'success');

            socketService.findNBDeviceParamByDeviceId($scope.sessionId, $scope.deviceId, $scope.serverId).success(function(response) {
                //定位到第一个工作方式选择--------------待完成------------------
                $scope.init();
            })
        } else {
            $scope.showStatus('缺少必要参数，无法查询', 'error');
        }
    };

    // 下载参数
    $scope.downloadParams = function() {

        if ($scope.sessionId && $scope.deviceId && $scope.serverId && $scope.jsdSys) {

            // 发送设置对应设备的机内参数
            socketService.setJSDNBDeviceParamByDeviceId($scope.sessionId, $scope.deviceId,$scope.serverId,$scope.jsdSys).success(function(response) {
                    if(response.type === -1){
                        alert(response.message);
                    }
            })
        } else {
            $scope.showStatus('缺少必要参数，无法下载', 'error');
        }
    };

    var messageListener = function (e) {
        $timeout(function () {
            switch (e.data.type) {
                case 1://查询设备状态
                    break;
                case 2:
                    //2.查询警闪灯机内参数返回
                    $scope.jsdSys = e.data.data;
                    $scope.currentDYLX = $scope.jsdSys.jsdPowerSys.dylx;
                    $scope.currentJSDPowerDY = $scope.jsdSys.jsdPowerSys.jsdPowerDYList[$scope.currentDYLX];
                    $scope.currentJSDZDYSDCFGZIndex = 0;
                    $scope.currentJSDZDYSDCFGZ = $scope.jsdSys.jsdzdysdcfgzList[0];
                    $scope.currentJSDVoiceIndex = 0;
                    $scope.currentJSDVoiceSys = $scope.jsdSys.jsdVoiceSysList[0];
                    $scope.currentJSDTDCFGZIndex = 0;
                    $scope.currentJSTDCFGZ = $scope.jsdSys.jsdtdcfgzList[0];
                    $scope.currentJSDZDYSDIndex = 0;
                    $scope.currentJSTZDYSDSys = $scope.jsdSys.jsdzdysdSysList[0];
                    $scope.currentJSDSDFAIndex = 0;
                    $scope.currentJSTSDFASysList = $scope.jsdSys.jsdsdfaSysList;
                    $scope.initTimeData();
                    break;
                default:
                    break;
            }
        })
    }

    //监视电源类型选项
    $scope.$watch('currentDYLX', function (newValue, oldValue) {
        //console.log("newValue：" + newValue);
        if (newValue != null && '' !== newValue && "" !== newValue) {
            if($scope.jsdSys!=null){
                $scope.currentJSDPowerDY = $scope.jsdSys.jsdPowerSys.jsdPowerDYList[newValue];
                $scope.jsdSys.jsdPowerSys.dylx = $scope.currentDYLX;
            }
        }
    });

    //监视自定义闪灯触发规则
    $scope.$watch('currentJSDZDYSDCFGZIndex', function (newValue, oldValue) {
        //console.log("newValue：" + newValue);
        if (newValue != null && '' !== newValue && "" !== newValue) {
            if($scope.jsdSys!=null){
                $scope.currentJSDZDYSDCFGZ = $scope.jsdSys.jsdzdysdcfgzList[newValue];
            }
        }
    });

    //监视语音触发规则
    $scope.$watch('currentJSDVoiceIndex', function (newValue, oldValue) {
        //console.log("newValue：" + newValue);
        if (newValue != null && '' !== newValue && "" !== newValue) {
            if($scope.jsdSys!=null){
                $scope.currentJSDVoiceSys = $scope.jsdSys.jsdVoiceSysList[newValue];
            }
        }
    });

    //监视通道触发规则
    $scope.$watch('currentJSDTDCFGZIndex', function (newValue, oldValue) {
        //console.log("newValue：" + newValue);
        if (newValue != null && '' !== newValue && "" !== newValue) {
            if($scope.jsdSys!=null){
                $scope.currentJSTDCFGZ = $scope.jsdSys.jsdtdcfgzList[newValue];
            }
        }
    });

    //监视自定义闪灯
    $scope.$watch('currentJSDZDYSDIndex', function (newValue, oldValue) {
        //console.log("newValue：" + newValue);
        if (newValue != null && '' !== newValue && "" !== newValue) {
            if($scope.jsdSys!=null){
                $scope.currentJSTZDYSDSys = $scope.jsdSys.jsdzdysdSysList[newValue];
            }
        }
    });

    //监视时段方案
    $scope.$watch('currentJSDSDFAIndex', function (newValue, oldValue) {
        //console.log("newValue：" + newValue);
        if (newValue != null && '' !== newValue && "" !== newValue) {
            if($scope.jsdSys!=null){
                if(parseInt(newValue) === 0){
                    $scope.currentJSTSDFASysList = $scope.jsdSys.jsdsdfaSysList;    //工作日
                }else {
                    $scope.currentJSTSDFASysList = $scope.jsdSys.jsdsdfaSysList1;   //节假日
                }
                $scope.initTimeData();
            }
        }
    });

    window.onload = function () {
        window.addEventListener('message', messageListener);

        let str = $location.absUrl().split('=')[1];
        if(str != null && str !== '' && str.includes("deviceId")) {
            // 先分割问号，取查询参数部分
            const queryString = $location.absUrl().split('?')[1];

            // 再按&分割成各个参数
            const params = queryString.split('&');

            // 提取每个参数的值
            const sessionId = params[0].split('=')[1];  // sessionId的值
            const deviceId = params[1].split('=')[1];   // deviceId的值
            const serverId = params[2].split('=')[1];   // serverId的值

            console.log('jsd_sessionId:', sessionId);
            console.log('jsd_deviceId:', deviceId);
            console.log('jsd_serverId:', serverId);

            $scope.deviceId = deviceId;
            $scope.serverId = serverId;
            $scope.sessionId = sessionId;

            //查询语音列表
            warningToneService.findByDeviceId(deviceId,serverId).success(function (response) {
                $scope.warningToneList = response;
                for(let i = 0;i<$scope.warningToneList.length;i++){
                    $scope.warningToneNames.push(
                        {
                            index:parseInt($scope.warningToneList[i].warningToneNo),
                            name:$scope.warningToneList[i].warningToneName.substring(0,4)
                        }
                    );
                }
            });

            // 发送查询机内参数
            socketService.findNBDeviceParamByDeviceId(sessionId, deviceId, serverId).success(function (response) {
                if(response!=null && response.type!=null){

                }

            });

            for (let i = 0; i < 50; i++) {
                $scope.wxxdList.push(i);
            }

            for (let i = 0; i < 64; i++) {
                $scope.jsdZDYSDIndexList.push(i);
            }
        }
    };

    $scope.init = function (){
        // 获取所有菜单项
        const menuItems = document.querySelectorAll('.menu-item');
        // 获取所有参数内容区域
        const paramSections = document.querySelectorAll('.param-section');
        // 获取当前参数标题元素
        const currentParamTitle = document.getElementById('currentParamTitle');
        // 获取状态消息元素
        const statusMessage = document.getElementById('statusMessage');
        // 初始激活第一个菜单项
        if (menuItems.length > 0) {
            const firstMenuItem = menuItems[0];
            const targetId = firstMenuItem.getAttribute('data-target');
            console.log("初始激活目标ID:", targetId);

            // 确保对应的参数区域显示
            paramSections.forEach(section => {
                if (section.id === targetId) {
                    section.classList.add('active');
                }else {
                    section.classList.remove('active')
                }
            });

            //console.log("点击菜单项:", this.querySelector('.menu-text').textContent, "目标ID:", targetId);

            // 更新菜单活动状态
            menuItems.forEach(menuItem => menuItem.classList.remove('active'));
            menuItems[0].classList.add('active');

            // 更新参数标题
            const menuText = menuItems[0].querySelector('.menu-text').textContent;
            currentParamTitle.textContent = menuText;

            // 显示对应的参数内容区域
            paramSections.forEach(section => {
                console.log("检查参数区域:", section.id);
                section.classList.remove('active');
                if (section.id === targetId) {
                    console.log("激活参数区域:", section.id);
                    section.classList.add('active');
                }
            });

        }
    }

    // 初始化数据
    $scope.timeRows = [];
    $scope.selectedRow = 0;

    // 初始化时间数据
    $scope.initTimeData = function() {
        $scope.timeRows = [];
        $scope.selectedRow = 0;

        // 第一行的开始时间固定为0:00
        var prevEndHour = '0';
        var prevEndMinute = '0';

        for (var i = 0; i < 10; i++) {
            var endHour = $scope.currentJSTSDFASysList[i] ? $scope.currentJSTSDFASysList[i].endHour : '23';
            var endMinute = $scope.currentJSTSDFASysList[i] ? $scope.currentJSTSDFASysList[i].endMin : '59';

            // 验证并格式化数据
            endHour = $scope.validateTime(endHour, 'hour');
            endMinute = $scope.validateTime(endMinute, 'minute');

            $scope.timeRows.push({
                index: i + 1,
                startHour: prevEndHour,
                startMinute: prevEndMinute,
                endHour: endHour,
                endMinute: endMinute
            });

            // 更新下一行的开始时间
            prevEndHour = endHour;
            prevEndMinute = endMinute;
        }
    };

    // 验证时间
    $scope.validateTime = function(value, type) {
        if (!value || value === '') {
            return type === 'hour' ? '0' : '0';
        }

        // 移除非数字字符
        value = value.toString().replace(/[^0-9]/g, '');

        if (value === '') {
            return type === 'hour' ? '0' : '0';
        }

        var numValue = parseInt(value);

        if (type === 'hour') {
            if (numValue < 0) return '0';
            if (numValue > 23) return '23';
            return numValue.toString();
        } else {
            if (numValue < 0) return '0';
            if (numValue > 59) return '59';
            return numValue.toString();
        }
    };

    // 更新下一行的开始时间
    $scope.updateNextStartTime = function(currentIndex) {
        // 验证当前行的结束时间
        var currentRow = $scope.timeRows[currentIndex];
        currentRow.endHour = $scope.validateTime(currentRow.endHour, 'hour');
        currentRow.endMinute = $scope.validateTime(currentRow.endMinute, 'minute');

        // 如果有下一行，更新下一行的开始时间
        if (currentIndex < 9) {
            var nextRow = $scope.timeRows[currentIndex + 1];
            nextRow.startHour = currentRow.endHour;
            nextRow.startMinute = currentRow.endMinute;

            // 递归更新后续行
            $scope.updateNextStartTime(currentIndex + 1);
        }
    };

    // 选择行
    $scope.selectRow = function(rowIndex) {
        console.log("选中上一行为:"+$scope.selectedRow);
        //此处更新数据中，结束时间和结束分为发送数据内容

        $scope.selectedRow = rowIndex;
        console.log('选中第', rowIndex + 1, '行');
        //------此处更新解析数据-----------------
    };

    //用户手动更行时段结束时分
    $scope.updateSD = function (){
        $scope.currentJSTSDFASysList[$scope.selectedRow].endHour =  $scope.timeRows[$scope.selectedRow].endHour;
        $scope.currentJSTSDFASysList[$scope.selectedRow].endMin =  $scope.timeRows[$scope.selectedRow].endMinute;
    }

});