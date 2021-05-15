package pw.cdmi.aws.edu.common.service;

import com.smn.client.AkskSmnClient;
import com.smn.client.SmnClient;
import com.smn.request.sms.SmsPublishRequest;
import com.smn.response.sms.SmsPublishResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SmnService {

    @Value("${huawei.smn.access.key}")
    private static String ACCESS_Key = "TDRC65WZKMB2P6S92JH4";

    @Value("${huawei.smn.secret.access.key}")
    private static String SECRET_ACCESS_KEY = "JJhlAzeLRQnkuGthjPRdoGPu3jpvfGHZ1iwdshxY";

    @Value("${huawei.smn.`.name}")
    private static String REGION_NAME = "cn-north-1";

    @Value("${huawei.smn.sign.id}")
    private static String SIGN_Id = "36778e025ec747b385c3673410587246";

    public static void sendMessage(String mobile, String value, String SignId) {
        SmnClient smnClient = new AkskSmnClient(
                ACCESS_Key,
                SECRET_ACCESS_KEY,
                REGION_NAME);


        // 构造请求对象
        SmsPublishRequest smnRequest = new SmsPublishRequest();
        // 设置参数,接收手机号，短信内容，短信签名ID
        smnRequest.setEndpoint(mobile)
                .setMessage(value)
                .setSignId(SignId);
        // 发送短信
        SmsPublishResponse res = null;
        try {
            res = smnClient.sendRequest(smnRequest);
            System.out.println("mobile = [" + mobile + "], value = [" + value + "], SignId = [" + SignId + "]");
            if(res.getHttpCode()!= 200){
                System.out.println(mobile+"短信发送失败"+"httpCode:" + res.getHttpCode()
                        + ",message_id:" + res.getMessageId()
                        + ", request_id:" + res.getRequestId()
                        + ", errormessage:" + res.getMessage()
                        + " code:"+res.getCode());
            }
        } catch (Exception e) {
            // 处理异常
            System.out.println(mobile+"短信发送失败");
            throw new SecurityException("短信发送失败");
        }

    }

    public static String sendMessage(String mobile) {

        String endMessage = "（有效期十分钟，请完成验证），如非本人操作，请忽略本消息";
        String uuid = String.valueOf(Math.abs(UUID.randomUUID().toString().replace("-", "").hashCode()));
        String time = String.valueOf(System.currentTimeMillis());
        String authNumber = uuid.subSequence(1, 5) + time.substring(11, 13);
        String headMessage = "你的验证码为：";
        sendMessage(mobile, headMessage + authNumber + endMessage, SIGN_Id);
        return authNumber;
    }
    
    public static void sendMessage(String mobile,String content) {
        sendMessage(mobile, content, SIGN_Id);
    }
    
    

    public static void sendBusinessMessage(String mobile, String userName, String userPhone, String orgName, String productName, Boolean isLogistics) {
//        String ss = "汪柏均(15928437563)在微信小程序“爱灵格儿童英语”购买了您商店【爱灵格】的"早教课程"。该商品需要物流，请在48小时内发货";
        StringBuilder message = new StringBuilder();
        message.append(userName).append("(").append(userPhone).append(")").append("在微信小程序“爱灵格儿童英语”购买了您商店【").append(orgName).append("】的“");
        message.append(productName).append("”。");
        if(isLogistics){
            message.append("该商品需要物流，请在48小时内发货");
        }
        sendMessage(mobile, message.toString(), SIGN_Id);
    }

//    public static void main(String[] args) {
////        sendMessage("18224431747","系统已经设置你为成都七中管理员!");
//        sendBusinessMessage("13390866191","汪柏均", "15928437563", "成都早慧堂教育咨询有限公司", "etar课程", true);
//    }
}
