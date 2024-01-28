import java.util.Random;

public class Randomotp {

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
        int otpLength = 6; // You can adjust the length of the OTP as needed
        String generatedOtp = generateOtp(otpLength);

        System.out.println("Generated OTP: " + generatedOtp);
    }
}
