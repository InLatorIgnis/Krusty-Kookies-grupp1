package krusty;

import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import static krusty.Jsonizer.toJson;

public class Database {
	
		private static final String jdbcString = "jdbc:mysql://puccini.cs.lth.se:3306/hbg03";
		private static final String jdbcUsername = "hbg03";
		private static final String jdbcPassword = "jav922za";
	
	
		private Connection conn = null;
	
		public void connect() {
			try {
				// Load the JDBC driver
				Class.forName("com.mysql.cj.jdbc.Driver");
				// Connect to the database
				conn = DriverManager.getConnection(jdbcString, jdbcUsername, jdbcPassword);
				System.out.println("Connected to the database successfully");
				
			} catch (ClassNotFoundException e) {
				System.out.println("MySQL JDBC driver not found");
				e.printStackTrace();
			} catch (SQLException e) {
				System.out.println("Connection failed");
				e.printStackTrace();
			}
			// The connection is stored in the conn field and can be used later
		}
	
		// Additional methods to use or close the connection
		public void closeConnection() {
			if (conn != null) {
				try {
					conn.close();
					System.out.println("Connection closed successfully");
				} catch (SQLException e) {
					System.out.println("Failed to close the connection");
					e.printStackTrace();
				}
			}
		}

	// TODO: Implement and change output in all methods below!

	public String getCustomers(Request req, Response res) {
		return "{}";
	}

	public String getRawMaterials(Request req, Response res) {
		return "{}";
	}

	public String getCookies(Request req, Response res) {
		return "{\"cookies\":[]}";
	}

	public String getRecipes(Request req, Response res) {
		return "{}";
	}

	public String getPallets(Request req, Response res) {
		return "{\"pallets\":[]}";
	}

	/**
	 * @param req
	 * @param res
	 * @return
	 * @throws SQLException
	 */
	public String reset(Request req, Response res) throws SQLException {

		//Connection connect = null;
		String clearTables = "TRANCUTE TABLE Storage"
		+ "TRANCUTE TABLE IngredientName"
		+ "TRANCUTE TABLE Pallet_Delivered"
		+ "TRANCUTE TABLE StorageAmount"
		+ "TRANCUTE TABLE Cookie"
		+ "TRANCUTE TABLE Pallet"
		+ "TRANCUTE TABLE Customer"
		+ "TRANCUTE TABLE StorageUpdate"
		+ "TRANCUTE TABLE IngredientInCookie"
		+ "TRANCUTE TABLE Order"
		+ "TRANCUTE TABLE OrderSpec";

		//Hade PreparedStatement resetAll = connect.prepareStatement(...) innan
		try(PreparedStatement conn = DriverManager.getConnection(jdbcString, jdbcUsername, jdbcPassword); 
		PreparedStatement resetAll = connect.prepareStatement(clearTables)) {

		} catch(SQLException e) {
			e.printStackTrace();

			
		}
		return "{}";
	}

	public String createPallet(Request req, Response res) {
		return "{}";
	}
}
