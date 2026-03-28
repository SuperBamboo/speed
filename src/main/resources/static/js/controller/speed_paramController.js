app.controller('speed_paramController', function ($scope,$timeout,socketService,$location,warningToneService,displayService) {

    $scope.sessionId = null;
    $scope.deviceId = null;
    $scope.serverId = -1;


    // 语音提示列表
    $scope.warningToneList = [];
    $scope.warningToneNames = [];

    //显示语句列表
    $scope.displayList = [];
    $scope.displayNames = [];

    $scope.wxxdList = [];

    $scope.$watch()


    // 显示状态消息
    $scope.showStatus = function(message, type) {
        const statusElement = document.getElementById('statusMessage');
        if (statusElement) {
            statusElement.textContent = message;
            statusElement.className = 'status-message';
            statusElement.classList.add(`status-${type}`);
            statusElement.style.display = 'block';

            $timeout(function() {
                statusElement.style.display = 'none';
            }, 10000);
        }
    };

    // 重新查询参数
    $scope.reReadParams = function() {
        if ($scope.sessionId && $scope.deviceId && $scope.serverId) {
            //$scope.showStatus('正在查询参数...', 'success');

            socketService.findNBDeviceParamByDeviceId($scope.sessionId, $scope.deviceId, $scope.serverId).success(function(response) {
                //定位到第一个
                $scope.init();
            })
        }
    };

    // 下载参数
    $scope.downloadParams = function() {

        if ($scope.sessionId && $scope.deviceId && $scope.serverId && $scope.speedSys) {

            // 发送设置对应设备的机内参数
            socketService.setNBDeviceParamByDeviceId($scope.sessionId, $scope.deviceId,$scope.serverId,$scope.speedSys).success(function(response) {
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
                    //2.查询测速设备机内参数返回
                    $scope.speedSys = e.data.data;

                    break;
                default:
                    break;
            }
        })
    }

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
                            index:i,
                            name:$scope.warningToneList[i].warningToneName.substring(0,4)
                        }
                    );
                }

                let index = 1;
                for (let i = 1; i < 16; i++) {
                    if($scope.warningToneNames.length === i){
                        $scope.warningToneNames.push({
                            index:i,
                            name:"定制 "+index
                        });
                        index++;
                    }
                }
            });

            //查询显示语句列表
            displayService.findByDeviceId(deviceId,serverId).success(function (response) {
                $scope.displayList = response;
                for(let i = 0;i<$scope.displayList.length;i++){
                    $scope.displayNames.push(
                        {
                            index:i,
                            name:$scope.displayList[i].displayName.length>=4?$scope.displayList[i].displayName.substring(0,4):$scope.displayList[i].displayName
                        }
                    );
                }

                let index = 1;
                for (let i = 1; i < 16; i++) {
                    if($scope.displayNames.length === i){
                        $scope.displayNames.push(
                            {
                                index:i,
                                name:"定制 "+index
                            }
                        );
                        index++;
                    }
                }
            })

            // 发送查询机内参数
            socketService.findNBDeviceParamByDeviceId(sessionId, deviceId, serverId).success(function (response) {
                if(response!=null && response.type!=null){
                    // 加载语音提示列表
                    //$scope.loadWarningTones();
                }

            });

        }

        for (let i = 0; i < 50; i++) {
            $scope.wxxdList.push(i);
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

            // 显示状态提示
            //showStatus(`已切换到${menuText}`, 'success');
        }
    }
});