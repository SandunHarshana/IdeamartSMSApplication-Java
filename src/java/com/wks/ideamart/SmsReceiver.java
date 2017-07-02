/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wks.ideamart;

import hms.kite.samples.api.sms.MoSmsListener;
import hms.kite.samples.api.sms.messages.MoSmsReq;

/**
 *
 * @author WANNIACHCHI
 */
public class SmsReceiver implements MoSmsListener{

    @Override
    public void init() {
        
    }

    @Override
    public void onReceivedSms(MoSmsReq msr) {
        //Recieve SMS
        String smsContent,sourceAddress;
        smsContent = "Received message is : "+msr.getMessage(); //get sms content
        sourceAddress = msr.getSourceAddress(); //get source address
        
        //Send SMS
        SmsSender smsSender = new SmsSender();
        smsSender.SendSMS("simple", sourceAddress, smsContent);
    }
    
}
