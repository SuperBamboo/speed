app.controller('websocketController',function ($scope,userInfoService,$timeout,configService,sessionService) {

    $scope.sessionId = 'aaa';
    $scope.contentWindow = null;

    $scope.currentUsername = "";

    function playBeep() {
        // 创建音频上下文
        var audioCtx = new (window.AudioContext || window.webkitAudioContext)();

        // 创建一个振荡器
        var oscillator = audioCtx.createOscillator();
        oscillator.type = 'triangle'; // 正弦波
        oscillator.frequency.setValueAtTime(440, audioCtx.currentTime); // 设置频率为440Hz (A4音)

        // 创建音量控制
        var gainNode = audioCtx.createGain();
        gainNode.gain.setValueAtTime(0.5, audioCtx.currentTime); // 设置音量

        // 连接振荡器到音量控制，再连接到音频输出
        oscillator.connect(gainNode);
        gainNode.connect(audioCtx.destination);

        // 开始振荡器
        oscillator.start();

        // 停止振荡器在1秒后
        oscillator.stop(audioCtx.currentTime + 0.5);
    }

    var findUserName = function (){
        userInfoService.getCurrentUser().success(function (response) {
            $scope.currentUsername = response;
        })
    }

    $scope.initApplyNum =function (){
        $("#apply_num").text("0");
        $('#apply_li').text("");
    }

    $scope.initAlarmNum =function (){
        $("#alarm_num").text("0");
        $('#alarm_li').text("");
    }

    $scope.init = function () {
        //页面初始化就加载用户信息
        findUserName();
        configService.getWebSocketUrl().success(function (response){

            if(response!=null){
                // 创建 WebSocket 连接
                const socket = new WebSocket(response);

                // 当连接建立时触发
                socket.onopen = function (event) {
                    console.log('WebSocket connection established.');
                };

                // 当收到消息时触发
                socket.onmessage = function (event) {
                    if(event.data.includes('SessionId')){
                        $scope.$apply(function (){
                            $scope.sessionId = event.data.replace('SessionId:','').trim();
                        })

                        console.log("当前sessionId:",$scope.sessionId);
                        //存储在共享中
                        sessionService.setSessionId($scope.sessionId);

                        //三秒后再发送一次
                        $timeout(function(){
                            var aa = document.getElementById("iframe").contentWindow;
                            var message4 = {
                                type:5,
                                data:$scope.sessionId
                            }
                            aa.postMessage(message4,"*");
                        },2000);
                        return;
                    }
                    if(event.data.includes('===')){
                        let parts = event.data.split("===");
                        if(parts[0] === '1'){
                            //查询设备状态返回
                            let deviceStau = JSON.parse(parts[1]);
                            //$scope.contentWindow.postMessage(parts[1],"*");
                            //alert($scope.deviceStau);
                            var a = document.getElementById("iframe").contentWindow;
                            var message = {
                                type:1,
                                data:deviceStau
                            }
                            a.postMessage(message,"*");
                        }
                        if(parts[0] === '2'){
                            //设置返回
                            let setResponse = JSON.parse(parts[1]);
                            alert(setResponse.message);
                        }
                        if(parts[0] === '3'){
                            let deviceControlModeApply = JSON.parse(parts[1]);
                            let showMsg = deviceControlModeApply.deviceId;
                            if(deviceControlModeApply.value == "0"){
                                showMsg = showMsg + " 发出取消管控申请"
                            }else {
                                showMsg = showMsg + " 发出手动管控申请";
                            }
                            //推送的申请
                            var n = $("#apply_num").text();
                            $("#apply_num").text(++n);
                            $('#apply_li').append($('<li>\n' +
                                '                                            <a href="#">\n' +
                                '                                                <i class="fa fa-users text-aqua"></i> '+showMsg+'\n' +
                                '                                            </a>\n' +
                                '                                        </li>'));
                            //发送
                            var d = document.getElementById("iframe").contentWindow;
                            var message3 = {
                                type:4,
                                data:showMsg
                            }
                            d.postMessage(message3,"*");
                            //播放声音
                            var audio = document.getElementById("audio_apply");
                            audio.play();
                        }
                        if(parts[0] === '4'){
                            let devicePlanModeApply = JSON.parse(parts[1]);
                            let showMsg = devicePlanModeApply.deviceId;
                            if(devicePlanModeApply.value == "0"){
                                showMsg = showMsg + " 发出取消程控申请"
                            }else {
                                showMsg = showMsg + " 发出程式管控申请";
                            }
                            //推送的申请
                            var n = $("#apply_num").text();
                            $("#apply_num").text(++n);
                            $('#apply_li').append($('<li>\n' +
                                '                                            <a href="#">\n' +
                                '                                                <i class="fa fa-users text-aqua"></i> '+showMsg+'\n' +
                                '                                            </a>\n' +
                                '                                        </li>'));
                            //发送
                            var c = document.getElementById("iframe").contentWindow;
                            var message2 = {
                                type:4,
                                data:showMsg
                            }
                            c.postMessage(message2,"*");

                            var audio = document.getElementById("audio_apply");
                            audio.play();
                        }
                        return;
                    }
                    //console.log('Received message from server:', event.data);
                    // 在页面上显示收到的消息
                    // 例如，可以将消息添加到页面的某个元素中
                    var n = $('#alarm_num').text();
                    $('#alarm_num').text(++n);
                    $('#alarm_li').append($('<li>\n' +
                        '                                            <a href="#">\n' +
                        '                                                <i class="fa fa-users text-aqua"></i> '+event.data+'\n' +
                        '                                            </a>\n' +
                        '                                        </li>'));
                    var b = document.getElementById("iframe").contentWindow;
                    var message1 = {
                        type:3,
                        data:event.data
                    }
                    b.postMessage(message1,"*");
                    //播放音乐
                    playBeep();
                };


                // 当发生错误时触发
                socket.onerror = function (error) {
                    console.error('WebSocket error:', error);
                };
            }

        });

    }

});