package com.ws.wsxyparkinterface.parkfee.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ws.wsxyparkinterface.parkfee.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jws.WebService;

/**
 * @Author wancheng
 * @Date 2020-7-10 10:44
 * @Version 1.0
 */
@WebService(serviceName = "", targetNamespace = "", endpointInterface = "com.ws.wsxyparkinterface.parkfee.service.MallCooService")
@Component
@Slf4j
public class MallCooServiceImpl implements MallCooService {
    @Value("${serviceId1}")
    private String serviceId1;
    @Value("${serviceId2}")
    private String serviceId2;
    @Value("${couponType}")
    private String couponType;
    @Resource
    private Util util;

    @Override
    public String getParkingPaymentInfo(String jsonStr) {
        log.info("猫酷计费请求参数:{}",jsonStr);
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        String plateNo = (String)jsonObject.get("plateNo");
        JSONObject mallCooResponseParams = new JSONObject();
        if(plateNo==null||plateNo.equals("")){
            mallCooResponseParams.put("resMsg","车牌号为空");
            log.info("异常参数:{}",mallCooResponseParams);
            return mallCooResponseParams.toString();
        }
        JSONObject params = new JSONObject();
        JSONObject attr = new JSONObject();
        attr.put("carNo",plateNo);
        JSONObject ResponseParams = util.getResponseParams(attr,params,serviceId1);
        if(ResponseParams.getString("resCode").equals("1")){
            log.info("异常参数:{}",ResponseParams);
            return ResponseParams.toString();
        }
        JSONObject json = (JSONObject) ResponseParams.getJSONArray("dataItems").get(0);
        JSONObject attributes = (JSONObject) json.get("attributes");
        String retcode = attributes.getString("retcode");
        if(!retcode.equals("0")){
            mallCooResponseParams.put("resCode",1);
            mallCooResponseParams.put("resMsg",attributes.getString("retmsg"));
            log.info("异常参数:{}",mallCooResponseParams);
            return mallCooResponseParams.toString();
        }
        log.info("猫酷计费响应参数:{}",mallCooResponseParams);
        plateNo = attributes.getString("carNo");
        String feeId  = attributes.getString("orderNo");
        String entryTime  = attributes.getString("startTime");
        String parkingMinutes  = attributes.getString("serviceTime");
        double parkingMinutesDo = Double.parseDouble(parkingMinutes);
        parkingMinutes = String.valueOf(parkingMinutesDo%60==0?(parkingMinutesDo/60):(parkingMinutesDo/60)+1);
        String totalAmount  = attributes.getString("serviceFee");
        String unPayAmount  = attributes.getString("totalFee");
        mallCooResponseParams.put("resCode",retcode);
        mallCooResponseParams.put("resMsg","");
        mallCooResponseParams.put("plateNo",plateNo);
        mallCooResponseParams.put("feeId ",feeId );
        mallCooResponseParams.put("entryTime",entryTime);
        mallCooResponseParams.put("parkingMinutes",parkingMinutes);
        mallCooResponseParams.put("totalAmount",totalAmount);
        mallCooResponseParams.put("unPayAmount",unPayAmount);
        log.info("猫酷计费响应参数:{}",mallCooResponseParams);
        return mallCooResponseParams.toString();
    }

    //缴费
    @Override
    public String PayParkingFee(String jsonStr) {
        log.info("猫酷缴费请求参数:{}",jsonStr);
        JSONObject mallCooRequestParams = JSON.parseObject(jsonStr);
        JSONObject mallCooResponseParams = new JSONObject();
        mallCooResponseParams.put("resCode",1);
        String unPayAmount = mallCooRequestParams.getString("unPayAmount");
        String plateNo = mallCooRequestParams.getString("plateNo");
        if(plateNo==null||plateNo.equals("")){
            mallCooResponseParams.put("resMsg","车牌号为空");
            log.info("异常参数:{}",mallCooResponseParams);
            return mallCooResponseParams.toString();
        }
        if(unPayAmount==null||unPayAmount.equals("")){
            mallCooResponseParams.put("resMsg","unPayAmount为空");
            log.info("异常参数:{}",mallCooResponseParams);
            return mallCooResponseParams.toString();
        }
        JSONObject params = new JSONObject();
        JSONObject attr = new JSONObject();
        attr.put("carNo",plateNo);
        attr.put("couponValue",unPayAmount);
        attr.put("couponType",couponType);
        JSONObject ResponseParams = util.getResponseParams(attr,params,serviceId2);
        if(ResponseParams.getString("resCode").equals("1")){
            log.info("异常参数:{}",ResponseParams);
            return ResponseParams.toString();
        }
        log.info("捷顺缴费响应参数:{}",ResponseParams);
        if(ResponseParams.getJSONArray("dataItems").size()==0){
            mallCooResponseParams.put("resCode",ResponseParams.getString("resCode"));
            log.info("猫酷缴费响应参数:{}",mallCooResponseParams);
            return mallCooResponseParams.toString();
        }
        JSONObject json = (JSONObject) ResponseParams.getJSONArray("dataItems").get(0);
        JSONObject attributes = (JSONObject) json.get("attributes");
        mallCooResponseParams.put("resMsg",attributes.getString("RESULT"));
        log.info("猫酷缴费响应参数:{}",mallCooResponseParams);
        return mallCooResponseParams.toString();
    }
}
