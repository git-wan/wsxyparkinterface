package com.ws.wsxyparkinterface.parkfee.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @Author wancheng
 * @Date 2020-7-10 10:32
 * @Version 1.0
 */
@WebService
public interface MallCooService {

    @WebMethod
    String getParkingPaymentInfo(@WebParam(name="jsonStr") String jsonStr);

    @WebMethod
//    @WebResult(name = "String",targetNamespace = "")
    String PayParkingFee(@WebParam(name="jsonStr") String jsonStr);
}
