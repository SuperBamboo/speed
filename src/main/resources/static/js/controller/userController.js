app.controller("userController",function ($scope,userService,regionDeviceTypeService){

    $scope.addUser = {role:"USER"};

    $scope.updateUser = {};
    $scope.regionDeviceTypeList = [];

    $scope.errorMsg={flag:false,message:''}

    $scope.findAll = function (){
        //查询所有
        userService.findAll().success(function (response){
            $scope.userList = response;
        })
    };

    $scope.findById = function (id){
        userService.findById(id).success(function (response){
           $scope.updateUser = response;
            regionDeviceTypeService.findByUserId($scope.updateUser.id).success(function (response){
                $scope.regionDeviceTypeList = response;
            });
        });
    };

    $scope.deleteById = function (id){
        //删除之前弹窗提醒用户
        userService.deleteById(id).success(function (response){
            $scope.findAll();
        });
    };

    $scope.saveUser = function (){
        //合法性校验
        if($scope.addUser.password == null || $scope.addUser.password ===''){
            $scope.errorMsg.flag = true;
            $scope.errorMsg.message = '用户密码不能为空';
        }else if($scope.addUser.username == null || $scope.addUser.username === ''){
            $scope.errorMsg.flag = true;
            $scope.errorMsg.message = '用户名不能为空';
        }else if($("#error_username1").text() !== ''){
            $scope.errorMsg.flag = true;
            $scope.errorMsg.message = '用户名已存在';
        }else {
            userService.saveUser($scope.addUser).success(function (response){
                //新政后重新查询
                $('#editModal1').modal('hide');
                $scope.initErrorMsg();
                $scope.findAll();
            });
        }
    };

    $scope.initErrorMsg = function (){
        $scope.errorMsg.flag = false;
        $scope.errorMsg.message = '';
    }

    $scope.update1User = function (){
        //进行非空校验
        if($scope.updateUser.password==null || $scope.updateUser.password === ''){
            $scope.errorMsg.flag = true;
            $scope.errorMsg.message = '用户密码不能为空';
        }else {
            userService.update1User($scope.updateUser).success(function (response){
                //新政后重新查询
                //更新用户权限
                userService.update2User($scope.updateUser.id,$scope.regionDeviceTypeList).success(function (response) {
                    $('#editModal').modal('hide');
                    //重置errorMsg
                    $scope.initErrorMsg();
                    $scope.findAll();
                });
            });
        }
    };

    $scope.findByUsername = function (){
        if($scope.addUser.username!=null && $scope.addUser.username!==''){
            userService.findByUsername($scope.addUser.username).success(function (response){
                if(response.username ==="该用户不存在"){
                    $("#error_username1").text('');
                }else {
                    $("#error_username1").text('该用户名已经存在!,请重试').css('color','red');
                }
            });
        }
    };

    $scope.initAddUser = function (){
        $('#error_username1').text('');
        $scope.initErrorMsg();
        $scope.addUser = {role: "USER"};
    };

    $scope.initUpdateUser = function (){
        $scope.updateUser = {};
        $scope.initErrorMsg();
    };
})