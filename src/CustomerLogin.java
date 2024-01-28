import java.sql.Connection;
import java.sql.Date;
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

public class CustomerLogin {

	Scanner sc;
	int cusid;
	//Connection con=null;
	void LogType()
	{
		int Choice;
		System.out.println("+-----------------------+\n|Select Your Option\t|\n|Press 1.For Login\t|\n|Press 2.For Register\t|\n"
				+ "+-----------------------+");
		//System.out.println("Select Your Option");
		//System.out.println("Press 1.For Login");
		//System.out.println("Press 2.For Register");
		sc = new Scanner(System.in);
		Choice= sc.nextInt();
		switch(Choice)
		{
		case 1:Login();
		break;
		case 2:Register();
		break;
		}
	}
	void Login()
	{
		String status="Yes";
		int account_status=0;
		String username,password;
		int vid;
		sc=new Scanner(System.in);
		System.out.println("Login Here");
		try
		{
			for(int i=1;i>=0;i--)
			{
				System.out.println("Enter Your UserName");
				username = sc.next();
				System.out.println("Enter Your Password");
				password = sc.next();
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank", "root", "vamshi1@NK");
				String sql="select * from customer where cusername='"+username+"' and cpassword='"+password+"' and status='"+status+"'";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				if(rs.next())
				{
					cusid = rs.getInt(1);
					System.out.println("Hello Customer .. Welcome To IBM Net Banking System");
					String sql2="select c1.cid,c1.cname,c1.status,c2.status,c3.account from customer c1 inner join customer_doc c2 on c1.cid=c2.cid inner join customer_account c3 on c2.cid=c3.cid and c2.status='Approved' and c2.cid='"+cusid+"'";
					Statement st1 = con.createStatement();
					ResultSet rs1 = st1.executeQuery(sql2);
					if(rs1.next())
					{
						System.out.println("Goto Dashboard");
						dashboard(); // Calling Dashboard Menu
					}
					else
					{
						vid = verification(); // verification method calling
						String sql1="select * from customer_doc where cid='"+cusid+"'";
						Statement st3 = con.createStatement();
						ResultSet rs3 = st3.executeQuery(sql1);
						while(rs3.next())
						{
							if(rs3.getString(6).equals("Approved"))
							{
								account_status=1;
							}
						}
						if(account_status==1)
						{
							System.out.println("Hello Customer .. Documents verified Success");
							System.out.println("Create your Account");
							accountCreation(); // Calling Account Creation Method
						}
						else
						{
							System.out.println("Hello Customer .. Sorry !! Document Verification Failed");
							System.out.println("Not Applicable to Create Account");
						}
					}
				}
				else
				{
					System.out.println("You have entered Invalid username and password You have only "+i+" times");

				}
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	void Register()
	{
		String cname,cuser,cpass,cemail,cmobile;
		System.out.println("Register Here");
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank", "root", "vamshi1@NK");
			System.out.println("Enter Customer Name");
			cname = sc.next();
			System.out.println("Enter Customer UserName");
			cuser = sc.next();
			System.out.println("Enter Customer Password");
			cpass = sc.next();
			System.out.println("Enter Customer Email");
			cemail = sc.next();
			System.out.println("Enter Customer Mobile");
			cmobile = sc.next();
			String sql="insert into customer(cname,cusername,cpassword,cemail,cmobile)values(?,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, cname);
			ps.setString(2, cuser);
			ps.setString(3, cpass);
			ps.setString(4, cemail);
			ps.setString(5, cmobile);
			int i = ps.executeUpdate();
			if(i==1)
			{
				String to=cemail;
				String subject = "SBI-Online NetBanking System -";
				String text = "Dear "+cname+",\n You're login Application request sent to Branch Manager\n Please Wait for the Confirmation Mail\n\n\n\n\n Thanks\n SBI-OnlineBanking System ";
				SendingGmail sg=new SendingGmail(to,subject,text);
				sg.SendMail();
				System.out.println("Application Successfully Sent to Branch Manager");
				System.out.println("Please Wait for the Verification");
				System.out.println("Try Login Later");
				System.exit(1);
			}
			else
			{
				System.out.println("Application Failed To sent");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	int verification()
	{
		String aadharcard,pancard,rationcard,status;
		int verified=0;
		//Scanner sc = new Scanner(System.in);
		System.out.println("Press Y For Document submitted or Press N for Not-Submitted");
		System.out.println("Aadhar Card is Submitted-?");
		aadharcard=sc.next();  // y
		System.out.println("Pan Card is Submitted-?");
		pancard=sc.next(); // y
		System.out.println("Ration Card is Submitted-?");
		rationcard = sc.next();  // n
		if(aadharcard.equalsIgnoreCase("yes"))
		{
			verified++;
		}
		if(pancard.equalsIgnoreCase("yes"))
		{
			verified++;
		}
		if(rationcard.equalsIgnoreCase("yes"))
		{
			verified++;
		}
		if(verified>=2)
		{
			status="Approved";
		}
		else
		{
			status="Rejected";
		}
		//System.out.println(status);
		try
		{
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank", "root", "vamshi1@NK");
			String sql="insert into customer_doc values(?,?,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, cusid);
			ps.setString(2, aadharcard);
			ps.setString(3, pancard);
			ps.setString(4, rationcard);
			ps.setInt(5, verified);
			ps.setString(6, status);
			int i=ps.executeUpdate();
			if(i>0)
			{
				System.out.println("Documents Saved");
			}
			else
			{
				System.out.println("Documents Not Saved");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 1;
	}
	void accountCreation()
	{
		String accounttype=null;
		double balance;
		String bank="SBI";
		int count=100;
		int value=0;
		try
		{
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank", "root", "vamshi1@NK");
			String sql="select count(*) from customer_account";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if(rs.next())
			{
				//System.out.println(rs.getInt(1));
				value = rs.getInt(1);
			}
			count = count + value;
			String cnumber = bank + count;
			System.out.println(cnumber);
			// sc = new Scanner(System.in);
			System.out.println("Press 1.For Savings Account\nPress 2.For Current Account");
			int choice = sc.nextInt();
			if(choice==1)
			{
				accounttype="Savings";
			}
			else if(choice==2)
			{
				accounttype="Current";
			}
			System.out.println("Enter Minimum Balance To Create Account");
			balance = sc.nextDouble();
			if(balance>=500)
			{
				String sql1="insert into customer_account(cid,cus_accountnumber,cus_accounttype,cus_balance,account)values(?,?,?,?,?)";
				PreparedStatement ps = con.prepareStatement(sql1);
				ps.setInt(1, cusid);
				ps.setString(2, cnumber);
				ps.setString(3, accounttype);
				ps.setDouble(4, balance);
				ps.setString(5, "Activated");
				int i = ps.executeUpdate();
				if(i>0)
				{
					System.out.println("Dear Customer ... Acount is Created Success");
					dashboard();
				}
				else
				{
					System.out.println("Dear Customer .. Account Creation Failed");
				}
			}
			else
			{
				System.out.println("Sorry !! minimum 500Rs to Create Account");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	void dashboard()
	{
		String cnumber;
		double Minamount=500;
		int option,ntxnpass,otxnpass,etxnpass;
		System.out.println("*****************************************");
		System.out.println("****** Welcome To Dashboard********");
		System.out.println("*****************************************");
		String choice="y";
		while(choice.equalsIgnoreCase("y"))
		{
			System.out.println("1.Balance EnQuiry");
			System.out.println("2.Deposit Amount");
			System.out.println("3.WithDrawl Amount");
			System.out.println("4.Transfer Amount");
			System.out.println("5.Change Transaction Password");
			System.out.println("6.View Your Statment");
			System.out.println("7.Exit");
			//Scanner sc= new Scanner(System.in);
			System.out.println("Select Your Process");
			option = sc.nextInt();
			switch(option)
			{
			case 1:
				System.out.println("******** Balance EnQuiry Process*********");
				System.out.println("Enter Customer Account number");
				cnumber = sc.next();
				try
				{
					Connection	con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank", "root", "vamshi1@NK");
					String sql="select * from customer_account where cus_accountnumber='"+cnumber+"'";
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(sql);
					if(rs.next())
					{
						System.out.println("Enter Transaction Password");
						ntxnpass = sc.nextInt();
						String sql1="select * from customer_account where txnpass='"+ntxnpass+"' and cid='"+cusid+"'";
						Statement st1 = con.createStatement();
						ResultSet rs1 = st1.executeQuery(sql1);
						if(rs1.next())
						{
							System.out.println("Your Balance Amount is:-  " + rs1.getDouble(4));
						}
						else
						{
							System.out.println("Transaction Password is InCorrect");
						}
					}
					else
					{
						System.out.println("Sorry !!! Customer Account number is InCorrect");
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				break;
			case 2:
				double damount;
				System.out.println("******** Deposit Amount Process*********");
				System.out.println("Enter Customer Account number");
				cnumber = sc.next();
				try
				{
					Connection	con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank", "root", "vamshi1@NK");
					String sql="select * from customer_account where cus_accountnumber='"+cnumber+"'";
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(sql);
					if(rs.next())
					{
						System.out.println("Enter Transaction Password");
						ntxnpass = sc.nextInt();
						String sql1="select * from customer_account where txnpass='"+ntxnpass+"' and cid='"+cusid+"'";
						Statement st1 = con.createStatement();
						ResultSet rs1 = st1.executeQuery(sql1);
						if(rs1.next())
						{
							double pamount=rs1.getDouble(4);
							System.out.println("Enter Your Deposit Amount");
							damount = sc.nextDouble();
							double tamount = pamount + damount;
							String sql2="update customer_account set cus_balance='"+tamount+"' where cid='"+cusid+"'";
							PreparedStatement ps = con.prepareStatement(sql2);
							int i=ps.executeUpdate();
							if(i>0)
							{
								String query="SELECT c.cmobile, ca.balance FROM customer c INNER JOIN customer_activity ca ON c.cid = ca.cid WHERE c.cid = 1 ORDER BY ca.dateandtime DESC LIMIT 1";
								Statement st3 = con.createStatement();
								ResultSet rs3 = st3.executeQuery(query);
								while(rs3.next())
								{
									double amount=rs3.getDouble(2);
									String mobile=rs3.getString(1);
									String sms="After Deposit Your account balance are "+amount;
									System.out.println(amount+" \n"+mobile);
									MessageApi ma=new MessageApi(mobile,sms);
									ma.SendSMS();
								}

								System.out.println("Your Amount Deposited in Your Account Successfully");
								//System.out.println("Your Balance is : - " + rs1.getDouble(4));
								// Code Here - Display updated amount //
							}
							else
							{
								System.out.println("Amount is not Deposited");
							}
						}
						else
						{
							System.out.println("Transaction Password is InCorrect");
						}
					}
					else
					{
						System.out.println("Sorry !!! Customer Account number is InCorrect");
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				break;
			case 3:
				double wamount,gamount;
				System.out.println("******** WithDrawl Amount Process*********");
				System.out.println("Enter Customer Account number");
				cnumber = sc.next();
				try
				{
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank", "root", "vamshi1@NK");
					String sql="select * from customer_account where cus_accountnumber='"+cnumber+"'";
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(sql);
					if(rs.next())
					{
						System.out.println("Enter Transaction Password");
						ntxnpass = sc.nextInt();
						String sql1="select * from customer_account where txnpass='"+ntxnpass+"' and cid='"+cusid+"'";
						Statement st1 = con.createStatement();
						ResultSet rs1 = st1.executeQuery(sql1);
						if(rs1.next())
						{
							System.out.println("Enter WithDrawl Amount");
							wamount = sc.nextDouble();
							//wamount-=Minamount;
							String sql2="select * from customer_account where cid='"+cusid+"'";
							Statement st2 = con.createStatement();
							ResultSet rs2 = st2.executeQuery(sql2);
							if(rs2.next())
							{
								//Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank", "root", "vamshi1@NK");
								gamount = rs2.getDouble(4);
								double uamount = gamount - wamount;
								//System.out.println(gamount+""+uamount+""+Minamount);
								if(Minamount>uamount) // 5001 > 5001 // Code Here minimum Balance maintaineance
								{
									System.out.println("You Have To Maintain Minmum Balance"+Minamount);
									System.out.println("Insufficient Balance Amount"+uamount);
								}
								else
								{
									String sql3="update customer_account set cus_balance='"+uamount+"' where cid='"+cusid+"'";
									PreparedStatement ps = con.prepareStatement(sql3);
									int i= ps.executeUpdate();
									if(i>0)
									{
										String query="SELECT c.cmobile, ca.balance FROM customer c INNER JOIN customer_activity ca ON c.cid = ca.cid WHERE c.cid = 1 ORDER BY ca.dateandtime DESC LIMIT 1";
										Statement st3 = con.createStatement();
										ResultSet rs3 = st3.executeQuery(query);
										while(rs3.next())
										{
											double amount=rs3.getDouble(2);
											//amount-=wamount;
											String mobile=rs3.getString(1);
											String sms="After Withdrawl From Your account balance are "+amount;
											System.out.println(amount+" \n"+mobile);
											MessageApi ma=new MessageApi(mobile,sms);
											ma.SendSMS();
										}
										System.out.println("Please Wait Your Transaction is Processing ..");
										System.out.println("Please Take Your Cash");
									}
									else
									{
										System.out.println("WithDrawl Failed.. Due to Some issues");
									}

								}
							}							
						}
						else
						{
							System.out.println("Transaction Password is InCorrect");
						}
					}
					else
					{
						System.out.println("Sorry !!! Customer Account number is InCorrect");
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				break;
			case 4:
				String tnumber;
				double tamount;
				System.out.println("******** Transaction Process*********");
				System.out.println("Enter Customer Account number");
				cnumber = sc.next();
				try
				{
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank", "root", "vamshi1@NK");
					String sql="select * from customer_account where cus_accountnumber='"+cnumber+"' and cid='"+cusid+"'";
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(sql);
					if(rs.next())
					{
						System.out.println("Enter Transaction Password");
						etxnpass = sc.nextInt();
						String sql1="select * from customer_account where txnpass='"+etxnpass+"' and cid='"+cusid+"'";
						Statement st1 = con.createStatement();
						ResultSet rs1 = st1.executeQuery(sql1);
						if(rs1.next())//txnpass
						{
							String query="SELECT cmobile FROM customer WHERE cid ='"+cusid+"'";
							Statement st2 = con.createStatement();
							ResultSet rs2 = st2.executeQuery(query);
							if(rs2.next())//mobile number
							{
								String mobile=rs2.getString(1);
								String cotp;
								String otp = SendOtp.generateOtp(SendOtp.otpLength);
								// Instantiate SendOtp class and send OTP
								SendOtp sendOtp = new SendOtp();
								sendOtp.sendOtp(mobile, otp);
								//System.out.println(mobile+" "+otp);
								System.out.println("Enter OTP To Transfer Amount");
								cotp=sc.next();
								if(cotp.equals(otp))
								{
									System.out.println("You entered the valid OTP: ");
									System.out.println("Enter Transfer Account Number");
									tnumber = sc.next(); // SBI101
									String sql2="select * from customer_account where cus_accountnumber='"+tnumber+"'";
									Statement st3 = con.createStatement();
									ResultSet rs3 = st3.executeQuery(sql2);
									if(rs3.next())
									{
										double vamount = rs3.getDouble(4);
										//System.out.println("Your Account Balance Are"+vamount);
										System.out.println("Transfer Account is Verified");
										System.out.println("Enter Transfer Amount");
										tamount = sc.nextDouble();
										//tamount+=Minamount;
										String sql3="select * from customer_account where cid='"+cusid+"'";
										Statement st4 = con.createStatement();
										ResultSet rs4= st4.executeQuery(sql3);
										if(rs4.next())
										{
											gamount = rs4.getDouble(4);
											double person1 = gamount - tamount;
											System.out.println(person1);
											if(Minamount > person1) // 5001 > 5001 // Code Here minimum Balance maintaineance
											{
												System.out.println("Insufficient Balance Amount to Transfer");
											}
											else
											{
												//System.out.println("True");
												//double person1 = gamount - tamount;
												double person2 = vamount + tamount;
												String sql4="update customer_account set cus_balance='"+person1+"' where cid='"+cusid+"'";
												PreparedStatement ps4 = con.prepareStatement(sql4);
												int i = ps4.executeUpdate();
												String sql5="update customer_account set cus_balance='"+person2+"' where cus_accountnumber='"+tnumber+"'";
												PreparedStatement ps5 = con.prepareStatement(sql5);
												int j = ps5.executeUpdate();
												if(i>0 && j>0)
												{
													String query1="SELECT c.cmobile, ca.balance, ca.transaction FROM customer c INNER JOIN customer_activity ca ON c.cid = ca.cid WHERE ca.accountno = '"+cnumber+"' OR ca.accountno = '"+tnumber+"' ORDER BY ca.dateandtime DESC LIMIT 2";
													Statement st5 = con.createStatement();
													ResultSet rs5 = st5.executeQuery(query1);
													while(rs5.next())
													{
														String mobile1=rs5.getString(1);
														double amount=rs5.getDouble(2);
														String transaction=rs5.getString(3);
														String sms="Amount "+transaction+" In Your Account Your Account Balance:"+amount;
														System.out.println(sms);
														System.out.println(amount+" \n"+mobile1);
														MessageApi ma=new MessageApi(mobile1,sms);
														ma.SendSMS();
													}
													System.out.println("Transfer Account Successfully Sent");
												}
											}
										}
									}
									else
									{
										System.out.println("Transfer Account Number Is Invalid");
									}
								}
								else
								{
									System.out.println("You Have Entered Invalid OTP Please Try Later");
								}
							}
							else
							{
								System.out.println("Mobile Number Not Found");
							}
						}
						else
						{
							System.out.println("Entered Transaction Password Is Invalid");
						}
					}
					else 
					{
						System.out.println("Customer Account Number Is Invalid");
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				break;
			case 5: System.out.println("***** Update / Change Transaction Password*****");
			System.out.println("Enter Old Transaction Password");
			otxnpass=sc.nextInt();
			String sql="select * from customer_account where txnpass='"+otxnpass+"' and cid='"+cusid+"'";
			try {
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank", "root", "vamshi1@NK");
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				if(rs.next())
				{
					System.out.println("Enter New Transaction Password");
					ntxnpass=sc.nextInt();
					String sql1="update customer_account set txnpass='"+ntxnpass+"' where cid='"+cusid+"'";
					PreparedStatement ps = con.prepareStatement(sql1);
					int i=ps.executeUpdate();
					if(i>0)
					{
						System.out.println("Successfully Transaction Password is Updated");
					}

				}
				else
				{
					System.out.println("Transaction password is not matched");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
			case 6:System.out.println("********View Your Statment*********");
			try
			{
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank", "root", "vamshi1@NK");
				String sql3="select *from customer_activity where cid='"+cusid+"'";
				Statement st = con.createStatement();
				ResultSet rs= st.executeQuery(sql3);
				System.out.println(" cid\taccountno\tbalance\ttransaction\tdateandtime");
				while(rs.next())	
				{
					int cid =rs.getInt(1);
					String accountno=rs.getString(2),transaction=rs.getString(4);
					Date dateandtime=rs.getDate(5);
					double balance=rs.getDouble(3);
					//System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t\t"+rs.getDouble(3)+"\t"+rs.getString(4)+"\t\t"+rs.getDate(5));
					String sms=cid+accountno+balance+transaction+dateandtime;
					System.out.println(sms);
				}

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			case 7:System.exit(1);
			}
			System.out.println("Do You Want To Continue Press Y Or Exit Press N");
			choice=sc.next();
		}
	}
}
