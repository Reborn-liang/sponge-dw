package cn.nearf.ggz.utils.message;

import org.apache.log4j.Logger;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import cn.nearf.ggz.config.ErpConfig;
import cn.nearf.ggz.utils.SUThread;

public class SmsUtil {
	private static final Logger log = Logger.getLogger(SmsUtil.class);
	
	private static final String ACCESS_KEY_ID = "LTAIQOoppm0hDQoL"; //阿里云短信服务用
	private static final String ACCESS_KEY_SECURET = "nKDAPoWWvdXXxZC6bZ3E5w3fx5DUUh"; //阿里云短信服务用
	
	private static interface SendMsgResult {
		public void onGetResult(String result);
	}
	
	public static void sendSMSVerifyCode(String code, String phone){
		sendSMSViaAliyun(phone, "{\"code\":\"" + code + "\"}", ErpConfig.getProperty("VERIFY_SMS_Template"));
	}
	
	public static void sendSMSAlarmMsg(String msg, String phone){
		if(ErpConfig.isDebug()){
			//如果是测试环境，不发告警短信。。。 note by Robin
			return;
		}
//		sendSMSMessage(phone, "{\"msg\":\""+msg+"\"}", "SMS_11355199", null);
	}
	
	private static void sendSMSViaAliyun(final String phone, final String param, final String templateCode)
	{
		SUThread.newThread(new SUThread.SUThreadProtocol() {
			public boolean process() throws Exception {
				//初始化ascClient需要的几个参数
				final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
				final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
				//替换成你的AK
				final String accessKeyId = ACCESS_KEY_ID;//你的accessKeyId,参考本文档步骤2
				final String accessKeySecret = ACCESS_KEY_SECURET;//你的accessKeySecret，参考本文档步骤2
				//初始化ascClient,暂时不支持多region（请勿修改）
				IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
				accessKeySecret);
				DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
				IAcsClient acsClient = new DefaultAcsClient(profile);
				 //组装请求对象
				 SendSmsRequest request = new SendSmsRequest();
				 //使用post提交
				 request.setMethod(MethodType.POST);
				 //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
				 request.setPhoneNumbers(phone);
				 //必填:短信签名-可在短信控制台中找到
				 request.setSignName("过个早");
				 //必填:短信模板-可在短信控制台中找到
				 request.setTemplateCode(templateCode);
				 //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
				 //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
				 request.setTemplateParam(param);
				//请求失败这里会抛ClientException异常
				SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
				if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
				//请求成功
				}
				return false;
			}
			public void prepare() {
			}
			public void onGetError(String error) {
			}
			public void finish() {
			}
			public void onFinally() {
			}
		}).start();
	}
	
	public static void main(String[] args) {
		sendSMSVerifyCode("1234","18607142212");
	}
}
