import java.util.Properties;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import oracle.ucp.jdbc.PoolDataSource;

public class ATPJson {



	  final static String DB_URL=   "jdbc:oracle:thin:@covaxine_high?TNS_ADMIN=C:\\Users\\devanshug\\Downloads\\Wallet_covaxine";
//	final static String DB_URL=   "jdbc:oracle:thin:@adb.oraclecloud.com:1522/covid19";
//	final static String DB_URL=   "jdbc:oracle:thin:@tcps://joxwp8rwsuxqees-covid19.adb.ap-mumbai-1.oraclecloudapps.com";//_high.adb.oraclecloud.com?oracle.net.ssl_server_cert_dn=\"CN=adb.ap-mumbai-1.oraclecloud.com,OU=Oracle ADB INDIA,O=Oracle Corporation,L=Redwood City,ST=California,C=US\"
	  // Use TNS alias when using tnsnames.ora.  Use it while connecting to the database service on cloud. 
	  // final static String DB_URL=   "jdbc:oracle:thin:@orcldbaccess";
	  final static String DB_USER                 = "*****";
	  final static String DB_PASSWORD             = "*********";
	  final static String CONN_FACTORY_CLASS_NAME = "oracle.jdbc.pool.OracleDataSource";

	  /*
	   * The sample demonstrates UCP as client side connection pool.
	   */
	  public static void main(String args[]) throws Exception {
	    // Get the PoolDataSource for UCP
	    PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();

	    // Set the connection factory first before all other properties
	    pds.setConnectionFactoryClassName(CONN_FACTORY_CLASS_NAME);
	    pds.setURL(DB_URL);
	    pds.setUser(DB_USER);
	    pds.setPassword(DB_PASSWORD);
	    pds.setConnectionPoolName("JDBC_UCP_POOL");

	    // Default is 0. Set the initial number of connections to be created
	    // when UCP is started.
	    pds.setInitialPoolSize(5);

	    // Default is 0. Set the minimum number of connections
	    // that is maintained by UCP at runtime.
	    pds.setMinPoolSize(5);

	    // Default is Integer.MAX_VALUE (2147483647). Set the maximum number of
	    // connections allowed on the connection pool.
	    pds.setMaxPoolSize(20);

	    // Default is 30secs. Set the frequency in seconds to enforce the timeout
	    // properties. Applies to inactiveConnectionTimeout(int secs),
	    // AbandonedConnectionTimeout(secs)& TimeToLiveConnectionTimeout(int secs).
	    // Range of valid values is 0 to Integer.MAX_VALUE. .
	    pds.setTimeoutCheckInterval(5);

	    // Default is 0. Set the maximum time, in seconds, that a
	    // connection remains available in the connection pool.
	    pds.setInactiveConnectionTimeout(10);

	    // Set the JDBC connection properties after pool has been created
	    Properties connProps = new Properties();
	    connProps.setProperty("fixedString", "false");
	    connProps.setProperty("remarksReporting", "false");
	    connProps.setProperty("restrictGetTables", "false");
	    connProps.setProperty("includeSynonyms", "false");
	    connProps.setProperty("defaultNChar", "false");
	    connProps.setProperty("AccumulateBatchResult", "false");
	    pds.setConnectionProperties(connProps);
	    System.out.println("Available connections before checkout: "
	        + pds.getAvailableConnectionsCount());
	    System.out.println("Borrowed connections before checkout: "
	        + pds.getBorrowedConnectionsCount());
	    // Get the database connection from UCP.
	    try (Connection conn = pds.getConnection()) {
	      System.out.println("Available connections after checkout: "
	          + pds.getAvailableConnectionsCount());
	      System.out.println("Borrowed connections after checkout: "
	          + pds.getBorrowedConnectionsCount());
	      doSQLWork(conn);
	    }
	    System.out.println("Available connections after checkin: "
	            + pds.getAvailableConnectionsCount());
	        System.out.println("Borrowed connections after checkin: "
	            + pds.getBorrowedConnectionsCount());
	    }
	  
	  public static void doSQLWork(Connection conn) {
		    try {
		      conn.setAutoCommit(false);
		      Statement statement = conn.createStatement();
		      ResultSet resultSet = statement.executeQuery("select tid,fname from testdb");
		      System.out.println("--------------------------");
		      while (resultSet.next()) {
		        System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2));
		      }
		      System.out.println("\nSuccessfully tested a connection from UCP");
		    }
		    catch (SQLException e) {
		      System.out.println("UCPSample - "
		          + "doSQLWork()- SQLException occurred : " + e.getMessage());
		    }
		    finally {
		      // Clean-up after everything
		      try (Statement statement = conn.createStatement()) {
		        //statement.execute("drop table EMP");
		      }
		      catch (SQLException e) {
		        System.out.println("UCPSample - "
		            + "doSQLWork()- SQLException occurred : " + e.getMessage());
		      }
		    }
		  }

}
