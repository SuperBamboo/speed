app.controller('userInfoController',function ($scope,userInfoService) {

    $scope.updateUser = {};

    $scope.beforePassword = "";
    $scope.newPassword1 = "";
    $scope.newPassword2 = "";
    $scope.errorMsg = {flag:false,message:''}

    $scope.findUsername = function (){
        userInfoService.getCurrentUser().success(function (response) {
            $scope.currentUsername = response;
            $scope.updateUser.username = response;
        });
    };

    $scope.checkPassword = function (){
        userInfoService.checkPassword($scope.currentUsername,$scope.beforePassword).success(function (response) {
            if(response.type === 0){
                $scope.initErrorMsg();
            }else {
                $scope.errorMsg.flag = true;
                $scope.errorMsg.message = response.message;
            }

        });
    }

    $scope.initErrorMsg=function (){
        $scope.errorMsg.flag = false;
        $scope.errorMsg.message = '';
    }

    /**
     * 再次输入密码失去焦点时判断两个密码是否一致
     */
    $scope.checkTwoPassword = function (){
        if($scope.newPassword1 === $scope.newPassword2){
            $scope.errorMsg.flag = false;
            $scope.errorMsg.message = '';
        }else {
            $scope.errorMsg.flag = true;
            $scope.errorMsg.message = '两次密码不一致,重新输入新密码';
        }
    };

    $scope.updateUserPassword = function (){
        if($scope.beforePassword === null || $scope.beforePassword ==='' || $scope.beforePassword ===""){
            $scope.errorMsg.flag = true;
            $scope.errorMsg.message = '原密码不能为空';
            return;
        }
        if($scope.newPassword1 === null || $scope.newPassword1 === "" || $scope.newPassword1 === ''){
            $scope.errorMsg.flag = true;
            $scope.errorMsg.message = '修改密码1不能为空';
        }
        if($scope.newPassword2 === null || $scope.newPassword2 === "" || $scope.newPassword2 === ''){
            $scope.errorMsg.flag = true;
            $scope.errorMsg.message = '修改密码2不能为空';
        }
        $scope.checkPassword();
        if($scope.errorMsg.flag===true){
            return;
        }
        $scope.checkTwoPassword();
        if($scope.errorMsg.flag===true){
            return;
        }
        //至此校验成功
        $scope.updateUser.password = $scope.newPassword2;
        userInfoService.updateUserPassword($scope.updateUser).success(function (response) {
            alert("修改成功，请重新登录");
            window.parent.location.href = '/logout';
        });
    }
});