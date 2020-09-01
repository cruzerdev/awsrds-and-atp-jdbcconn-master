
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AWSDb {

	public static void main(String[] args) throws ClassNotFoundException {
		Class.forName("oracle.jdbc.OracleDriver");
		Connection conn = null;
		try {
			//Pass the hosturl in the format i.e. host:port:sid
			String dbURL2 = "jdbc:oracle:thin:@springdev.ca1xrkuhforr.ap-south-1.rds.amazonaws.com:1521:ORCL";
			//Give the AWS Oracle RDS username 
			String username = "*****";
			//Give the AWS Oracle RDS password  
			String password = "*****";
			conn = DriverManager.getConnection(dbURL2, username, password);
			System.out.println("Database Driver Name: "+conn.getMetaData().getDriverName());
			System.out.println("Database Product Name: "+conn.getMetaData().getDatabaseProductName());
			System.out.println("Database Product Version: "+conn.getMetaData().getDatabaseProductVersion());
			System.out.println("Database Username: "+conn.getMetaData().getUserName());
			System.out.println("Database Connection URL: "+conn.getMetaData().getURL());
			if (conn != null) {
				System.out.println("Connected with connection #2");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (conn != null) {

				Statement statement = conn.createStatement();

				ResultSet results = statement.executeQuery("SELECT FIRST_NAME FROM CUSTOMER");

				while (results.next()) {
					String data = results.getString(1);

					System.out.println("Fetching data by column index for row " + results.getRow() + " : " + data);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

}
