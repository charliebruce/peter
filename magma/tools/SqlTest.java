import java.sql.*;

public class SqlTest {

	public static void main(String[] args){
		//LOLOL
		
		Connection conn = null;

        try
        {
            String userName = "root";
            String password = "1qaz2wsx3edc4rfv";
            String url = "jdbc:mysql://localhost/";// /test
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = DriverManager.getConnection (url, userName, password);
            System.out.println ("Database connection established");
        }
        catch (Exception e)
        {
            System.err.println ("Cannot connect to database server");
            e.printStackTrace();
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                    System.out.println ("Database connection terminated");
                }
                catch (Exception e) { /* ignore close errors */ }
            }
        }
	}
	
}
