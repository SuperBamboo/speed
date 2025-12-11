package com.shengxuan.speed.socket;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.shengxuan.speed.entity.*;
import com.shengxuan.speed.entity.pojo.*;
import com.shengxuan.speed.mapper.*;
import com.shengxuan.speed.util.DateFormat;
import com.shengxuan.speed.util.callback.CallbackListener;
import com.shengxuan.speed.util.tostring.*;
import com.shengxuan.speed.util.toxml.StringToXML;
import com.shengxuan.speed.websocket.MyWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Yunzhu_Chen
 */

public class Client extends Thread implements Serializable {

    private MyWebSocketHandler webSocketHandler;
    private DeviceMapper deviceMapper;
    private RegionDeviceTypeMapper regionDeviceTypeMapper;
    private WarningToneMapper warningToneMapper;
    private DisplayMapper displayMapper;
    private AlarmMapper alarmMapper;
    private DeviceCtrlModeMapper deviceCtrlModeMapper;
    private PlanParamValueMapper planParamValueMapper;
    private PlanParamMapper planParamMapper;
    private PlanModeMapper planModeMapper;
    private ParameterMapper parameterMapper;
    private PortMapper portMapper;
    private PushAlarmMapper pushAlarmMapper;
    private RegionMapper regionMapper;
    private DeviceControlModeApplyMapper deviceControlModeApplyMapper;
    private DevicePlanModeApplyMapper devicePlanModeApplyMapper;
    private PlanParamApplyMapper planParamApplyMapper;


    private UserRegionMapper userRegionMapper;

    List<LocationInfo> locationInfoList = new ArrayList<>();

    public  Socket socket = null;
    public boolean isLogin = false;
    BufferedReader br;
    InputStream s;


    ArrayList<PushAlarm> alarmArrayList = new ArrayList<>();
    private String username;
    private String password;
    public String code;
    private String fromInstance = "";
    private String toInstance = "";
    private boolean isStopTread = false;

    private String mip;
    private int mport;

    //volatile 修饰的变量属于对象而非类
    private volatile List<DeviceReceiver> deviceReceiverList = new ArrayList<>();

    private boolean isAcceptHeartBeat; //是否在90s内收到服务器信条

    //区域对象集合
    public static List<Region> regionList = new ArrayList<>();

    private ArrayList<String> deviceIdList = new ArrayList<>();

    private CallbackListener callbackListener;

    public CallbackListener getCallbackListener() {
        return callbackListener;
    }

    public void setCallbackListener(CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    public Socket getSocket() {
        return socket;
    }


    public ArrayList<PushAlarm> getAlarmArrayList() {
        return alarmArrayList;
    }


    //private static List<MyQueue> myQueueList = new ArrayList<>();
    private List<MyQueue> myQueueList = new ArrayList<>();

    public List<MyQueue> getMyQueueList() {
        return myQueueList;
    }

    public void setMyQueueList(List<MyQueue> myQueueList) {
        this.myQueueList = myQueueList;
    }

    private int serverId;

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public boolean isStopTread() {
        return isStopTread;
    }

    public void setStopTread(boolean stopTread) {
        isStopTread = stopTread;
    }

    public Client(int serverId, String host, int port, String username, String password, DeviceMapper deviceMapper, WarningToneMapper warningToneMapper, DisplayMapper displayMapper, AlarmMapper alarmMapper, DeviceCtrlModeMapper deviceCtrlModeMapper, PlanParamValueMapper planParamValueMapper, PlanParamMapper planParamMapper, PlanModeMapper planModeMapper, ParameterMapper parameterMapper, PortMapper portMapper, PushAlarmMapper pushAlarmMapper, RegionMapper regionMapper, DeviceControlModeApplyMapper deviceControlModeApplyMapper, DevicePlanModeApplyMapper devicePlanModeApplyMapper, PlanParamApplyMapper planParamApplyMapper, RegionDeviceTypeMapper regionDeviceTypeMapper, UserRegionMapper userRegionMapper, WebSocketHandler webSocketHandler) {
        this.userRegionMapper = userRegionMapper;
        this.serverId = serverId;
        this.deviceMapper = deviceMapper;
        this.warningToneMapper = warningToneMapper;
        this.displayMapper = displayMapper;
        this.alarmMapper = alarmMapper;
        this.deviceCtrlModeMapper = deviceCtrlModeMapper;
        this.planParamValueMapper = planParamValueMapper;
        this.planParamMapper = planParamMapper;
        this.planModeMapper = planModeMapper;
        this.parameterMapper = parameterMapper;
        this.portMapper = portMapper;
        this.pushAlarmMapper = pushAlarmMapper;
        this.regionMapper = regionMapper;
        this.deviceControlModeApplyMapper = deviceControlModeApplyMapper;
        this.devicePlanModeApplyMapper = devicePlanModeApplyMapper;
        this.planParamApplyMapper = planParamApplyMapper;
        this.regionDeviceTypeMapper = regionDeviceTypeMapper;
        this.webSocketHandler = (MyWebSocketHandler) webSocketHandler;

        try {
            //需要服务器的IP地址和端口号，才能获得正确的Socket对象
            /*socket = new Socket();

            InetAddress address = InetAddress.getByName(host);
            // 设置地址重用选项
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(40001+serverId));  //绑定固定端口
            socket.connect(new InetSocketAddress(address,port));*/
            socket = new Socket(host, port);
            mip = host;
            mport = port;
            socket.setSoTimeout(1000*40);
            this.username = username;
            this.password = password;
        } catch (Exception e) {
            System.out.println("----------异常定位开始----------");
            e.printStackTrace();
            System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
            System.out.println("----------异常定位结束----------");
            //System.out.println("登陆错误");
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    private HeartBeatThread mHeartBeatThread;
    private TimeCount mTimeCount;

    public TimeCount getmTimeCount() {
        return mTimeCount;
    }

    public HeartBeatThread getmHeartBeatThread() {
        return mHeartBeatThread;
    }

    @Override
    public void run() {
        //发送登录xml报文
        login();
        //postHandle.postDelayed(mRunnble,30*1000);//这又是啥意思（）30s后启动线程

        mHeartBeatThread = new HeartBeatThread();  //启动心跳包线程
        mHeartBeatThread.start();
        mTimeCount = new TimeCount();   //启动检测心跳接受线程
        mTimeCount.start();


        try {
            // 读取服务器发来的数据

            s = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(s, "gbk"));

            StringBuffer message = new StringBuffer();//定义存储一次完整的数据包

            String line = "";
            while (true) {
                try {
                    line = br.readLine();
                } catch (Exception e) {
                    System.out.println("----------异常定位开始----------");
                    e.printStackTrace();
                    System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
                    System.out.println("----------异常定位结束----------");
                    //清空缓冲字符串
                    message.setLength(0);
                    //System.out.println("-------------->运维平台读取行捕捉到错误------重新初始化socket");
                    //等待100s
                    //关闭心跳线程
                    mHeartBeatThread.exitHeart = true;
                    sleep(25 * 1000);
                    try {
                        if(socket!=null){
                            socket.close();
                        }
                        socket = new Socket(mip,mport);
                        /*socket = new Socket();
                        InetAddress address = InetAddress.getByName(mip);
                        // 设置地址重用选项
                        socket.setReuseAddress(true);
                        socket.bind(new InetSocketAddress(40001+serverId));  //绑定固定端口
                        socket.connect(new InetSocketAddress(address,mport));*/
                        s = socket.getInputStream();
                        br = new BufferedReader(new InputStreamReader(s, "gbk"));
                        login();
                        mHeartBeatThread = new HeartBeatThread();
                        mHeartBeatThread.start();
                    } catch (Exception e1) {
                        //System.out.println("------>运维平台再次链接失败");
                        System.out.println("----------异常定位开始----------");
                        e.printStackTrace();
                        System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
                        System.out.println("----------异常定位结束----------");
                    }


                }

                if (isStopTread) {
                    return;
                }
                if (line != null) {

                    message.append(line);
                    if(line.equals("</Message>")){
                        //此时的message为一次完整的数据包
                        String msg = message.toString();
                        //判断此书举报是否是完整的一个数据包
                        //String msg1 = CountString.count(msg, "<Message>");
                        //System.out.println("打印服务器发来的数据包" + msg);
                        message.setLength(0);//清空message重新接受完整数据颗星
                        dataParse(msg);
                    }
                    line = null;
                }
                /*if (line != null && line.equals("</Message>")) {
                    //此时的message为一次完整的数据包
                    String msg = message.toString();
                    //判断此书举报是否是完整的一个数据包
                    //String msg1 = CountString.count(msg, "<Message>");
                    //System.out.println("打印服务器发来的数据包" + msg);
                    message.setLength(0);//清空message重新接受完整数据颗星
                    dataParse(msg);
                }*/
               // System.out.println("打印服务器返回的每一行:---" + line);
            }

        } catch (Exception e) {
            System.out.println("----------异常定位开始----------");
            e.printStackTrace();
            System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
            System.out.println("----------异常定位结束----------");
        }
    }


    /**
     * 数据解析
     */
    private void dataParse(final String msg) {
        new ThreadTask<String>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public String onDoInBackground() {

                //登录失败
                if (msg.contains("<SDO_Error>")) {
                    //提示用户 用户名 或 密码 错误
                    return "";
                }

                //登陆成功
                if (msg.contains("<SDO_User>")) {
                    isLogin = true;
                    //登录成功
                    //解析这个数据包
                    int a = 0;      //服务器返回的数据中instance节点数据数组中的下标
                    int b = 0;
                    ArrayList<XMLNodeAndAttr> xmlList = new StringToXML(msg).getXMLList();

                    for (int i = 0; i < xmlList.size(); i++) {
                        if (xmlList.get(i).getNodeName().equals("Instance")) {
                            if (a == 0) {
                                a = i;
                            } else {
                                b = i;
                            }
                        }
                    }
                    fromInstance = xmlList.get(a).getNodeValue();
                    toInstance = xmlList.get(b).getNodeValue();
                    //查看数据库是否有这个表数据
                    //if (deviceList == null || deviceList.size() <= 0) {
                    sendSysInfo();
                    try {
                        webSocketHandler.handleMessage(null,new TextMessage("登陆成功"));
                        System.out.println("serverId: "+serverId+ " 在 "+DateFormat.getSeq()+ " 登录成功");
                    } catch (Exception e) {
                        //throw new RuntimeException(e);
                        e.printStackTrace();
                    }
                }

                //操作服务器发来的心跳信息
                if (msg.contains("<SDO_HeartBeat/>") && msg.contains("<Type>PUSH</Type>")) {
                    //这个数据包是服务器发来的心跳信息
                    isAcceptHeartBeat = true;
                }

                //操作查询到的报警信息
                if (msg.contains("AlarmInfo") && msg.contains("<Type>RESPONSE</Type>")) {
                    //此时这个数据包是响应查询的报警信息
                    ArrayList<XMLNodeAndAttr> xmlList = new StringToXML(msg).getXMLList();
                    QueryAlarm queryAlarm = new QueryAlarm();
                    List<QueryAlarm> queryAlarmList = new ArrayList<>();
                    for (XMLNodeAndAttr xmlNodeAndAttr : xmlList) {
                        if (xmlNodeAndAttr.getNodeName().equals("AlarmNo")) {
                            queryAlarm.setAlarmno(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DeviceID")) {
                            queryAlarm.setDeviceid(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DeviceName")) {
                            queryAlarm.setDevicename(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("OfRegionName")) {
                            queryAlarm.setOfregionname(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("OfSubRegionName")) {
                            queryAlarm.setOfsubregionname(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DeviceModel")) {
                            queryAlarm.setDevicemodel(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("Supplier")) {
                            queryAlarm.setSupplier(xmlNodeAndAttr.getNodeName());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("Longitude")) {
                            queryAlarm.setLongitude(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("Dimension")) {
                            queryAlarm.setDimension(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("OccurDate")) {
                            queryAlarm.setOccurdate(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("OccurTime")) {
                            if (xmlNodeAndAttr.getNodeValue().length() == 5) {
                                queryAlarm.setOccurtime("0" + xmlNodeAndAttr.getNodeValue());
                            } else {
                                queryAlarm.setOccurtime(xmlNodeAndAttr.getNodeValue());
                            }

                        }
                        if (xmlNodeAndAttr.getNodeName().equals("AlarmType")) {
                            queryAlarm.setAlarmtype(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("AlarmDesc")) {
                            queryAlarm.setAlarmdesc(xmlNodeAndAttr.getNodeValue());

                            //此时一个完整的报警信息已经设置完毕 保存数据
                            queryAlarmList.add(queryAlarm);
                            queryAlarm = new QueryAlarm();
                        }
                    }
                }

                //操作服务器推送的报警信息
                if (msg.contains("AlarmInfo") && msg.contains("<Type>PUSH</Type>")) {
                    //此时这个数据包是主动推送的报警信息(存入数据库)
                    //解析该报警xml
                    ArrayList<XMLNodeAndAttr> xmlList = new StringToXML(msg).getXMLList();
                    PushAlarm pushAlarm = new PushAlarm();
                    String alarmNo = "";    //推送报警信息的编号
                    for (XMLNodeAndAttr xmlNodeAndAttr : xmlList) {
                        if (xmlNodeAndAttr.getNodeName().equals("AlarmNo")) {
                            alarmNo = xmlNodeAndAttr.getNodeValue();
                            pushAlarm.setAlarmNo(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DeviceID")) {
                            pushAlarm.setDeviceId(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DeviceName")) {
                            pushAlarm.setDeviceName(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("OfRegionName")) {
                            pushAlarm.setOfRegionName(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("OfSubRegionName")) {
                            pushAlarm.setOfSubregionName(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DeviceModel")) {
                            pushAlarm.setDeviceModel(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("Supplier")) {
                            pushAlarm.setSupplier(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("Longitude")) {
                            pushAlarm.setLongitude(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("Dimension")) {
                            pushAlarm.setDimension(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("OccurDate")) {
                            pushAlarm.setOccurDate(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("OccurTime")) {
                            pushAlarm.setOccurTime(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("AlarmType")) {
                            pushAlarm.setAlarmType(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("AlarmDesc")) {
                            pushAlarm.setAlarmDesc(xmlNodeAndAttr.getNodeValue());
                        }
                    }

                    //存储在数据库中
                    pushAlarm.setServerId(serverId);
                    pushAlarmMapper.add(pushAlarm);
                    //pushAlarm.save();

                    //根据报警信息跟新数据库
                    Device device = deviceMapper.findByDeviceId(pushAlarm.getDeviceId(),serverId);
                    if(device!=null){
                        //数据库有相关设备信息
                    }else {
                        sendDevice(pushAlarm.getDeviceId());
                    }

                    try {
                        webSocketHandler.broadcastMessage(serverId,pushAlarm.getDeviceId()+" "+pushAlarm.getDeviceName()+" "+pushAlarm.getAlarmDesc());
                    } catch (Exception e) {
                        System.out.println("----------异常定位开始----------");
                        e.printStackTrace();
                        System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
                        System.out.println("----------异常定位结束----------");
                        //throw new RuntimeException(e);
                    }

                }
                //操作响应的系统信息
                if (msg.contains("<SysInfo>")) {
                    //此时这个数据包是系统对象数据包(开始解析系统信息)
                    ArrayList<XMLNodeAndAttr> xmlList = new StringToXML(msg).getXMLList();
                    doGetList(xmlList);
                }

                //操作响应的子区对象
                if (msg.contains("<RegionParam>") && msg.contains("<SubRegionIDList>")) {
                    //清空区域集合数组
                }

                //操作相应的设备信息
                if (msg.contains("<DeviceParam>")) {
                    //这个数据包是设备信息数据包
                    ArrayList<XMLNodeAndAttr> xmlList = new StringToXML(msg).getXMLList();

                    String deviceId = null;
                    Device device = new Device();
                    RegionDeviceType regionDeviceType = new RegionDeviceType();

                    Parameter parameter = new Parameter();
                    Port port = new Port();
                    WarningTone warningTone = new WarningTone();
                    Display display = new Display();
                    Alarm alarm = new Alarm();
                    DeviceCtrlMode deviceCtrlMode = new DeviceCtrlMode();
                    PlanMode planMode = new PlanMode();
                    PlanParam planParam = new PlanParam();
                    PlanParamValue planParamValue = new PlanParamValue();

                    String planModeNo = null;
                    String planParamNo = null;

                    for (XMLNodeAndAttr xmlNodeAndAttr : xmlList) {
                        if (xmlNodeAndAttr.getNodeName().equals("DeviceID")) {
                            device.setDeviceId(xmlNodeAndAttr.getNodeValue());
                            deviceId = xmlNodeAndAttr.getNodeValue();
                            //List<Device> device111 = DataSupport.where("deviceid = ? ", deviceId).find(Device.class);
                            Device device111 = deviceMapper.findByDeviceId(deviceId,serverId);
                            if (device111 != null) {
                                //当前数据库中已有这个设备删除之
                                deviceMapper.del(deviceId,serverId);

                                //删除参数列表中相关行
                                parameterMapper.delByDeviceId(deviceId,serverId);
                                portMapper.delByDeviceId(deviceId,serverId);
                                warningToneMapper.delByDeviceId(deviceId,serverId);
                                displayMapper.delByDeviceId(deviceId,serverId);
                                alarmMapper.delByDeviceId(deviceId,serverId);
                                deviceCtrlModeMapper.delByDeviceId(deviceId,serverId);
                                planParamValueMapper.delByDeviceId(deviceId,serverId);
                                planParamMapper.delByDeviceId(deviceId,serverId);
                                planModeMapper.delByDeviceId(deviceId,serverId);

                            }
                            device111 = null;
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DeviceName")) {
                            device.setDeviceName(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("OfRegionID")) {
                            device.setOfRegionId(xmlNodeAndAttr.getNodeValue());
                            regionDeviceType.setRegion(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("OfSubRegionID")) {
                            device.setOfSubRegionId(xmlNodeAndAttr.getNodeValue());
                            regionDeviceType.setSubregion(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DeviceType")) {
                            device.setDeviceType(xmlNodeAndAttr.getNodeValue());
                            regionDeviceType.setDeviceType(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DeviceModel")) {
                            device.setDeviceModel(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DeviceAttribute")) {
                            device.setDeviceAttribute(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("Supplier")) {
                            device.setSupplier(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("ServiceDate")) {
                                device.setServiceDate(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("Longitude")) {
                            device.setLongitude(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("Dimension")) {
                            device.setDimension(xmlNodeAndAttr.getNodeValue());
                        }

                        //参数列表解析
                        if (xmlNodeAndAttr.getNodeName().equals("ParameterNo")) {
                            parameter.setDeviceId(deviceId);
                            parameter.setParameterNo(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("ParameterName")) {
                            parameter.setParameterName(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("ParameterType")) {
                            parameter.setParameterType(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("ParameterValue")) {
                            parameter.setParameterValue(xmlNodeAndAttr.getNodeValue());
                            //到此存储一个参数结束
                            //parameter.save();
                            parameter.setServerId(serverId);
                            parameterMapper.add(parameter);
                            parameter = null;
                            parameter = new Parameter();
                        }

                        //端口列表解析
                        if (xmlNodeAndAttr.getNodeName().equals("PortNo")) {
                            //此设备列表中包含端口列表
                            port.setDeviceId(deviceId);
                            port.setPortNo(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("PortName")) {
                            //此设备列表中包含端口列表
                            port.setPortName(xmlNodeAndAttr.getNodeValue());
                            port.setServerId(serverId);
                            //port.save();
                            portMapper.add(port);
                            //到此一个端口数据解析完成
                            port = null;
                            port = new Port();
                        }

                        //WarningTone列表解析
                        if (xmlNodeAndAttr.getNodeName().equals("WarningToneNo")) {
                            if(deviceCtrlMode.getDeviceId() == null){
                                warningTone.setDeviceId(deviceId);
                                warningTone.setWarningToneNo(xmlNodeAndAttr.getNodeValue());
                            }
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("WarningToneName")) {
                            warningTone.setWarningToneName(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("WarningLen")) {
                            warningTone.setWarningToneLen(xmlNodeAndAttr.getNodeValue());
                            warningTone.setServerId(serverId);
                            warningToneMapper.add(warningTone);
                            //到此一个WarningTone对象存储完成
                            warningTone = null;
                            warningTone = new WarningTone();
                        }

                        //Display列表解析
                        if (xmlNodeAndAttr.getNodeName().equals("DisplayNo") && xmlNodeAndAttr.getAttrName() == null) {
                            display.setDeviceId(deviceId);
                            display.setDisplayNo(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DisplayName")) {
                            display.setDisplayName(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DisplayColor")) {
                            display.setDisplayColor(xmlNodeAndAttr.getNodeValue());
                            display.setServerId(serverId);
                            displayMapper.add(display);
                            //到此一个WarningTone对象存储完成
                            display = null;
                            display = new Display();
                        }

                        //Alarm列表解析
                        if (xmlNodeAndAttr.getNodeName().equals("AlarmNo")) {
                            alarm.setDeviceId(deviceId);
                            alarm.setAlarmNo(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("AlarmName")) {
                            alarm.setAlarmName(xmlNodeAndAttr.getNodeValue());
                            alarm.setServerId(serverId);
                            alarmMapper.add(alarm);
                            alarm = null;
                            alarm = new Alarm();
                        }

                        //DeviceCtrlMode列表解析
                        if (xmlNodeAndAttr.getNodeName().equals("DeviceCtrlModeNo")) {
                            deviceCtrlMode.setDeviceId(deviceId);
                            deviceCtrlMode.setDeviceCtrlModeNo(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DeviceCtrlModeName")) {
                            deviceCtrlMode.setDeviceCtrlModeName(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("RelayStau")) {
                            if("validity".equals(xmlNodeAndAttr.getAttrName())){
                                deviceCtrlMode.setRelayStauValidity(xmlNodeAndAttr.getAttrValue());
                            }
                            deviceCtrlMode.setRelayStau(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("RelayStau1")) {
                            if("validity".equals(xmlNodeAndAttr.getAttrName())){
                                deviceCtrlMode.setRelayStau1Validity(xmlNodeAndAttr.getAttrValue());
                            }
                            deviceCtrlMode.setRelayStau1(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DisplayNo") && "validity".equals(xmlNodeAndAttr.getAttrName())) {
                            deviceCtrlMode.setDisplayNo(xmlNodeAndAttr.getNodeValue());
                            deviceCtrlMode.setDisplayNoValidity(xmlNodeAndAttr.getAttrValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("WarningToneNo")) {
                            if(deviceCtrlMode.getDeviceId()!=null){
                                deviceCtrlMode.setWarningToneNo(xmlNodeAndAttr.getNodeValue());
                                deviceCtrlMode.setWarningToneNoValidity(xmlNodeAndAttr.getAttrValue());
                            }
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("Volume")){
                            deviceCtrlMode.setVolume(xmlNodeAndAttr.getNodeValue());
                            deviceCtrlMode.setVolumeValidity(xmlNodeAndAttr.getAttrValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("PlayInteval")) {
                            deviceCtrlMode.setPlayInteval(xmlNodeAndAttr.getNodeValue());
                            deviceCtrlMode.setPlayIntevalValidity(xmlNodeAndAttr.getAttrValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("PlayNumbers")) {
                            deviceCtrlMode.setPlayNumbers(xmlNodeAndAttr.getNodeValue());
                            deviceCtrlMode.setPlayNumbersValidity(xmlNodeAndAttr.getAttrValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("HoldTime")) {
                            if(planMode.getDeviceId() == null){
                                deviceCtrlMode.setHoldTime(xmlNodeAndAttr.getNodeValue());
                                deviceCtrlMode.setHoldTimeValidity(xmlNodeAndAttr.getAttrValue());
                                deviceCtrlMode.setServerId(serverId);
                                deviceCtrlModeMapper.add(deviceCtrlMode);
                                deviceCtrlMode = null;
                                deviceCtrlMode = new DeviceCtrlMode();
                            }
                        }

                        //PlanMode列表解析
                        if (xmlNodeAndAttr.getNodeName().equals("PlanModeNo")) {
                            planMode.setDeviceId(deviceId);
                            planMode.setPlanModeNo(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("PlanModeName")) {
                            planMode.setPlanModeName(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("HoldTime")) {
                            if(planMode.getDeviceId()!=null){
                                planMode.setHoldTime(xmlNodeAndAttr.getNodeValue());
                            }
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("Repetitions")) {
                            planMode.setRepetitions(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("IntervalTime")) {
                            planMode.setIntervalTime(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("SysTime")) {
                            planMode.setSysTime(xmlNodeAndAttr.getNodeValue());
                            planMode.setServerId(serverId);
                            planModeMapper.add(planMode);
                            planModeNo = planMode.getPlanModeNo();
                            planMode = null;
                            planMode = new PlanMode();
                        }

                        //PlanParam
                        if (xmlNodeAndAttr.getNodeName().equals("PlanParamNo")) {
                            planParam.setDeviceId(deviceId);
                            //如何获取到上一次存储的
                            planParam.setPlanModeNo(planModeNo);
                            planParam.setPlanParamNo(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("PlanParamName")) {
                            planParam.setPlanParamName(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DefaultValue")) {
                            planParam.setDefaultValue(xmlNodeAndAttr.getNodeValue());
                            planParam.setServerId(serverId);
                            planParamMapper.add(planParam);
                            planParamNo = planParam.getPlanParamNo();
                            planParam = null;
                            planParam = new PlanParam();
                        }

                        //PlanParamValue
                        if (xmlNodeAndAttr.getNodeName().equals("PlanParamValueNo")) {
                            planParamValue.setDeviceId(deviceId);
                            planParamValue.setPlanModeNo(planModeNo);
                            planParamValue.setPlanParamNo(planParamNo);
                            planParamValue.setPlanParamValueNo(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("PlanParamValueName")) {
                            planParamValue.setPlanParamValueName(xmlNodeAndAttr.getNodeValue());
                            planParamValue.setServerId(serverId);
                            planParamValueMapper.add(planParamValue);
                            planParamValue = null;
                            planParamValue = new PlanParamValue();
                        }

                    }
                    //根据id查询这条记录，如果标示为false，则添加其余属性，跟新数据表
                    device.setFlag("true");
                    //存设备信息的时候就把坐标给转换了
                    device.setServerId(serverId);
                    deviceMapper.add(device);
                    //插入RegionDeviceType
                    regionDeviceType.setServerId(serverId);
                    List<RegionDeviceType> selectRegionDeviceType = regionDeviceTypeMapper.findByRegionDeviceType(regionDeviceType);
                    if(selectRegionDeviceType == null || selectRegionDeviceType.size()==0){
                        regionDeviceTypeMapper.add(regionDeviceType);
                    }
                    for (DeviceReceiver deviceReceiver : deviceReceiverList) {
                        if (deviceReceiver.getDeviceID().equals(device.getDeviceId())) {
                            deviceReceiver.setReceiver(true);
                        }
                    }
                    //主动垃圾回收
                    try{
                        xmlList = null;
                        deviceId = null;
                        device = null;
                        parameter = null;
                        port = null;
                        warningTone = null;
                        display = null;
                        alarm = null;
                        deviceCtrlMode = null;
                        planMode = null;
                        planParam = null;
                        planParamValue = null;
                        planModeNo = null;
                        planParamNo = null;
                        //System.gc();
                    }catch (Exception e){
                        System.out.println("----------异常定位开始----------");
                        e.printStackTrace();
                        System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
                        System.out.println("----------异常定位结束----------");
                    }

                }

                if (msg.contains("<FileInfo>") && msg.contains("<Type>RESPONSE</Type>")) {
                    //这个数据包是请求的文件返回
                    ArrayList<XMLNodeAndAttr> xmlList = new StringToXML(msg).getXMLList();
                    String fileName = "";
                    String fileLength = "";
                    StringBuilder fileContent = new StringBuilder();
                    for (XMLNodeAndAttr xmlNodeAndAttr : xmlList) {
                        if (xmlNodeAndAttr.getNodeName().equals("FileName")) {
                            fileName = xmlNodeAndAttr.getNodeValue();
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("FileLength")) {
                            fileLength = xmlNodeAndAttr.getNodeValue();
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("FileContent")) {
                            fileContent.append(xmlNodeAndAttr.getNodeValue());
                        }
                    }
                    //System.out.println("文件信息------>" + "fileName:" + fileName + " fileLength:" + fileLength + " fileContent:" + fileContent);

                }

                //这个数据包是请求查询设备状态的返回
                if (msg.contains("<DeviceStau>")&& msg.contains("<Type>RESPONSE</Type>")) {
                    ArrayList<XMLNodeAndAttr> xmlList = new StringToXML(msg).getXMLList();
                    String deviceID = "";
                    String deviceName = "";
                    String detectTime = "";
                    String ctrlMode = "";
                    String tempCtrl = "";
                    String tempPlan = "";
                    String seq = "";
                    DetectStau detectStau = new DetectStau();
                    DeviceStau deviceStau = new DeviceStau();
                    List<DetectStau> list = new ArrayList<>();
                    for (XMLNodeAndAttr xmlNodeAndAttr : xmlList) {
                        if(xmlNodeAndAttr.getNodeName().equals("Seq")){
                            seq = xmlNodeAndAttr.getNodeValue();
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("DeviceID")){
                            deviceID = xmlNodeAndAttr.getNodeValue();
                            deviceStau.setDeviceId(deviceID);
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("DeviceName")){
                            deviceName = xmlNodeAndAttr.getNodeValue();
                            deviceStau.setDeviceName(deviceName);
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DetectTime")) {
                            detectTime = xmlNodeAndAttr.getNodeValue();
                            deviceStau.setDetectTime(detectTime);
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("CtrlMode")) {
                            ctrlMode = xmlNodeAndAttr.getNodeValue();
                            deviceStau.setCtrlMode(ctrlMode);
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("TempCtrl")) {
                            tempCtrl = xmlNodeAndAttr.getNodeValue();
                            if(tempCtrl==null){
                                deviceStau.setTempCtrl("无");
                            }else if("0".equals(tempCtrl)){
                                deviceStau.setTempCtrl("未执行");
                            }else {
                                String nameByDeviceIdAndCtrlNo = deviceCtrlModeMapper.findNameByDeviceIdAndCtrlNo(deviceID, tempCtrl,serverId);
                                deviceStau.setTempCtrl(nameByDeviceIdAndCtrlNo);
                            }
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("TempPlan")) {
                            tempPlan = xmlNodeAndAttr.getNodeValue();
                            if(tempPlan == null){
                                deviceStau.setTempPlan("无");
                            } else if ("0".equals(tempPlan)) {
                                deviceStau.setTempPlan("未执行");
                            }else {
                                String nameByDeviceIdAndPlanNo = planModeMapper.findNameByDeviceIdAndPlanNo(deviceID, tempPlan,serverId);
                                deviceStau.setTempPlan(nameByDeviceIdAndPlanNo);
                            }
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DetectID")) {
                            if(detectStau.getDetectId()!=null){
                                //上一个结尾是没有AbnormalStau的需要加入集合
                                list.add(detectStau);
                                detectStau = new DetectStau();
                            }
                            detectStau.setDetectId(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DetectName")) {
                            detectStau.setDetectName(xmlNodeAndAttr.getNodeValue());
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("DetectValue")) {
                            detectStau.setDetectValue(xmlNodeAndAttr.getNodeValue());
                            //如果列表只有一个此处就应该存储
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("AbnormalStau")) {
                            detectStau.setAbnormalStau(xmlNodeAndAttr.getNodeValue());
                            list.add(detectStau);
                            detectStau = new DetectStau();
                        }
                    }

                    //判断对象参数是否为空，如果不为空则说明最后一个是不含Ab参数的没有保存
                    if(detectStau.getDetectId()!=null){
                        list.add(detectStau);
                    }
                    deviceStau.setDetectStauList(list);

                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        deviceStau.setServerId(serverId);
                        String result = objectMapper.writeValueAsString(deviceStau);
                        int index = -1;
                        for (int i = 0; i < myQueueList.size(); i++) {
                            if(myQueueList.get(i).getSeq().equals(seq)){
                                index = i;
                                webSocketHandler.broadcastMessageBySessionId(1,myQueueList.get(i).getSessionId(),result);
                                System.out.println("服务器返回"+deviceID+"设备状态");
                            }
                        }
                        //如果之前有删除
                        if(index>0){
                            for (int i = 0; i < index; i++) {
                                myQueueList.remove(0);
                            }
                        }
                    } catch (Exception e) {
                        //throw new RuntimeException(e);
                        e.printStackTrace();
                    }
                }

                if (msg.contains("<DeviceControlModeSet")) {
                    //请求控制返回数据包
                    ArrayList<XMLNodeAndAttr> xmlList = new StringToXML(msg).getXMLList();
                    boolean flag = false;
                    String message = "";
                    String seq = "";
                    for (XMLNodeAndAttr xmlNodeAndAttr : xmlList) {
                        if(xmlNodeAndAttr.getNodeName().equals("Seq")){
                            seq = xmlNodeAndAttr.getNodeValue();
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("Res")) {
                            if (xmlNodeAndAttr.getNodeValue().equals("1")) {
                                //设置成功
                                flag = true;
                            }
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("ErrorMsg")) {

                            message = xmlNodeAndAttr.getNodeValue();
                        }
                    }
                    //-----------------------------通知前台----------------------
                    if(Objects.equals(message, "")){
                        message = "设置手动管控成功!";
                    }
                    SetResponse setResponse = new SetResponse(flag,message);
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String result = objectMapper.writeValueAsString(setResponse);
                        int index = -1;
                        for (int i = 0; i < myQueueList.size(); i++) {
                            if(myQueueList.get(i).getSeq().equals(seq)){
                                index = i;
                                webSocketHandler.broadcastMessageBySessionId(2,myQueueList.get(i).getSessionId(),result);
                                //System.out.println("服务器返回设置返回"+flag);
                            }
                        }
                        //如果之前有删除
                        if(index>0){
                            for (int i = 0; i < index; i++) {
                                myQueueList.remove(0);
                            }
                        }
                    } catch (Exception e) {
                        //throw new RuntimeException(e);
                        e.printStackTrace();
                    }
                    //再次发送查询
                }

                if (msg.contains("<DevicePlanModeSet")) {
                    //请求控制返回数据包
                    ArrayList<XMLNodeAndAttr> xmlList = new StringToXML(msg).getXMLList();
                    boolean flag = false;
                    String message = "";
                    String seq = "";
                    for (XMLNodeAndAttr xmlNodeAndAttr : xmlList) {
                        if(xmlNodeAndAttr.getNodeName().equals("Seq")){
                            seq = xmlNodeAndAttr.getNodeValue();
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("Res")) {
                            if (xmlNodeAndAttr.getNodeValue().equals("1")) {
                                //设置成功
                                flag = true;
                            }
                        }
                        if (xmlNodeAndAttr.getNodeName().equals("ErrorMsg")) {

                            message = xmlNodeAndAttr.getNodeValue();
                        }
                    }

                    //--------------------------------------通知前台---------------------------------
                    if(Objects.equals(message, "")){
                        message = "设置程式控制成功!";
                    }
                    SetResponse setResponse = new SetResponse(flag,message);
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String result = objectMapper.writeValueAsString(setResponse);
                        int index = -1;
                        for (int i = 0; i < myQueueList.size(); i++) {
                            if(myQueueList.get(i).getSeq().equals(seq)){
                                index = i;
                                webSocketHandler.broadcastMessageBySessionId(2,myQueueList.get(i).getSessionId(),result);
                                //System.out.println("服务器返回设置返回"+flag);
                            }
                        }
                        //如果之前有删除
                        if(index>0){
                            for (int i = 0; i < index; i++) {
                                myQueueList.remove(0);
                            }
                        }
                    } catch (Exception e) {
                        //throw new RuntimeException(e);
                        e.printStackTrace();
                    }

                }

                if(msg.contains("<DeviceControlModeApply>")){
                    //临时手控申请
                    //1.存在数据库
                    String deviceID = "";
                    String ctrlNo = "";
                    DeviceControlModeApply deviceControlModeApply = new DeviceControlModeApply();
                    ArrayList<XMLNodeAndAttr> xmlList = new StringToXML(msg).getXMLList();
                    for (XMLNodeAndAttr xmlNodeAndAttr : xmlList) {
                        if(xmlNodeAndAttr.getNodeName().equals("DeviceID")){
                            deviceID = xmlNodeAndAttr.getNodeValue();
                            deviceControlModeApply.setDeviceId(deviceID);
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("Value")){
                            deviceControlModeApply.setValue(xmlNodeAndAttr.getNodeValue());
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("CtrlNo")){
                            ctrlNo = xmlNodeAndAttr.getNodeValue();
                            deviceControlModeApply.setCtrlNo(ctrlNo);
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("Relay1")){
                            deviceControlModeApply.setRelay1(xmlNodeAndAttr.getNodeValue());
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("Relay2")){
                            deviceControlModeApply.setRelay2(xmlNodeAndAttr.getNodeValue());
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("DisplayNo")){
                            deviceControlModeApply.setDisplayNo(xmlNodeAndAttr.getNodeValue());
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("WarningTone")){
                            deviceControlModeApply.setWarningTone(xmlNodeAndAttr.getNodeValue());
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("Volume")){
                            deviceControlModeApply.setVolume(xmlNodeAndAttr.getNodeValue());
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("PlayInteval")){
                            deviceControlModeApply.setPlayInteval(xmlNodeAndAttr.getNodeValue());
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("PlayNumbers")){
                            deviceControlModeApply.setPlayNumbers(xmlNodeAndAttr.getNodeValue());
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("HoldTime")){
                            deviceControlModeApply.setHoldTime(xmlNodeAndAttr.getNodeValue());
                            deviceControlModeApply.setApplyTime(DateFormat.dateNowFormat());
                            deviceControlModeApply.setServerId(serverId);
                            deviceControlModeApplyMapper.add(deviceControlModeApply);
                        }
                    }
                    //-------------------------------给前台通知收到手控申请-----------------------------
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String result = objectMapper.writeValueAsString(deviceControlModeApply);

                        webSocketHandler.broadcastMessage(serverId,"3==="+result);
                       // System.out.println("服务器推送了"+deviceID+"设备手控申请");
                    } catch (Exception e) {
                        //throw new RuntimeException(e);
                        e.printStackTrace();
                    }

                }
                if(msg.contains("<DevicePlanModeApply>")){
                    //临时手控申请
                    //1.存在数据库
                    String deviceID = "";
                    String planNo = "";
                    long id = -1;
                    PlanParamApply planParamApply = null;
                    DevicePlanModeApply devicePlanModeApply = new DevicePlanModeApply();
                    ArrayList<XMLNodeAndAttr> xmlList = new StringToXML(msg).getXMLList();
                    for (XMLNodeAndAttr xmlNodeAndAttr : xmlList) {
                        if(xmlNodeAndAttr.getNodeName().equals("DeviceID")){
                            deviceID = xmlNodeAndAttr.getNodeValue();
                            devicePlanModeApply.setDeviceId(deviceID);
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("Value")){
                            devicePlanModeApply.setValue(xmlNodeAndAttr.getNodeValue());
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("PlanNo")){
                            planNo = xmlNodeAndAttr.getNodeValue();
                            devicePlanModeApply.setPlanNo(planNo);
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("HoldTime")){
                            devicePlanModeApply.setHoldTime(xmlNodeAndAttr.getNodeValue());
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("IntervalTime")){
                            devicePlanModeApply.setIntervalTime(xmlNodeAndAttr.getNodeValue());
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("Repetitions")){
                            devicePlanModeApply.setRepetitions(xmlNodeAndAttr.getNodeValue());
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("SysTime")){
                            devicePlanModeApply.setSysTime(xmlNodeAndAttr.getNodeValue());
                            devicePlanModeApply.setApplyTime(DateFormat.dateNowFormat());
                            devicePlanModeApply.setServerId(serverId);
                            devicePlanModeApplyMapper.add(devicePlanModeApply);
                            id = devicePlanModeApply.getId();
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("PlanParamNo")){
                            planParamApply = new PlanParamApply();
                            planParamApply.setDeviceId(deviceID);
                            planParamApply.setPlanNo(planNo);
                            planParamApply.setDevicePlanModeApplyId(id);
                            planParamApply.setPlanParamNo(xmlNodeAndAttr.getNodeValue());
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("PlanParamValue")){
                            planParamApply.setPlanParamValue(xmlNodeAndAttr.getNodeValue());
                            planParamApply.setServerId(serverId);
                            planParamApplyMapper.add(planParamApply);
                        }
                    }
                    //----------------------------给主页面广播收到程控申请-----------------------
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String result = objectMapper.writeValueAsString(devicePlanModeApply);

                        webSocketHandler.broadcastMessage(serverId,"4==="+result);
                       // System.out.println("服务器推送了"+deviceID+"设备程控申请");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if(msg.contains("<LocationInfo>") && msg.contains("<Type>RESPONSE</Type>")){
                    //请求查询定位返回
                    ArrayList<XMLNodeAndAttr> xmlList = new StringToXML(msg).getXMLList();
                    int index = -1;
                    for (XMLNodeAndAttr xmlNodeAndAttr : xmlList) {
                        if(xmlNodeAndAttr.getNodeName().equals("DeviceID")){
                            String deviceId = xmlNodeAndAttr.getNodeValue();
                            for (int i = 0; i < locationInfoList.size(); i++) {
                                if(deviceId.equals(locationInfoList.get(i).getDeviceId())){
                                    index = i;
                                }
                            }
                            locationInfoList.get(index).setDeviceId(deviceId);
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("DeviceName")){
                            locationInfoList.get(index).setDeviceName(xmlNodeAndAttr.getNodeValue());
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("Longitude")){
                            locationInfoList.get(index).setLongitude(xmlNodeAndAttr.getNodeValue());
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("Dimension")){
                            locationInfoList.get(index).setDimension(xmlNodeAndAttr.getNodeValue());
                            //更新数据库

                            if(index == locationInfoList.size()-1){
                                //已经接收到最后一条
                                //---------------------------通知前台所有定位点刷新完毕--------------------------
                                locationInfoList.clear();

                            }else {
                                sendLocationInfo(locationInfoList.get(index+1).getDeviceId());
                            }
                        }
                    }
                }

                if(msg.contains("<LocationInfo>") && msg.contains("<Type>PUSH</Type>")){
                    //推送定位信息
                    String deviceId = "";
                    String longitude = "";
                    String dimension = "";
                    ArrayList<XMLNodeAndAttr> xmlList = new StringToXML(msg).getXMLList();
                    for (XMLNodeAndAttr xmlNodeAndAttr : xmlList) {
                        if(xmlNodeAndAttr.getNodeName().equals("DeviceID")){
                            deviceId = xmlNodeAndAttr.getNodeValue();
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("Longitude")){
                            longitude = xmlNodeAndAttr.getNodeValue();
                        }
                        if(xmlNodeAndAttr.getNodeName().equals("Dimension")){
                            dimension = xmlNodeAndAttr.getNodeValue();
                        }
                    }
                    Device device = new Device();
                    device.setDeviceId(deviceId);
                    device.setServerId(serverId);
                    device.setLongitude(longitude);
                    device.setDimension(dimension);
                    deviceMapper.updateLatLng(device);

                }

                return "结果返回了";
            }

            @Override
            public void onResult(String s) {
                super.onResult(s);
            }
        }.execute();
    }

    private boolean isFirstCreateThread = true;
    private boolean isRefresh = false;

    private void doGetList(final ArrayList<XMLNodeAndAttr> xmlList) {
        new ThreadTask<String>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public String onDoInBackground() {

                //不管是那次都要跟新数据库
                //List<Device> deviceDb = DataSupport.findAll(Device.class);
                //List<Device> deviceDb = deviceMapper.findAll();
                List<Device> deviceDb = deviceMapper.findAllByServerId(serverId);
                List<DeviceIsHave> deviceIsHaveList = new ArrayList<>();
                for (Device device : deviceDb) {
                    deviceIsHaveList.add(new DeviceIsHave(device.getDeviceId(),false));
                }
                for (XMLNodeAndAttr xmlNodeAndAttr : xmlList) {
                    if (xmlNodeAndAttr.getNodeName().equals("DeviceID")) {
                        for (DeviceIsHave deviceIsHave : deviceIsHaveList) {
                            if(deviceIsHave.getDeviceId().equals(xmlNodeAndAttr.getNodeValue())){
                                //找到id相同的设备了
                                deviceIsHave.setHave(true);
                            }
                        }
                    }

                    //存储区域对象
                    if (xmlNodeAndAttr.getNodeName().equals("RegionID")) {
                        Region region = new Region();
                        region.setRegionName(xmlNodeAndAttr.getNodeValue());
                        regionList.add(region);
                    }
                }
                //删除权限之外的数据
                for (DeviceIsHave deviceIsHave : deviceIsHaveList) {
                    if(!deviceIsHave.isHave()){
                        //数据库存在多余列表数据 删除
                        deviceMapper.del(deviceIsHave.getDeviceId(),serverId);

                        //删除参数列表中相关行
                        parameterMapper.delByDeviceId(deviceIsHave.getDeviceId(),serverId);
                        portMapper.delByDeviceId(deviceIsHave.getDeviceId(),serverId);
                        warningToneMapper.delByDeviceId(deviceIsHave.getDeviceId(),serverId);
                        displayMapper.delByDeviceId(deviceIsHave.getDeviceId(),serverId);
                        alarmMapper.delByDeviceId(deviceIsHave.getDeviceId(),serverId);
                        deviceCtrlModeMapper.delByDeviceId(deviceIsHave.getDeviceId(),serverId);
                        planParamValueMapper.delByDeviceId(deviceIsHave.getDeviceId(),serverId);
                        planParamMapper.delByDeviceId(deviceIsHave.getDeviceId(),serverId);
                        planModeMapper.delByDeviceId(deviceIsHave.getDeviceId(),serverId);
                    }
                }

                if(isRefresh){
                    //刷新设备的操作
                    //List<Device> all = deviceMapper.findAll();
                    List<Device> all = deviceMapper.findAllByServerId(serverId);
                    //List<Device> all = DataSupport.findAll(Device.class);
                    List<DeviceIsHave> needUpdate = new ArrayList<>();  //需要跟新的为true
                    //重新查询数据库之外存在存在的数据
                    for (XMLNodeAndAttr xmlNodeAndAttr : xmlList) {
                        if (xmlNodeAndAttr.getNodeName().equals("DeviceID")) {
                            DeviceIsHave deviceIsHave = new DeviceIsHave(xmlNodeAndAttr.getNodeValue(),true);
                            for (Device device : all) {
                                if(xmlNodeAndAttr.getNodeValue().equals(device.getDeviceId())){
                                    deviceIsHave.setHave(false);
                                }
                            }
                            needUpdate.add(deviceIsHave);
                        }
                    }

                    int countDevice = 1;
                    for (DeviceIsHave deviceIsHave : needUpdate) {
                        if(deviceIsHave.isHave()){
                            //发送查询设备
                            if (countDevice == 50) {
                                //每发送50条查询设备请求后 额外发送一次心跳
                                sendHeartBeat();
                               // System.out.println("账号跟新查询数据库不存在新设备50条设备-----额外发送的心跳包....");
                                countDevice = 0;
                            }

                            //交给异步线程执行查询操作
                            sendDevice(deviceIsHave.getDeviceId());

                            countDevice++;
                        }
                    }

                }



                if(!isRefresh){     //目前不管是否刷新设备都全部查询一边重新插入
                    //deviceReceiverList = new ArrayList<>();
                    for (XMLNodeAndAttr xmlNodeAndAttr : xmlList) {
                        if (xmlNodeAndAttr.getNodeName().equals("DeviceID")) {
                            //此节点是设备id节点(接下来一个个查询设备)
                            //Device device = new Device();
                            DeviceReceiver deviceReceiver = new DeviceReceiver(xmlNodeAndAttr.getNodeValue(), false);
                            deviceReceiverList.add(deviceReceiver);
                        }
                    }

                    //发送设备查询指令
                    int countDevice = 1;
                    int sendDeviceCount = 1;
                    for (XMLNodeAndAttr xmlNodeAndAttr : xmlList) {
                        if (xmlNodeAndAttr.getNodeName().equals("DeviceID")) {
                            //此节点是设备id节点
                            if (countDevice == 50) {
                                //每发送50条查询设备请求后 额外发送一次心跳
                                sendHeartBeat();
                               // System.out.println("运维平台发送查询50条设备-----额外发送的心跳包....");
                                countDevice = 0;
                            }
                            deviceIdList.add(xmlNodeAndAttr.getNodeValue());
                            //此节点是设备id节点(接下来一个个查询设备)

                            //交给异步线程执行查询操作
                            sendDevice(xmlNodeAndAttr.getNodeValue());

                            //System.out.println("发送设备查询第" + sendDeviceCount + "次");
                            countDevice++;
                            sendDeviceCount++;

                        }

                        //启动线程用来补发没有接收到的设备
                        if(isFirstCreateThread){
                            new CheckDeviceListTimeCount().start();
                            //System.out.println("-----启动补发线程");
                            isFirstCreateThread = false;
                        }

                    }
                }

                return "结果返回了";
            }

            @Override
            public void onResult(String s) {
                super.onResult(s);
            }
        }.execute();

    }


    /**
     * 发送登录指令
     */
    public void login() {

        BufferedWriter bufferedWriter = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        //写操作
        try {
            if (socket == null) {
                System.out.println("--------运维平台socket==null");
                return;
            }

            //通过socket获取字符流           
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "gb2312"));

            //获取要传送的xml文件字符串形式
            LoginXMLToString xmlFile = new LoginXMLToString(username, password);
            String xml = xmlFile.loginXMLToString();

            //字符串转换为字符输入流
            inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            //通过标准输入流获取字符流           
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line + "\n");
                //System.out.println("222" + line);
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            System.out.println("----------异常定位开始----------");
            e.printStackTrace();
            System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
            System.out.println("----------异常定位结束----------");
        }finally {

        }
    }

    /**
     * 发送查询系统信息指令
     *
     */
    public void sendSysInfo() {
        BufferedWriter bufferedWriter = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        //写操作
        try {
            //通过socket获取字符流           
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "gb2312"));

            //获取要传送的xml文件字符串形式
            SysInfoXMLToString xmlFile = new SysInfoXMLToString("20190707143000000001", fromInstance, toInstance);
            String xml = xmlFile.sysInfoXMLToString();

            //字符串转换为字符输入流
            inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            //通过标准输入流获取字符流           
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line + "\n");
                //System.out.println("111" + line);
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            System.out.println("----------异常定位开始----------");
            e.printStackTrace();
            System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
            System.out.println("----------异常定位结束----------");
        }finally {

        }
    }


    /**
     * 发送查询某设备的状态
     */
    public  void sendQueryStatus(final String deviceID,String sessionId) {
        new ThreadTask<String>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public String onDoInBackground() {

                BufferedWriter bufferedWriter = null;
                InputStream inputStream = null;
                BufferedReader bufferedReader = null;
                //写操作
                try {
                    //通过socket获取字符流           
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "gb2312"));

                    String seq = DateFormat.getSeq();
                    //获取要传送的xml文件字符串形式
                    QueryStatusXMLToString xmlFile = new QueryStatusXMLToString(seq, fromInstance, toInstance, deviceID);
                    String xml = xmlFile.queryStatus();

                    //字符串转换为字符输入流
                    inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                    //通过标准输入流获取字符流           
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        bufferedWriter.write(line + "\n");
                        //System.out.println("111" + line);
                        bufferedWriter.flush();
                    }
                    //加入等待返回队列
                    //System.out.println("客户端发送了查询"+deviceID+"设备状态信息");
                    myQueueList.add(new MyQueue(sessionId,seq));
                } catch (IOException e) {
                    System.out.println("----------异常定位开始----------");
                    e.printStackTrace();
                    System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
                    System.out.println("----------异常定位结束----------");
                }finally {

                }
                return "结果返回了";
            }

            @Override
            public void onResult(String s) {
                super.onResult(s);
            }
        }.execute();

    }


    /**
     * 向服务器发送查询设备请求
     *
     * @param deviceID
     */
    public void sendDevice(final String deviceID) {
        new ThreadTask<String>() {

            @Override
            public void onStart() {
                super.onStart();
                //Log.d("ThreadTask ", "onStart线程：" + Thread.currentThread().getName());
            }

            @Override
            public String onDoInBackground() {

                BufferedWriter bufferedWriter = null;
                InputStream inputStream = null;
                BufferedReader bufferedReader = null;
                //写操作
                try {
                    //Log.d("------>","发送设备开始写入");
                    //通过socket获取字符流           
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "gb2312"));

                    //获取要传送的xml文件字符串形式
                    DeviceXMLToString xmlFile = new DeviceXMLToString("20190707143000000002", fromInstance, toInstance, deviceID);
                    String xml = xmlFile.sysInfoXMLToString();
                    //System.out.println("打印我发送的查询设备xml" + xml);

                    //字符串转换为字符输入流
                    inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                    //通过标准输入流获取字符流           
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        bufferedWriter.write(line + "\n");
                        //System.out.println("111" + line);
                        bufferedWriter.flush();
                    }
                    //Log.d("------>","发送设备发送完成");
                } catch (IOException e) {
                    System.out.println("----------异常定位开始----------");
                    e.printStackTrace();
                    System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
                    System.out.println("----------异常定位结束----------");
                    //System.out.println("--------运维平台发送查询设备时候异常。。。");
                }finally {

                }
                return "结果返回了";
            }

            @Override
            public void onResult(String s) {
                super.onResult(s);
            }
        }.execute();
    }

    public void sendCancelCtrl(DeviceControlModeSet deviceControlModeSet,String sessionId) {
        new ThreadTask<String>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public String onDoInBackground() {

                BufferedWriter bufferedWriter = null;
                InputStream inputStream = null;
                BufferedReader bufferedReader = null;
                //写操作
                try {
                    //通过socket获取字符流           
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "gb2312"));

                    String seq = DateFormat.getSeq();
                    //获取要传送的xml文件字符串形式
                    DeviceControlModeSetXMLToString xmlFile = new DeviceControlModeSetXMLToString(seq, fromInstance, toInstance, deviceControlModeSet);
                    String xml = xmlFile.toXMLString();

                    //字符串转换为字符输入流
                    inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                    //通过标准输入流获取字符流           
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        bufferedWriter.write(line + "\n");
                        //System.out.println("111" + line);
                        bufferedWriter.flush();
                    }
                    //加入等待返回队列
                    //("客户端发送了"+deviceControlModeSet.getDeviceId()+"手动管控");
                    myQueueList.add(new MyQueue(sessionId,seq));
                } catch (IOException e) {
                    System.out.println("----------异常定位开始----------");
                    e.printStackTrace();
                    System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
                    System.out.println("----------异常定位结束----------");
                }finally {

                }
                return "结果返回了";
            }

            @Override
            public void onResult(String s) {
                super.onResult(s);
            }
        }.execute();
    }

    /**
     * 发送取消程控
     */
    public void sendCancelPlan(DevicePlanModeSet devicePlanModeSet,String sessionId) {
        new ThreadTask<String>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public String onDoInBackground() {

                //写操作
                InputStream inputStream = null;
                BufferedReader bufferedReader = null;
                BufferedWriter bufferedWriter = null;
                try {
                    //通过socket获取字符流           
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "gb2312"));

                    String seq = DateFormat.getSeq();
                    //获取要传送的xml文件字符串形式
                    DevicePlanModeSetXMLToString xmlFile = new DevicePlanModeSetXMLToString(seq, fromInstance, toInstance, devicePlanModeSet);
                    String xml = xmlFile.toXMLString();

                    //字符串转换为字符输入流
                    inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                    //通过标准输入流获取字符流           
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        bufferedWriter.write(line + "\n");
                        //System.out.println("111" + line);
                        bufferedWriter.flush();
                    }
                    //加入等待返回队列
                    //System.out.println("客户端发送了"+devicePlanModeSet.getDeviceId()+"程式控制");
                    myQueueList.add(new MyQueue(sessionId, seq));
                } catch (IOException e) {
                    System.out.println("----------异常定位开始----------");
                    e.printStackTrace();
                    System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
                    System.out.println("----------异常定位结束----------");
                } finally {

                }
                return "结果返回了";
            }

            @Override
            public void onResult(String s) {
                super.onResult(s);
            }
        }.execute();
    }


    /**
     * 心跳线程类
     */
    class HeartBeatThread extends Thread {
        public boolean exitHeart = false;

        @Override
        public void run() {
            super.run();
            //写操作
            try {
                int index = 0;
                //sendHeartBeat();
                while (!exitHeart) {
                    if (isStopTread) {
                        return;
                    }
                    if (index == 30) {
                        index = 0;


                        sendHeartBeat();

                        //System.out.println("--------运维平台心跳线程发送心跳包。。。");
                    }
                    index += 30;
                    sleep(30000);

                }
            } catch (Exception e) {
                //System.out.println("--------运维平台心跳包异常");
                System.out.println("----------异常定位开始----------");
                e.printStackTrace();
                System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
                System.out.println("----------异常定位结束----------");
            }
        }

    }


    /**
     * 倒计时检测服务器发送的心跳是否正常
     */
    class TimeCount extends Thread {
        @Override
        public void run() {
            super.run();
            //写操作
            try {
                int index = 0;
                while (true) {
                    if (isStopTread) {
                        return;
                    }
                    if (index == 120) {
                        if (isAcceptHeartBeat) {
                            //90s内接收到心跳
                            isAcceptHeartBeat = false;
                            index = 0;
                        } else {
                            //90s内没有接收到心跳
                            //初始化socket并且发送登录报文
                            mHeartBeatThread.exitHeart = true;
                            try {
                                index = 0;
                                if(socket!=null){
                                    socket.close();
                                }
                                socket = new Socket(mip,mport);
                                /*socket = new Socket();
                                InetAddress address = InetAddress.getByName(mip);
                                // 设置地址重用选项
                                socket.setReuseAddress(true);
                                socket.bind(new InetSocketAddress(40001+serverId));  //绑定固定端口
                                socket.connect(new InetSocketAddress(address,mport));*/
                                s = socket.getInputStream();
                                br = new BufferedReader(new InputStreamReader(s, "gbk"));
                                login();
                                mHeartBeatThread = new HeartBeatThread();
                                mHeartBeatThread.start();
                            } catch (Exception e) {
                                System.out.println("----------异常定位开始----------");
                                e.printStackTrace();
                                System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
                                System.out.println("----------异常定位结束----------");
                            }

                        }

                    }
                    index += 120;
                    Thread.sleep(120 * 1000);
                }
            } catch (Exception e) {
                System.out.println("----------异常定位开始----------");
                e.printStackTrace();
                System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
                System.out.println("----------异常定位结束----------");
            }
        }

    }

    private volatile boolean  isFirstsleep = true;
    private volatile boolean isRun = true;

    /**
     * 倒计时检测服务器发送的心跳是否正常
     */
    class CheckDeviceListTimeCount extends Thread {
        @Override
        public void run() {
            super.run();
            //写操作
            try {
                while (isRun) {
                    if (isStopTread) {
                        return;
                    }
                    if(isFirstsleep){
                        //第一次更具设备数量等待
                        sleep(deviceReceiverList.size() * 200);
                        //判断有哪些设备没收到
                        int count = 0;
                        for (DeviceReceiver deviceReceiver : deviceReceiverList) {
                            if(!deviceReceiver.isReceiver()){
                                //再次发送
                                //System.out.println(deviceReceiver.getDeviceID()+"没收到");
                                sendDevice(deviceReceiver.getDeviceID());
                                count++;
                            }
                        }
                        //System.out.println(count);
                        //继续沉睡等待
                        sleep(count * 3000);
                        isFirstsleep = false;
                    }else {
                        boolean isFinish  = true;   //设备接收完成标志
                        for (DeviceReceiver deviceReceiver : deviceReceiverList) {
                            if(!deviceReceiver.isReceiver()){
                                isFinish = false;
                            }
                        }
                        if(isFinish){
                            //System.out.println("检测到没有漏包");
                            //当每次接收完全的时候对比数据库删除本服务器无效数据
                            List<RegionDeviceType> list1 = deviceMapper.findAllRegionAndDeviceType(serverId);   //现在应该有这么多
                            for (int i = 0; i < list1.size(); i++) {
                                List<RegionDeviceType> result = regionDeviceTypeMapper.findByRegionDeviceType(list1.get(i));
                                if(result==null || result.size()==0){
                                    //新增
                                    regionDeviceTypeMapper.add(list1.get(i));
                                }
                            }
                            //List<RegionDeviceType> list2 = regionDeviceTypeMapper.findAll();    //新增之后的列表（去除多于数据）
                            List<RegionDeviceType> list2 = regionDeviceTypeMapper.findAllByServerId(serverId);    //新增之后的列表（去除多于数据）
                            for (int i = 0; i < list2.size(); i++) {
                                boolean needDel = true;
                                for (int i1 = 0; i1 < list1.size(); i1++) {
                                    if(list2.get(i).getRegion().equals(list1.get(i1).getRegion()) && list2.get(i).getSubregion().equals(list1.get(i1).getSubregion()) && list2.get(i).getDeviceType().equals(list1.get(i1).getDeviceType())){
                                        needDel = false;
                                    }
                                }
                                if(needDel){
                                    //删除数据库中的这个数据
                                    regionDeviceTypeMapper.delByRegionDeviceType(list2.get(i));
                                    //删除相应的权限应当删除用户有该权限对应的用户权限关联表
                                    userRegionMapper.delByRid(list2.get(i).getRid());
                                }
                            }
                            isRun = false;
                            return;
                        }else {
                            //计算还有没接受到设备的数量
                            int count1 = 0;
                            for (DeviceReceiver deviceReceiver : deviceReceiverList) {
                                if(!deviceReceiver.isReceiver()){
                                    //再次发送
                                    //System.out.println(deviceReceiver.getDeviceID()+"没收到");
                                    sendDevice(deviceReceiver.getDeviceID());
                                    count1++;
                                }
                            }

                            //继续沉睡等待
                            Thread.sleep(count1 * 3000);
                            isFirstsleep = false;
                        }

                    }

                }
            } catch (Exception e) {
                System.out.println("----------异常定位开始----------");
                e.printStackTrace();
                System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
                System.out.println("----------异常定位结束----------");
            }
        }

    }


    /**
     * 发送心跳包
     */
    private int sendHeartBeatCount = 1;

    void sendHeartBeat() {
        //System.out.println("运维平台发送心跳包第" + sendHeartBeatCount + "次");
        sendHeartBeatCount++;
        new ThreadTask<String>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public String onDoInBackground() {

                BufferedWriter bufferedWriter = null;
                Date now;
                SimpleDateFormat f;
                String dateStr;
                String seqStr;
                HeartBeatXMLToString xmlFile;
                String xml;
                InputStream inputStream = null;
                BufferedReader bufferedReader = null;
                String line;
                try {
                    //通过socket获取字符流           
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "gb2312"));

                    //获取要传送的xml文件字符串形式
                    now = new Date();
                    f = new SimpleDateFormat("yyyyMMddHHmmss");
                    dateStr = f.format(now);
                    seqStr = dateStr + "000001";
                    xmlFile = new HeartBeatXMLToString(seqStr, username);
                    xml = xmlFile.heartBeatXML();
                    //System.out.println("打印我发送的心跳包xml" + xml);
                    //字符串转换为字符输入流
                    inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                    //通过标准输入流获取字符流           
                    while ((line = bufferedReader.readLine()) != null) {
                        bufferedWriter.write(line + "\n");
                        //System.out.println(line);
                        bufferedWriter.flush();
                    }
                } catch (IOException e) {
                    //System.out.println("发送过程中心跳包异常");
                    mHeartBeatThread.exitHeart = true;
                    System.out.println("----------异常定位开始----------");
                    e.printStackTrace();
                    System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
                    System.out.println("----------异常定位结束----------");
                } finally {

                    bufferedWriter = null;
                    now = null;
                    f = null;
                    dateStr = null;
                    seqStr = null;
                    xmlFile = null;
                    xml = null;
                    inputStream = null;
                    bufferedReader = null;
                    line = null;
                    //System.gc();
                }
                return "结果返回了";
            }

            @Override
            public void onResult(String s) {
                super.onResult(s);
            }
        }.execute();

    }


    /**
     * 发送退出查询
     *
     * @param mCallbackListener
     */
    public void sendExit(CallbackListener mCallbackListener) {
        //写操作
        try {
            //通过socket获取字符流           
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "gb2312"));

            //获取要传送的xml文件字符串形式
            ExitXMLToString xmlFile = new ExitXMLToString("20190707143000000002", fromInstance, toInstance, username, password);
            String xml = xmlFile.exitXMLToString();

            //字符串转换为字符输入流
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            //通过标准输入流获取字符流           
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line + "\n");
                //System.out.println("111" + line);
                bufferedWriter.flush();
            }
            //System.out.println("发送完退出指令");
            mCallbackListener.onFinish(0, "");

            isLogin = false;

        } catch (IOException e) {
            System.out.println("----------异常定位开始----------");
            e.printStackTrace();
            System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
            System.out.println("----------异常定位结束----------");
            //System.out.println("发送完退出指令(异常)");
        }
    }


    /**
     * 发送查询文件指令
     */
    public void sendQueryFile(final String fileName) {
        new ThreadTask<String>() {

            @Override
            public void onStart() {
                super.onStart();
                //System.out.println("onStart线程：" + Thread.currentThread().getName());
            }

            @Override
            public String onDoInBackground() {
                //System.out.println("ThreadTask onDoInBackground线程： " + Thread.currentThread().getName());

                //写操作
                try {
                    if (socket == null) {
                        //System.out.println("socket==null");
                        return "";
                    }
                    //通过socket获取字符流           
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "gb2312"));

                    //获取要传送的xml文件字符串形式
                    FileXMLToString xmlFile = new FileXMLToString("20190707143000000002", fromInstance, toInstance, fileName);
                    String xml = xmlFile.FileXMLToString();

                    //字符串转换为字符输入流
                    InputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                    //通过标准输入流获取字符流           
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        bufferedWriter.write(line + "\n");
                        //System.out.println("222" + line);
                        bufferedWriter.flush();
                    }

                } catch (IOException e) {
                    System.out.println("----------异常定位开始----------");
                    e.printStackTrace();
                    System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
                    System.out.println("----------异常定位结束----------");
                }
                return "结果返回了";
            }

            @Override
            public void onResult(String s) {
                super.onResult(s);
            }
        }.execute();
    }

    /**
     * 向服务器发送查询设备请求
     *
     * @param deviceID
     */
    private void sendLocationInfo(final String deviceID) {
        new ThreadTask<String>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public String onDoInBackground() {

                //写操作
                try {
                    //Log.d("------>","发送设备开始写入");
                    //通过socket获取字符流           
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "gb2312"));

                    //获取要传送的xml文件字符串形式
                    LocationInfoXMLToString xmlFile = new LocationInfoXMLToString("20190707143000000002", fromInstance, toInstance, deviceID);
                    String xml = xmlFile.toXmlString();
                    //System.out.println("打印我发送的查询设备xml" + xml);

                    //字符串转换为字符输入流
                    InputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                    //通过标准输入流获取字符流           
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        bufferedWriter.write(line + "\n");
                        //System.out.println("111" + line);
                        bufferedWriter.flush();
                    }
                    //Log.d("------>","发送设备发送完成");
                } catch (IOException e) {
                    System.out.println("----------异常定位开始----------");
                    e.printStackTrace();
                    System.out.println(serverId +"号 服务器 在 "+DateFormat.getSeq()+"发生异常");
                    System.out.println("----------异常定位结束----------");
                    //System.out.println("--------运维平台发送查询设备时候异常。。。");
                }
                return "结果返回了";
            }

            @Override
            public void onResult(String s) {
                super.onResult(s);
            }
        }.execute();
    }
}