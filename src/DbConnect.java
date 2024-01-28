import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DbConnect {
	static boolean getconnection() throws SQLException
	{
		try 
		{
			 Class.forName("com.mysql.cj.jdbc.Driver");
			 Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlinebank", "root", "vamshi1@NK");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return true;
	}
}
