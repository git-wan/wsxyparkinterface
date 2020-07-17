package com.ws.wsxyparkinterface.parkfee.service;

import com.ws.wsxyparkinterface.parkfee.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

/**
 * @Author wancheng
 * @Date 2020-7-9 15:56
 * @Version 1.0
 */
//捷顺接口
@FeignClient(name = "ParkFeeService",url = "http://syx.jslife.com.cn/jsaims",configuration = FeignConfiguration.class)
public interface JsstService {
    //@RequestLine("GET /login?cid={cid}&usr={usr}&psw={psw}")
    @PostMapping(value = "/login",consumes = APPLICATION_FORM_URLENCODED_VALUE)
    String getToken(@RequestBody Map<String,?> map);


    @PostMapping(value = "/as",consumes = APPLICATION_FORM_URLENCODED_VALUE)
    String getParams(@RequestBody Map<String,?> map);

}
