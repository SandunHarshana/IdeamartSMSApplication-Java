/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wks.ideamart;

import com.wks.controller.AppConstant;
import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.StatusCodes;
import hms.kite.samples.api.sms.SmsRequestSender;
import hms.kite.samples.api.sms.messages.MtSmsReq;
import hms.kite.samples.api.sms.messages.MtSmsResp;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author WANNIACHCHI
 */
public class SmsSender {
    
    private final static Logger LOGGER = Logger.getLogger(SmsSender.class.getName());
    
    public void SendSMS(String smsType ,String sourceAddress, String smsContent ){
        try{
            SmsRequestSender smsMtSender = null;
            if(AppConstant.ISPRODUCT)
                smsMtSender = new SmsRequestSender(new URL("http://api.dialog.lk:8080/sms/send"));
            else
                smsMtSender = new SmsRequestSender(new URL("http://localhost:7000/sms/send"));
            
            ArrayList<String> sourceAdd = new ArrayList<>();
            sourceAdd.add(sourceAddress);
            MtSmsReq mtSmsReq;            
       
            if(smsType.equals("simple")){
                mtSmsReq = createSimpleMtSms( sourceAdd, smsContent );
                LOGGER.log(Level.INFO, "sms req : {0}", mtSmsReq);                
            }else{
                mtSmsReq = createSubmitMultipleSms( sourceAdd, smsContent);
                LOGGER.log(Level.INFO, "sms req : {0}", mtSmsReq);                
            }
            mtSmsReq.setSourceAddress("XXXXX"); 
            
            mtSmsReq.setApplicationId(AppConstant.APP_ID);
            mtSmsReq.setPassword(AppConstant.APP_PASSWORD);

            MtSmsResp mtSmsResp = smsMtSender.sendSmsRequest(mtSmsReq);
            
            String statusCode = mtSmsResp.getStatusCode();
            String statusDetails = mtSmsResp.getStatusDetail();
            if (StatusCodes.SuccessK.equals(statusCode)) {
                LOGGER.log(Level.INFO, "SMS message successfully sent with status code : {0}", statusCode);
            } else {
                LOGGER.log(Level.INFO, "MT SMS message sending failed with status code [{0}] {1}", new Object[]{statusCode, statusDetails});
            }
        }catch(MalformedURLException | SdpException ex){
            LOGGER.log(Level.INFO, "Unexpected error occurred", ex);
        }        
    }
    private MtSmsReq createSimpleMtSms( ArrayList<String> sourceAdd,String content) {
        //send a single SMS
        MtSmsReq mtSmsReq = new MtSmsReq();
        mtSmsReq.setDestinationAddresses(sourceAdd);
        mtSmsReq.setMessage(content);

        return mtSmsReq;
    }
    private MtSmsReq createSubmitMultipleSms( ArrayList<String> sourceAdd ,String content) {
        //send multiple SMSs
        MtSmsReq mtSmsReq = new MtSmsReq();
        mtSmsReq.setDestinationAddresses(sourceAdd);
        mtSmsReq.setMessage(content);

        return mtSmsReq;
    }

}
