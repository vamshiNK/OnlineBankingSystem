import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AdminLogin {
	Scanner sc;
	String username,password;
	int option;
	//private	int cid;

	void Login()
	{
		try
		{
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank", "root", "vamshi1@NK");
			String sql="select *from admin";
			Statement st = con.createStatement();
			ResultSet rs= st.executeQuery(sql);
			System.out.println("Enter username and password");
			sc=new Scanner(System.in);
			while(rs.next())
			{
				username=sc.next();
				password=sc.next();
				if(username.equals(rs.getString(2)) && password.equals(rs.getString(3)))
				{
					System.out.println("Admin Login Successfully");
					System.out.println("Select any Option\n1.VerifyCustomer\n2.Customer History");
					option=sc.nextInt();
					switch(option)
					{
					case 1:VerifyCustomer();
					break;
					case 2:CustomerHistory();
					break;
					}
				}
				else 
				{
					System.out.println("login failed");
				}
			} 
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	void VerifyCustomer()
	{
		int cid;
		String status;
		try 
		{
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank", "root", "vamshi1@NK");
			String sql="Select *from customer";
			PreparedStatement ps=con.prepareStatement(sql);
			ResultSet rs=ps.executeQuery(sql);
			System.out.println("cid\tcname\tcusername\tcemail\t\t\tcmoble\t\tcstatus");
			while(rs.next())
			{
				System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t\t"+rs.getString(5)+"\t"+rs.getString(6)+"\t"+rs.getString(7));
			}
			//exit()
			System.out.println("Hello Admin Please Enter Customer Id To Aprove");
			System.out.println("Enter customer Id");
			sc=new Scanner(System.in);
			cid=sc.nextInt();
			String str="select *from customer where cid='"+cid+"'";
			Statement st1 = con.createStatement();
			ResultSet rs1= st1.executeQuery(str);
			while(rs1.next())
			{
				System.out.println("If You Want To Verify Press Y Or N To Reject");
				status=sc.next();
				if(status.equalsIgnoreCase("y"))
				{
					status="Yes";
				}
				else
				{
					status="No";
				}
				String quary="Update customer set status='"+status+"'where cid='"+cid+"'";
				PreparedStatement ps1=con.prepareStatement(quary);
				ps1.executeUpdate();
			}
			String sql1="select *from customer where cid='"+cid+"'";
			PreparedStatement ps1=con.prepareStatement(sql1);
			ResultSet rs2=ps1.executeQuery(sql1);
			while(rs2.next())
			{
				
				String sta=rs2.getString(7);
				//System.out.println(sta);
				if(sta.equalsIgnoreCase("Yes"))
				{
					String mobile=rs2.getString(6);
					String cemail=rs2.getString(5);
					String cname=rs2.getString(2),cuser=rs2.getString(3);
					String sms="Your Account Verification Completed";
					System.out.println(mobile);
					MessageApi ma=new MessageApi(mobile,sms);
					ma.SendSMS();
					String host="smtp.gmail.com";   // Types of gmail sending hostname []
					final String user="vamshichary2810@gmail.com";//change accordingly  
					final String password="kjza gkti qyrp qrxm";//change accordingly  

					String to=cemail;//change accordingly  
					
					String subject=("SBI-Online NetBanking System -");  
					String text=("Dear,\n "+cname+" Welcome to SBI Online Banking System\n  You're application is Verified Successfully \n  Please Login with Your username-"+cuser+" and You're Password Credentials\nSubmit your Aadhar card, Pan card, Ration Card For Further Verfication.\n\n\n\n Thanks\n SBI-OnlineBanking System "); 
					SendingGmail sg=new SendingGmail(to,subject,text);
					sg.SendMail();
				}
				
				else
				{
					String mobile=rs2.getString(6);
					String sms="Your Account Verification Not Completed";
					System.out.println(mobile);
					MessageApi ma=new MessageApi(mobile,sms);
					ma.SendSMS();
				}
			}
			con.close();
			sc.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	void CustomerHistory()
	{
		System.out.println("********View Customer Statment*********");
		try
		{
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank", "root", "vamshi1@NK");
			String sql="select *from customer_activity";
			Statement st = con.createStatement();
			ResultSet rs= st.executeQuery(sql);
			System.out.println("cid\taccountno\tbalance\t\ttransaction\tdateandtime");
			while(rs.next())	
			{
				System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t\t"+rs.getDouble(3)+"\t\t"+rs.getString(4)+"\t\t"+rs.getDate(5));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}

