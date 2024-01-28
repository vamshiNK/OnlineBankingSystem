import java.util.Scanner;

public class Admin {

	public static void main(String[] args) throws Exception {
		int option;
		boolean connection=DbConnect.getconnection();
		if(connection==true)
		{
			//System.out.println("Connection establised successfully");
		System.out.println("+*******************************************************+"+"\n|\t\t WELCOME TO SBI NET BANKING\t\t|"+"\n+*******************************************************+");
		System.out.println(" _______________________________");
		System.out.println("|\tSelect Your Option\t|");
		System.out.println("|_______________________________|");
		System.out.println("+-----------------------+"+"\n|1.Admin Login\t\t|\n|2.Customer Login\t|\n+-----------------------+");
		//System.out.println("Select Your Option\n1.Admin Login\n2.Customer Login");
		Scanner sc=new Scanner(System.in);
		option=sc.nextInt();
		switch(option)
		{
		case 1:System.out.println("+*******************************************************+"+"\n|\t\t Welcome to Admin Login\t\t\t|"+"\n+*******************************************************+");
		AdminLogin al=new AdminLogin();
		al.Login();
		break;
		case 2:System.out.println("+*******************************************************+"+"\n|\tWelcome to Customer Login Or Register\t\t|"+"\n+*******************************************************+");
		CustomerLogin cl=new CustomerLogin();
		cl.LogType();
		break;
		}
		sc.close();
	}
	}

}
