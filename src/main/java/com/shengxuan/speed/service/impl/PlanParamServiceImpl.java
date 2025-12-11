package com.shengxuan.speed.service.impl;

import com.shengxuan.speed.entity.PlanParam;
import com.shengxuan.speed.entity.PlanParamValue;
import com.shengxuan.speed.entity.pojo.PlanParam1;
import com.shengxuan.speed.mapper.PlanParamMapper;
import com.shengxuan.speed.mapper.PlanParamValueMapper;
import com.shengxuan.speed.service.PlanParamService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlanParamServiceImpl implements PlanParamService {

    private final PlanParamMapper planParamMapper;
    private final PlanParamValueMapper planParamValueMapper;

    public PlanParamServiceImpl(PlanParamMapper planParamMapper, PlanParamValueMapper planParamValueMapper) {
        this.planParamMapper = planParamMapper;
        this.planParamValueMapper = planParamValueMapper;
    }

    @Override
    public List<PlanParam1> findByDeviceIdAndPlanModeNo(String deviceId, String planModeNo,int serverId) {
        List<PlanParam1> planParam1List = new ArrayList<>();
        List<PlanParam> planParamList = planParamMapper.findByDeviceIdAndPlanModeNo(deviceId,planModeNo,serverId);
        for (PlanParam planParam : planParamList) {
            PlanParam1 planParam1 = new PlanParam1();
            planParam1.setDeviceId(planParam.getDeviceId());
            planParam1.setPlanModeNo(planParam.getPlanModeNo());
            planParam1.setPlanParamNo(planParam.getPlanParamNo());
            planParam1.setPlanParamName(planParam.getPlanParamName());
            planParam1.setDefaultValue(planParam.getDefaultValue());
            List<PlanParamValue> list = planParamValueMapper.findByDeviceIdAndPlanModeNoAndPlanParamNo(deviceId, planModeNo, planParam.getPlanParamNo(),planParam.getServerId());
            if(list!=null && list.size()>0){
                planParam1.setHavePlanParamValue(true);
            }else {
                planParam1.setHavePlanParamValue(false);
            }
            planParam1.setPlanParamValueList(list);
            planParam1List.add(planParam1);
        }
        //直接在后端转换为js好解析的格式
        return planParam1List;
    }

}
