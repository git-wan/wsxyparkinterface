package com.ws.wsxyparkinterface.parkfee.config;

import com.ws.wsxyparkinterface.parkfee.service.MallCooService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.xml.ws.Endpoint;

/**
 * @Author wancheng
 * @Date 2020-7-10 10:15
 * @Version 1.0
 */
@Configuration
public class CxfConfiguration {
    @Resource
    private Bus bus;
    @Resource
    private MallCooService mallCooService;

    @Bean
    public Endpoint endpoint(){
        EndpointImpl endpoint = new EndpointImpl(bus, mallCooService);
        endpoint.publish("/wsxypark");
        return endpoint;
    }
}
