app.controller("serverController",function ($scope,serverService,socketService){

    $scope.addServer = {flag:0};


    $scope.errorMsg={flag:false,message:''}

    $scope.findAll = function (){
        //查询所有
        serverService.findAll().success(function (response){
            $scope.serverList = response;
        })
    };


    $scope.deleteById = function (id){
        //删除之前弹窗提醒用户
        const isConfirmed = confirm('确定要删除服务器吗？(删除服务器会删除其关联所有信息,请谨慎操作!)');
        // 根据用户选择执行操作
        if (isConfirmed) {
            // 在这里写你的下一步操作代码
            serverService.deleteById(id).success(function (response){
                $scope.findAll();
            });
        } else {
            // 在这里写取消操作的代码
        }


    };

    $scope.saveServer = function (){
        //合法性校验
        if($scope.addServer.password == null || $scope.addServer.password ===''){
            $scope.errorMsg.flag = true;
            $scope.errorMsg.message = '用户密码不能为空';
        }else if($scope.addServer.username == null || $scope.addServer.username === ''){
            $scope.errorMsg.flag = true;
            $scope.errorMsg.message = '用户名不能为空';
        }else if($scope.addServer.serverName == null || $scope.addServer.serverName === ''){
            $scope.errorMsg.flag = true;
            $scope.errorMsg.message = '服务器名称不能为空';
        }else if($scope.addServer.ip == null || $scope.addServer.ip === ''){
            $scope.errorMsg.flag = true;
            $scope.errorMsg.message = 'ip不能为空';
        }else if($scope.addServer.port == null || $scope.addServer.port === ''){
            $scope.errorMsg.flag = true;
            $scope.errorMsg.message = '端口不能为空';
        }else if($("#error_serverName1").text() !== ''){
            $scope.errorMsg.flag = true;
            $scope.errorMsg.message = '服务器名称已存在';
        }else if(!isValidIPStrict($scope.addServer.ip)){
            $scope.errorMsg.flag = true;
            $scope.errorMsg.message = 'ip错误';
        }else if(!isValidPortStrict($scope.addServer.port)){
            $scope.errorMsg.flag = true;
            $scope.errorMsg.message = 'port错误';
        } else {
            serverService.saveServer($scope.addServer).success(function (response){
                if(response.type === -1){
                    alert(response.message);
                }else {
                    //新增后重新查询
                    $('#editModal1').modal('hide');
                    $scope.initErrorMsg();
                    $scope.findAll();
                }
            });
        }
    };

    $scope.initErrorMsg = function (){
        $scope.errorMsg.flag = false;
        $scope.errorMsg.message = '';
    }




    $scope.findByServerName = function (){
        if($scope.addServer.serverName!=null && $scope.addServer.serverName!==''){
            serverService.findByServerName($scope.addServer.serverName).success(function (response){
                console.log("response.serverName: "+response.serverName);
                console.log("response: "+response);
                if(response.serverName === '' || response.serverName === "" || response.serverName === undefined){
                    $("#error_serverName1").text('');
                }else {
                    $("#error_serverName1").text('该服务器名已经存在!,请重试').css('color','red');
                }
            });
        }
    };

    $scope.initAddServer = function (){
        $('#error_serverName1').text('');
        $scope.initErrorMsg();
        $scope.addServer = {flag:0};
    };

    $scope.openSocket = function (server){
        const isConfirmed = confirm('确定要启动服务器吗？');
        // 根据用户选择执行操作
        if (isConfirmed) {
            // 在这里写你的下一步操作代码
            socketService.openSocket(server).success(function (response) {
                alert(response.message);
                //刷新列表
                $scope.findAll();
            })
        } else {
            // 在这里写取消操作的代码
        }
    }

    $scope.closeSocket = function (server){
        const isConfirmed = confirm('确定要关闭服务器吗？');
        // 根据用户选择执行操作
        if (isConfirmed) {
            // 在这里写你的下一步操作代码
            socketService.closeSocket(server).success(function (response) {
                alert(response.message);
                //刷新列表
                $scope.findAll();
            })
        } else {
            // 在这里写取消操作的代码
        }
    }


    // 更严格的版本，排除特殊IP地址
    function isValidIPStrict(ip) {
        if (typeof ip !== 'string') return false;

        const ipRegex = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;

        if (!ipRegex.test(ip)) return false;

        // 检查是否为特殊IP地址
        const parts = ip.split('.').map(Number);

        // 排除：0.0.0.0, 255.255.255.255, 127.x.x.x, 224-239.x.x.x (组播), 240-255.x.x.x (保留)
        if (ip === '0.0.0.0' || ip === '255.255.255.255') return false;
        if (parts[0] === 127) return false; // 环回地址
        if (parts[0] >= 224 && parts[0] <= 239) return false; // 组播地址
        if (parts[0] >= 240) return false; // 保留地址

        return true;
    }


    // 更严格的版本，排除系统端口
    function isValidPortStrict(port) {
        const portNum = Number(port);

        if (isNaN(portNum) || !Number.isInteger(portNum)) return false;

        // 标准端口范围
        if (portNum < 1 || portNum > 65535) return false;

        // 排除系统保留端口 (0-1023)
        if (portNum <= 1023) return false;

        return true;
    }

});