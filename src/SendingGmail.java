import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendingGmail {
	private String to,subject,text;//change accordingly  
	SendingGmail(String to,String subject,String text)
	{
		this.to=to;
		this.subject=subject;
		this.text=text;
	}
	void SendMail()
	{
		String host="smtp.gmail.com";   // Types of gmail sending hostname []
		final String user="vamshichary2810@gmail.com";//change accordingly  
		final String password="kjza gkti qyrp qrxm";//change accordingly  

		//Get the session object 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		Session session = Session.getDefaultInstance(props,  
				new javax.mail.Authenticator()
		{  
			protected PasswordAuthentication getPasswordAuthentication()
			{  
				return new PasswordAuthentication(user,password);  
			}  
		});  

		//Compose the message  
		try
		{  
			MimeMessage message = new MimeMessage(session);  
			message.setFrom(new InternetAddress(user));  
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
			message.setSubject(subject);
			message.setText(text);
			//send the message  
			Transport.send(message);  

			System.out.println("message sent successfully...");
		} 
		catch (MessagingException e)
		{
			e.printStackTrace();
		}
	}

}
