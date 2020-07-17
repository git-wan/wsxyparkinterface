package com.ws.wsxyparkinterface.parkfee.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ws.wsxyparkinterface.parkfee.service.JsstService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;


/**
 * @Author wancheng
 * @Date 2020-7-13 17:17
 * @Version 1.0
 */
@Component
@Slf4j
public class Util {

    @Value("${usr}")
    private String usr;
    @Value("${psw}")
    private String psw;
    @Value("${cid}")
    private String cid;
    @Value("${requestType}")
    private String  requestType;
    @Value("${businesserCode}")
    private String businesserCode;
    @Value("${parkCode}")
    private String parkCode;
    @Value("${orderType}")
    private String  orderType;
    @Value("${v}")
    private String v;
    @Value("${signKey}")
    private String signKey;

    //token
    private String tn = "";

    @Autowired
    private JsstService jsstService;


    public static String getSign(String signKey,String p) throws Exception {
        // 生成MD5签名
        MessageDigest md5Tool = MessageDigest.getInstance("MD5");
        byte[] md5Data = md5Tool.digest((p+signKey).getBytes("UTF-8"));
        String sn = toHexString(md5Data);
        return sn;
    }

    private static String toHexString(byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            buffer.append(String.format("%02X", bytes[i]));
        }
        return buffer.toString();
    }

    public  JSONObject getToken(){
            JSONObject jsstGetTokenParams = new JSONObject(true);
            jsstGetTokenParams.put("cid",cid);
            jsstGetTokenParams.put("usr",usr);
            jsstGetTokenParams.put("psw",psw);
            String tokenStr = jsstService.getToken(jsstGetTokenParams);
        return JSON.parseObject(tokenStr);
    }



    public JSONObject getResponseParams(JSONObject attr,JSONObject params,String serviceId){
        JSONObject ResponseParams = new JSONObject();
        ResponseParams.put("resCode","1");
        params.put("serviceId",serviceId);
        JSONObject jsstRequestParams;
        try {
            jsstRequestParams = buildJsstRequestParams(attr,params);
        } catch (Exception e) {
            ResponseParams.put("resMsg","签名异常");
            return ResponseParams;
        }
        JSONObject jsstLoginResponseParams;
        if(tn==null||tn.equals("")){
            jsstLoginResponseParams = getToken();
            tn = jsstLoginResponseParams.getString("token");
            if(!jsstLoginResponseParams.getString("resultCode").equals("0")){
                ResponseParams.put("resMsg",jsstLoginResponseParams.getString("message"));
                return ResponseParams;
            }
        }
        jsstRequestParams.put("tn",tn);
        log.info("捷顺请求参数:{}",jsstRequestParams);
        String JsstResponseStr = jsstService.getParams(jsstRequestParams);
        JSONObject jsstResponseParams = JSON.parseObject(JsstResponseStr);
        String resultCode = jsstResponseParams.getString("resultCode");
        if(resultCode.equals("6")){
            jsstLoginResponseParams = getToken();
            tn = jsstLoginResponseParams.getString("tn");
            if(!jsstLoginResponseParams.getString("resultCode").equals("0")){
                ResponseParams.put("resMsg",jsstLoginResponseParams.getString("message"));
                return ResponseParams;
            }
            JsstResponseStr = jsstService.getParams(jsstRequestParams);
            jsstResponseParams = JSON.parseObject(JsstResponseStr);
        }
         resultCode = jsstResponseParams.getString("resultCode");
        if(!resultCode.equals("0")){
            ResponseParams.put("resMsg",jsstResponseParams.getString("message"));
            return ResponseParams;
        }
        jsstResponseParams.put("resCode","0");
        return jsstResponseParams;
    }

    public  JSONObject buildJsstRequestParams(JSONObject attr,JSONObject params) throws Exception {
        JSONObject jsstRequestParams = new JSONObject();
        attr.put("businesserCode",businesserCode);
        attr.put("parkCode",parkCode);
        attr.put("orderType",orderType);
        params.put("requestType",requestType);
        params.put("attributes",attr);
        String p = params.toString();
        jsstRequestParams.put("cid", cid);
        jsstRequestParams.put("v", v);
        jsstRequestParams.put("p",p);
        String sn = getSign(signKey,p);
        jsstRequestParams.put("sn",sn);
        return jsstRequestParams;
    }
}
