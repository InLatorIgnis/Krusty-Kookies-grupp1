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

import com.mysql.cj.PreparedQuery;
import com.mysql.cj.jdbc.PreparedStatementWrapper;
import com.mysql.cj.xdevapi.PreparableStatement;

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
		String selectCustomers = "select name, address from Customer";
		try (
			PreparedStatement ps = conn.prepareStatement(selectCustomers);
		) {
			ResultSet resultSet = ps.executeQuery();
			String json = Jsonizer.toJson(resultSet, "customers");
			return json;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{}";
	}

	public String getRawMaterials(Request req, Response res) {
		String selectRawMaterials = 
			"SELECT s.IngredientName as name, s.StorageAmount as amount, i.Unit as unit " +
			"FROM Storage s " +
			"JOIN IngredientInCookie i ON s.IngredientName = i.IngredientName";
		try (
			PreparedStatement ps = conn.prepareStatement(selectRawMaterials);
		) {
			ResultSet resultSet = ps.executeQuery();
			String json = Jsonizer.toJson(resultSet, "customers");
			return json;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{}";
	}

    public String getCookies(Request req, Response res) {
		String query = "SELECT name\n" + "FROM cookies\n" + "ORDER BY name";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			String result = Jsonizer.toJson(rs, "cookies");
			return result;
		} 

		catch (SQLException e) {
			e.printStackTrace();
			return "{\"cookies\":[],\"error\":\"Database error occurred.\"}";
		}

	}

	public String getRecipes(Request req, Response res) {
		String query = "SELECT Name, IngredientName, Unit, Quantity\n" + "FROM IngredientInCookie JOIN cookies\n" + "orders BY cookies, ingredientInCookies";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			String result = Jsonizer.toJson(rs, "recipes");
			return result;
		} 

		catch (SQLException e) {
			e.printStackTrace();
			return "{\"recipes\":[],\"error\":\"Database error occurred.\"}";
		}
		
	}

    public String getPallets(Request req, Response res) {
        // Initial SQL query,
        String sql = "SELECT id, IF(blocked, 'TRUE', 'FALSE') AS blocked\n"+ "FROM pallets\n"+ "WHERE 1=1"; // A valid yet simple start to allow for dynamic string building.

        // ArrayList to hold parameters for prepared statement
        ArrayList<String> values = new ArrayList<>();

        // Handling the 'from' query parameter 
        String fromParam = req.queryParams("from");
        if (fromParam != null) {
            sql += " AND someColumn >= ?";
            values.add(fromParam);
        }

        // Handling the 'to' query parameter (date produced on or before)
String toParam = req.queryParams("to");
if (toParam != null) {
    sql += " AND production_date <= ?";
    values.add(toParam);
}

// Handling the 'cookie' query parameter
String cookieParam = req.queryParams("cookie");
if (cookieParam != null) {
    sql += " AND cookie = ?";
    values.add(cookieParam);
}

// Handling 'blocked' parameter

String blockedParam = req.queryParams("blocked");
if (blockedParam != null) {
    String blockedValue = blockedParam.equalsIgnoreCase("TRUE") ? "yes" : "no";
    sql += " AND blocked_bool_attr = ?";
    values.add(blockedValue);
} else {
    // If 'blocked' parameter is not provided, assume it's not blocked
    sql += " AND blocked_bool_attr = ?";
    values.add("no"); // Add as string
}

try (
    PreparedStatement stmt = conn.prepareStatement(sql)) {
// Set the values for the prepared statement
for (int i = 0; i < values.size(); i++) {
   stmt.setString(i + 1, values.get(i));
}

// Execute the query and handle the results
try (ResultSet rs = stmt.executeQuery()) {
   // Convert ResultSet to JSON object string using JSONizer
   String json = krusty.Jsonizer.toJson(rs, "pallets");
   return json;
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

					String[] tableNames = new String[]{"Storages", "ingredientName", "pallet_Delivered", "storageAmount", "cookies", "pallets", "customers", "storageUpdates", "ingredientInCookies", "orders", "orderSpec"};
					
					
					String clearTables = "TRUNCATE TABLE Storages; " +
					"TRUNCATE TABLE ingredientName; " +
					"TRUNCATE TABLE pallet_Delivered; " +
					"TRUNCATE TABLE storageAmount; " +
					"TRUNCATE TABLE cookies; " +
					"TRUNCATE TABLE pallets; " +
					"TRUNCATE TABLE customers; " +
					"TRUNCATE TABLE storageUpdates; " +
					"TRUNCATE TABLE ingredientInCookies; " +
					"TRUNCATE TABLE orders; " +
					"TRUNCATE TABLE orderSpec;";
		
			String insertCustomers = "INSERT INTO customers (name, Address) VALUES " +
					"('Bjudkakor AB', 'Ystad'), " +
					"('Finkakor AB', 'Helsingborg'), " +
					"('Gästkakor AB', 'Hässleholm'), " +
					"('Kaffebröd AB', 'Landskrona'), " +
					"('Kalaskakor AB', 'Trelleborg'), " +
					"('Partykakor AB', 'Kristianstad'), " +
					"('Skånekakor AB', 'Perstorp'), " +
					"('Småbröd AB', 'Malmö');";
		
			String insertCookies = "INSERT INTO cookies (name) VALUES " +
					"('Almond delight'), " +
					"('Amneris'), " +
					"('Berliner'), " +
					"('Nut cookie'), " +
					"('Nut ring'), " +
					"('Tango');";
		
			String insertIngredients = "INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES " +
					"(400, 'g', 'Butter', 'Almond delight'), " +
					"(279, 'g', 'Chopped almonds', 'Almond delight'), " +
					"(10, 'g', 'Cinnamon', 'Almond delight'), " +
					"(400, 'g', 'Flour', 'Almond delight'), " +
					"(270, 'g', 'Sugar', 'Almond delight'); " +
					
					""+
					 "INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES"+
					"(250, 'g', 'Butter', 'Amneris'), "+
					"(250, 'g', 'Eggs', 'Amneris'), "+
					"(750, 'g', 'Marzipan', 'Amneris'), "+
					"(25, 'g', 'Potato starch', 'Amneris'), "+
					"(25, 'g', 'Wheat flour', 'Amneris'); "+
					""+
					"INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES"+
					"(250, 'g', 'Butter', 'Berliner'), "+
					"(50, 'g', 'Chocolate', 'Berliner'), "+
				   " (50, 'g', 'Eggs', 'Berliner'), "+
				   " (350, 'g', 'Flour', 'Berliner'), "+
					"(100, 'g', 'Icing sugar', 'Berliner'), "+
					"(5, 'g', 'Vanilla sugar', 'Berliner'); "+
					""+
					"INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES"+
				  "(125, 'g', 'Bread crumbs', 'Nut cookie'), "+
				   " (50, 'g', 'Chocolate', 'Nut cookie'), "+
					"(350, 'ml', 'Egg whites', 'Nut cookie'), "+
					"(750, 'g', 'Fine-ground nuts', 'Nut cookie'), "+
					"(625, 'g', 'Ground, roasted nuts', 'Nut cookie'), "+
					"(375, 'g', 'Sugar', 'Nut cookie'); "+
					""+
				  "INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES"+
				   " (450, 'g', 'Butter', 'Nut ring'), "+
					"(450, 'g', 'Flour', 'Nut ring'), "+
					"(190, 'g', 'Icing sugar', 'Nut ring'), "+
					"(225, 'g', 'Roasted, chopped nuts', 'Nut ring'); "+
					""+
	
					"INSERT INTO ingredientInCookies (Quantity, Unit, ingredient_name, cookie_name) VALUES"+
					"(200, 'g', 'Butter', 'Tango'), "+
					"(300, 'g', 'Flour', 'Tango'), "+
					"(4, 'g', 'Sodium bicarbonate', 'Tango'), "+
					"(250, 'g', 'Sugar', 'Tango'), "+
					"(2, 'g', 'Vanilla', 'Tango'); "+
					""
					;
				
					Connection connection = null;
					try {
						connection = conn; // Ensure conn is appropriately managed and closed elsewhere
						connection.setAutoCommit(false);
						try (Statement statement = connection.createStatement()) {
							for (String tableName : tableNames) {
								statement.addBatch("TRUNCATE TABLE " + tableName + ";");
							}
							statement.addBatch(insertCookies);
							statement.addBatch(insertCustomers);
							statement.addBatch(insertIngredients);
							statement.executeBatch();
							connection.commit();
						} catch (SQLException e) {
							connection.rollback();
							throw e; // Rethrow exception after rollback
						}
					} finally {
						if (connection != null) connection.close(); // Make sure to close the connection if conn is not managed outside
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
                PreparedStatement insertPalletStmt = connection.prepareStatement(insertPalletSql,
                        Statement.RETURN_GENERATED_KEYS)) {

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