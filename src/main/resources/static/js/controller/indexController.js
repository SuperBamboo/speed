app.controller('indexController',function ($scope,deviceService,$timeout,sessionService,alarmService,applyService,regionService) {
    $scope.sessionId = "";
    var messageListener = function (e) {
        $timeout(function (){
            switch (e.data.type) {
                case 3:
                    //更新报警表格
                    $scope.findNewAlarm10Size();
                    break;
                case 4:
                    //更新申请列表
                    $scope.findNewApply10Size();
                    break;
                case 5:
                    //sessionId
                    $scope.sessionId = e.data.data;
                    break;
                default:
                    break;
            }
        })
    }

    window.onload = function () {
        window.addEventListener('message', messageListener);
    };
    if(document.readyState === 'complete'){
        window.onload();
    };
    window.addEventListener('beforeunload',function (){
       window.removeEventListener('message',messageListener);
    });

    $scope.deviceList = [];
    $scope.alarmList = [];
    $scope.applyList = [];

    $scope.map = null;
    $scope.AMap = null;

    $scope.toggleNode = function(node) {
        node.expanded = !node.expanded;
    };

    $scope.treeData1 = [];

    $scope.findNewAlarm10Size = function (){
        alarmService.findNewAlarm10Size().success(function (response){
            $timeout(function (){
                $scope.alarmList = response;
            });
        });
    };

    $scope.findNewApply10Size = function (){
        applyService.findNewApply10Size().success(function (response){
            $timeout(function (){
                $scope.applyList = response;
                if($scope.applyList == null || $scope.applyList.length===0){
                    //没有申请就隐藏
                    $("#apply_flex").css('display','none');
                }else {
                    $("#apply_flex").css('display','flex');
                }
            });
        });
    };

    $scope.findAllRegionAndSubRegionAndDevice = function (){
        regionService.findAllRegionAndSubRegionAndDevice().success(function (response){
            $scope.treeData1 = response;
        });
    }

    $scope.isSlidOut = false;
    $scope.isSlidOut1 = false;

    $scope.toggleSlide = function() {
        $scope.isSlidOut = !$scope.isSlidOut;
        if ($scope.isSlidOut === true){
            $("#right_img").attr('src','../static/images/left.png');
        }else {
            $("#right_img").attr('src','../static/images/right.png');
        }
    };

    $scope.toggleSlide1 = function() {
        $scope.isSlidOut1 = !$scope.isSlidOut1;
        if ($scope.isSlidOut1 === true){
            $("#left_img").attr('src','../static/images/right.png');
        }else {
            $("#left_img").attr('src','../static/images/left.png');
        }
    };

    $scope.alarmDescList = [];
    $scope.applyDescList = [];

    $scope.markerList=[];

    $scope.initMap = function (){

        AMapLoader.load({
            key: "23bcac540381de992b2657af4db726aa", //申请好的Web端开发者key，调用 load 时必填
            version: "2.0", //指定要加载的 JS API 的版本，缺省时默认为 1.4.15
        })
            .then((AMap) => {
                //JS API 加载完成后获取AMap对象
                $scope.AMap = AMap;
                $scope.map = new AMap.Map("map-container", {
                    mapStyle: "amap://styles/grey",
                    viewMode: '2D', //默认使用 2D 模式
                    zoom: 10, //地图级别
                    center: [121.623891,29.885977], //地图中心点
                });
                deviceService.findAllAndFlag().success(function (response) {
                    $scope.deviceList = response;
                    $scope.addMarker();
                });

            })
            .catch((e) => {
                console.error(e); //加载错误提示
            });

        //查询初始数据
        $scope.findNewAlarm10Size();
        $scope.findNewApply10Size();
        $scope.findAllRegionAndSubRegionAndDevice();

        //获取共享数据
        $scope.sessionId = sessionService.getSessionId();
    }

    $scope.initMap1 = function (){

        // 自定义地图层
        const base_url = "http://192.168.0.32:8080/"
        const layers = [new AMap.TileLayer({
            getTileUrl: function (x, y, z) {
                return `${base_url}MAP_zxy/${z}/${x}/${y}.png`;
            },
            opacity: 1,
            zIndex: 99,
        })]

        $scope.map = new AMap.Map("map-container", {
            viewMode: '2D', //默认使用 2D 模式
            zoom: 10, //地图级别
            center: [121.623891,29.885977], //地图中心点
            layers:layers,
        });
        deviceService.findAllAndFlag().success(function (response) {
            $scope.deviceList = response;
            $scope.addMarker1();
        });



        //查询初始数据
        $scope.findNewAlarm10Size();
        $scope.findNewApply10Size();
        $scope.findAllRegionAndSubRegionAndDevice();

        //获取共享数据
        $scope.sessionId = sessionService.getSessionId();
    }

   $scope.moveCameraTo = function(newLongitude, newLatitude) {
        var gps = [newLongitude, newLatitude];
        //参数说明：需要转换的坐标，需要转换的坐标类型，转换成功后的回调函数
        $scope.AMap.convertFrom(gps, "gps", function (status, result) {
           //status：complete 表示查询成功，no_data 为查询无结果，error 代表查询错误
           //查询成功时，result.locations 即为转换后的高德坐标系
           if (status === "complete" && result.info === "ok") {
               //var lnglats = result.locations[0]; //转换后的高德坐标 Array.<LngLat>
               $scope.map.setZoomAndCenter(18, [result.locations[0].lng,result.locations[0].lat]);
           }
       });

    };

    $scope.moveCameraTo1 = function(newLongitude, newLatitude) {
        var gps = [newLongitude, newLatitude];
        $scope.map.setZoomAndCenter(14, [newLongitude,newLatitude]);


    };


    $scope.addMarker = async function () {
        // 封装 AMap.convertFrom 为 Promise，支持批量转换
        function convertCoordinatesBatch(gpsList) {
            return new Promise((resolve, reject) => {
                // 将多个坐标用 | 分隔
                const locations = gpsList.map(gps => gps.join(',')).join('|');
                $scope.AMap.convertFrom(locations, "gps", function (status, result) {
                    if (status === "complete" && result.info === "ok") {
                        resolve(result.locations); // 返回转换后的坐标数组
                    } else {
                        reject(new Error('坐标转换失败'));
                    }
                });
            });
        }

        // 延迟函数
        function sleep(ms) {
            return new Promise(resolve => setTimeout(resolve, ms));
        }

        // 每批处理的设备数量
        const batchSize = 40; // 高德地图 API 单次请求最多支持 40 个坐标
        for (let i = 0; i < $scope.deviceList.length; i += batchSize) {
            const batch = $scope.deviceList.slice(i, i + batchSize); // 获取当前批次的设备
            const gpsList = batch.map(device => [device.longitude, device.dimension]); // 提取 GPS 坐标

            try {
                // 批量转换坐标
                const convertedPoints = await convertCoordinatesBatch(gpsList);

                // 遍历当前批次的设备
                for (let j = 0; j < batch.length; j++) {
                    const device = batch[j];
                    const point1 = convertedPoints[j]; // 获取转换后的坐标
                    const lng = point1.lng;
                    const lat = point1.lat;
                    const deviceId = device.deviceId;
                    const serverId = device.serverId;
                    const name = device.deviceName;
                    const deviceType = device.deviceType;
                    const flag = device.flag;

                    // 根据设备类型设置图标
                    let icon_url = "";
                    if (deviceType === "0") {
                        icon_url = "../static/images/speed_";
                    } else if (deviceType === "1") {
                        icon_url = "../static/images/xsp_";
                    }else if (deviceType === "2"){
                        icon_url = "../static/images/jsd_";
                    }else if (deviceType === "3") {
                        icon_url = "../static/images/ydd_";
                    } else if(deviceType === "4"){
                        icon_url = "../static/images/xhd_";
                    }else {
                        icon_url = "../static/images/unknown_";
                    }

                    // 根据 flag 设置图标颜色
                    if (flag === "false") {
                        icon_url = icon_url + "red.png"; // 有报警或申请
                    } else {
                        icon_url = icon_url + "blue.png"; // 正常状态
                    }

                    // 创建标记
                    const marker1 = new AMap.Marker({
                        position: [lng, lat],
                        offset: new AMap.Pixel(-10, -10),
                        icon: icon_url,
                        title: deviceId + "--" + name,
                    });

                    // 添加点击事件
                    marker1.on("click", function (e) {
                        const url = 'device.html?sessionId=' + $scope.sessionId + '&deviceId=' + deviceId + '&serverId=' + serverId;
                        window.location.href = url;
                    });

                    // 将标记添加到地图
                    $scope.map.add(marker1);
                    $scope.markerList.push(marker1);
                }
            } catch (error) {
                console.error(`批次 ${i / batchSize + 1} 坐标转换失败:`, error);
            }

            // 每次调用后延迟 150 毫秒
            await sleep(500);
        }
    };
    $scope.addMarker1 = async function () {
        // 封装 AMap.convertFrom 为 Promise，支持批量转换

        // 每批处理的设备数量
       // const batchSize = 40; // 高德地图 API 单次请求最多支持 40 个坐标
        for (let i = 0; i < $scope.deviceList.length; i++) {

            try {
                const device = $scope.deviceList[i];
                const lng = device.longitude;//point1.lng;
                const lat = device.dimension;
                const deviceId = device.deviceId;
                const serverId = device.serverId;
                const name = device.deviceName;
                const deviceType = device.deviceType;
                const flag = device.flag;

                // 根据设备类型设置图标
                let icon_url = "";
                if (deviceType === "0") {
                    icon_url = "../static/images/speed_";
                } else if (deviceType === "1") {
                    icon_url = "../static/images/xsp_";
                }else if (deviceType === "2"){
                    icon_url = "../static/images/jsd_";
                }else if (deviceType === "3") {
                    icon_url = "../static/images/ydd_";
                } else if(deviceType === "4"){
                    icon_url = "../static/images/xhd_";
                }else {
                    icon_url = "../static/images/unknown_";
                }

                // 根据 flag 设置图标颜色
                if (flag === "false") {
                    icon_url = icon_url + "red.png"; // 有报警或申请
                } else {
                    icon_url = icon_url + "blue.png"; // 正常状态
                }

                // 创建标记
                const marker1 = new AMap.Marker({
                    position: [lng, lat],
                    offset: new AMap.Pixel(-10, -10),
                    icon: icon_url,
                    title: deviceId + "--" + name,
                });

                // 添加点击事件
                marker1.on("click", function (e) {
                    const url = 'device.html?sessionId=' + $scope.sessionId + '&deviceId=' + deviceId + '&serverId=' + serverId;
                    window.location.href = url;
                });

                // 将标记添加到地图
                $scope.map.add(marker1);
                $scope.markerList.push(marker1);
            } catch (error) {
                console.error(`批次 ${i / batchSize + 1} 坐标转换失败:`, error);
            }

        }
    };

    let infoWindow = null;


    // 打开点的信息窗口
    $scope.openInfoWindow = function(device) {
        // 关闭之前的弹窗
        if (infoWindow) {
            infoWindow.close();
        }

        // 创建信息窗口内容 - 带底部指向的暗色系版本
        const content = `
            <div style="
                position: relative;
                width: auto;
                margin-bottom: 10px;
            ">
            <!-- 弹窗主体 -->
            <div style="
            text-align: center;
                padding: 12px 14px;
                border-radius: 8px;
                background: rgba(26, 35, 44, 0.1);
                backdrop-filter: blur(8px);
                border: 1px solid rgba(255, 255, 255, 0.3);
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
                color: #f1f5f9;
                font-family: system-ui, -apple-system, sans-serif;
                font-size: 13px;
                line-height: 1.4;
                position: relative;
                animation: fadeInUp 0.25s ease-out forwards;
            ">
                
                <h6 style="
                    margin: 2px 0 6px 0;
                    font-size: 13px;
                    font-weight: 400;
                    /*color: #e5d9e2;*/
                    color: #0a0e14;
                    line-height: 1.2;
                ">
                    ${device.server.serverName}
                </h6>
                
                <p style="
                    margin: 0;
                    font-size: 12px;
                    font-weight: 400;
                    /*color: #d5d9e2;*/
                    color: #0a0e14;
                ">
                    ${device.deviceName}
                </p>
            </div>
            
            <!-- 主箭头 - 渐变效果 - 只改变指向为向下 -->
            <div style="
                position: absolute;
                bottom: -10px;
                left: 50%;
                transform: translateX(-50%);
                width: 16px;
                height: 10px;
                background: linear-gradient(to top,
                    rgba(30, 41, 59, 0.95) 0%, 
                    rgba(59, 130, 246, 0.95) 50%,
                    rgba(30, 41, 59, 0.95) 100%
                );
                clip-path: polygon(0% 0%, 100% 0%, 50% 100%);
                filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
                animation: arrowGlow 2s ease-in-out infinite;
            "></div>
            
            <!-- 箭头发光边框 -->
            <div style="
                position: absolute;
                bottom: -12px;
                left: 50%;
                transform: translateX(-50%);
                width: 18px;
                height: 12px;
                background: linear-gradient(to top,
                    rgba(59, 130, 246, 0.3) 0%,
                    rgba(139, 92, 246, 0.3) 100%
                );
                clip-path: polygon(0% 0%, 100% 0%, 50% 100%);
                z-index: -1;
            "></div>
        </div>
        
        <style>
        @keyframes fadeInUp {
            0% {
                opacity: 0;
                transform: translateY(5px);
            }
            100% {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        @keyframes arrowGlow {
            0%, 100% {
                background: linear-gradient(to top,
                    rgba(30, 41, 59, 0.95) 0%, 
                    rgba(59, 130, 246, 0.8) 50%,
                    rgba(30, 41, 59, 0.95) 100%
                );
            }
            50% {
                background: linear-gradient(to top,
                    rgba(30, 41, 59, 0.95) 0%, 
                    rgba(99, 179, 237, 0.9) 50%,
                    rgba(30, 41, 59, 0.95) 100%
                );
            }
        }
        </style>
        `;


        // 创建信息窗口
        infoWindow = new AMap.InfoWindow({
            content: content,
            offset: new AMap.Pixel(5, -30),
            showShadow: true,
            isCustom: true  // 使用自定义弹窗
        });

        // 打开弹窗
        //使用转换后的坐标
        for (let i = 0; i < $scope.deviceList.length; i++) {
            if($scope.deviceList[i].deviceId === device.deviceId && $scope.deviceList[i].serverId === device.serverId){
                /*console.log($scope.markerList[i].position);
                console.log($scope.markerList[i]._position);
                console.log($scope.markerList[i]._position[0]);
                console.log($scope.markerList[i]._position[1]);*/
                infoWindow.open($scope.map, [$scope.markerList[i]._position[0],$scope.markerList[i]._position[1]]);
            }
        }


        // 居中显示
        //map.setCenter([point.lng, point.lat]);
    }

});