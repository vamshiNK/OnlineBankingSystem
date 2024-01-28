import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class MessageApi {
	 String mobile,sms;
  // Find your Account Sid and Token at twilio.com/console
  public static final String ACCOUNT_SID = "";
  public static final String AUTH_TOKEN = "";
 public MessageApi(String mobile,String sms)
 {
	 this.mobile=mobile;
	 this.sms=sms;
 }
 void SendSMS()
 {
   Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
   Message message = Message.creator(
     new com.twilio.type.PhoneNumber(mobile),
     new com.twilio.type.PhoneNumber("My Twilio phone number"),
     sms)
   .create();

   System.out.println(message.getSid());
 }
}