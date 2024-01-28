import java.util.Random;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class SendOtp {
    // Your Twilio Account SID and Auth Token
    public static final String ACCOUNT_SID = "AC8ade96189e307903b8495ff7be5c1a78";
    public static final String AUTH_TOKEN = "1981902bd0da3b7722089a1bbf0eabdc";
    // Your Twilio phone number
    public static final String TWILIO_PHONE_NUMBER = "+17155397061";
    public static void sendOtp(String customerPhoneNumber, String otp) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(customerPhoneNumber),
                new com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER),
                "Your OTP is: " + otp)
                .create();

        System.out.println("OTP sent successfully. SID: " + message.getSid());
    }
    static int otpLength = 6;
    public static String generateOtp(int length) {
        // Define the characters allowed in the OTP
        String characters = "0123456789";
        StringBuilder otp = new StringBuilder();

        // Initialize a random number generator
        Random random = new Random();

        // Generate the OTP of the specified length
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            otp.append(characters.charAt(index));
        }
        
        return otp.toString();
    }
    public static void main(String[] args) {
        // Example usage
        
    	String customerPhoneNumber = "+919381065927"; // Replace with the actual customer's phone number
         // You can adjust the length of the OTP as needed
    	String generatedOtp = generateOtp(otpLength);
        System.out.println("Generated OTP: " + generatedOtp);
    	String otp = generatedOtp; // Replace with the actual OTP generated for the customer
        sendOtp(customerPhoneNumber, otp);
    }
}
