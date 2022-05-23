package edu.sau.NetworkLotterySystem.config;

import com.tencentcloudapi.common.Credential;

import com.tencentcloudapi.common.profile.ClientProfile;

import com.tencentcloudapi.common.profile.HttpProfile;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;



import com.tencentcloudapi.sms.v20190711.SmsClient;

import com.tencentcloudapi.sms.v20190711.models.*;


public class TxSendSmsUtil {



private static String SecretId = "AKIDa0QaT5QVJq65nDMEqBF4dvwf7JEsFLBj";



private static String SecretKey = "IK4XjdIfndEYbNs63i6E5fZDafT1IHyO";

//接口地址

private static String smsapiurl = "sms.tencentcloudapi.com";

//短信模板ID

private static String TemplateID = "1411204";

//短信签名

private static String Sign = "悦凡life个人网";

// 短信应用 SDK AppID，SDK AppID 以1400开头

private static String SmsSdkAppid = "1400681844";



public static String txSendSms(String[] phoneNumber,String[] templateParam)

{

try{



Credential cred = new Credential(SecretId, SecretKey);



HttpProfile httpProfile = new HttpProfile();

httpProfile.setEndpoint(smsapiurl);



ClientProfile clientProfile = new ClientProfile();

clientProfile.setHttpProfile(httpProfile);



SmsClient client = new SmsClient(cred, "", clientProfile);



SendSmsRequest req = new SendSmsRequest();

String[] phoneNumberSet1 = phoneNumber;

req.setPhoneNumberSet(phoneNumberSet1);



req.setTemplateID(TemplateID);

req.setSign(Sign);



String[] templateParamSet1 = templateParam;

req.setTemplateParamSet(templateParamSet1);



req.setSmsSdkAppid(SmsSdkAppid);



SendSmsResponse resp = client.SendSms(req);



String res = SendSmsResponse.toJsonString(resp);



//System.out.println(res);



return res;

//System.out.println(SendSmsResponse.toJsonString(resp));

} catch (TencentCloudSDKException e) {

System.out.println(e.toString());

return e.toString();

}

}



}
