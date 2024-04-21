package krusty;

import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;



import java.util.ArrayList;
import java.sql.ResultSet;


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
// TODO by Martin


public String getPallets(Request req, Response res) {
    // Initial SQL query, booleans are ENUMS for TINYs
    String sql = "SELECT id, IF(blocked_bool_attr, 'yes', 'no') AS blocked  FROM pallets";
	
    // ArrayList to hold parameters for prepared statement
    ArrayList<String> values = new ArrayList<>();

    // Handling the 'from' query parameter
    String fromParam = req.queryParams("from");
    if (fromParam != null) {
        sql += " AND someColumn >= ?";
        values.add(fromParam);
    }

    // Add more query parameters handling here
    // Example:
    // String toParam = req.queryParams("to");
    // if (toParam != null) {
    //     sql += " AND someColumn <= ?";
    //     values.add(toParam);
    // }

    try (Connection connection = conn;
         PreparedStatement stmt = connection.prepareStatement(sql)) {
        // Set the values for the prepared statement
        for (int i = 0; i < values.size(); i++) {
            stmt.setString(i + 1, values.get(i));
        }

        // Execute the query and handle the results
        try (ResultSet rs = stmt.executeQuery()) {
            // Initialize JSON result string
            StringBuilder result = new StringBuilder("{\"pallets\":[");
            boolean first = true;
            while (rs.next()) {
                if (!first) {
                    result.append(",");
                } else {
                    first = false;
                }
                result.append(String.format("{\"id\": \"%s\", \"blocked\": \"%s\"}",
                        rs.getString("id"), rs.getString("blocked")));
            }
            result.append("]}");

            // Return the JSON result
            return result.toString();
        }

    } catch (SQLException e) {
        // Log error and return an error message or empty JSON
        e.printStackTrace();
        return "{\"pallets\":[],\"error\":\"Database error occurred.\"}";
    }
}


	/**
	 * @param req
	 * @param res
	 * @return
	 * @throws SQLException
	 */
	
	public String reset(Request req, Response res) throws SQLException {

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
		try( Connection connection = conn;
		PreparedStatement resetAll = connection.prepareStatement(clearTables)) {

		} catch(SQLException e) {
			e.printStackTrace();

			
		}
		return "{}";
	}
// Todo by Martin
public String createPallet(Request req, Response res) {
    // Get the cookie name from the query parameter
    String cookieName = req.queryParams("cookie");
    if (cookieName == null) {
        res.status(400); // Bad Request
        return "{\"status\":\"error\"}";
    }

    // Database connection and SQL queries
    String checkCookieSql = "SELECT COUNT(*) FROM Cookie WHERE Name = ?";
    String insertPalletSql = "INSERT INTO Pallets (productionDate, Blocked, Location, Name) VALUES (NOW(), false, 'Factory 1', ?)";

    try (Connection connection = conn;
         PreparedStatement checkCookieStmt = connection.prepareStatement(checkCookieSql);
         PreparedStatement insertPalletStmt = connection.prepareStatement(insertPalletSql, Statement.RETURN_GENERATED_KEYS)) {

        // Check if the cookie exists
        checkCookieStmt.setString(1, cookieName);
        ResultSet cookieResult = checkCookieStmt.executeQuery();
        if (cookieResult.next() && cookieResult.getInt(1) > 0) {
            // Cookie exists, create the pallet
            insertPalletStmt.setString(1, cookieName);
            int affectedRows = insertPalletStmt.executeUpdate();
            if (affectedRows > 0) {
                // Pallet created successfully, retrieve and return new pallet ID
                try (ResultSet generatedKeys = insertPalletStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long palletId = generatedKeys.getLong(1);
                        res.status(201); // Created
                        return String.format("{\"status\":\"ok\",\"id\":%d}", palletId);
                    } else {
                        res.status(500); // Internal Server Error
                        return "{\"status\":\"error\"}";
                    }
                }
            } else {
                res.status(500); // Internal Server Error
                return "{\"status\":\"error\"}";
            	}
        	} else {
            // Cookie does not exist
            res.status(404); // Not Found
            return "{\"status\":\"unknown cookie\"}";
        	}
    	} catch (SQLException e) {
        e.printStackTrace();
        res.status(500); // Internal Server Error
        return "{\"status\":\"error\"}";
    	}
	}
}