package krusty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import spark.Request;
import spark.Response;

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
		String selectCustomers = "select name, address\n" + "FROM customers;";
		try (
				PreparedStatement ps = conn.prepareStatement(selectCustomers);) {
			ResultSet resultSet = ps.executeQuery();
			String json = Jsonizer.toJson(resultSet, "customers");
			return json;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{}";
	}

	public String getRawMaterials(Request req, Response res) {
		String selectRawMaterials = "SELECT *\n" + "FROM storages\n" + "Order by ingredient_name;";
		try (
				PreparedStatement ps = conn.prepareStatement(selectRawMaterials);) {
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
		String query = "SELECT *\n" + "FROM ingredientInCookies\n" + "Order by cookie_name;";

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
		// Initial SQL query with WHERE 1=1
		String sql = "SELECT p.Pallet_id AS id, p.name AS cookie, p.productionDate AS production_date, " +
				"IFNULL(c.name, 'null') AS customer, IF(p.blocked, 'yes', 'no') AS blocked " +
				"FROM pallets p " +
				"LEFT JOIN pallet_Delivered pd ON p.Pallet_id = pd.Pallet_id " +
				"LEFT JOIN orders o ON pd.Order_id = o.Order_id " +
				"LEFT JOIN customers c ON o.customer_id = c.customer_id " +
				"WHERE 1=1 "; // This condition always evaluates to true

		// ArrayList to hold parameters for prepared statement
		ArrayList<String> values = new ArrayList<>();

		// Handling the 'from' query parameter (date produced on or after)
		String fromParam = req.queryParams("from");
		if (fromParam != null) {
			sql += " AND p.productionDate >= ?";
			values.add(fromParam);
		}

		// Handling the 'to' query parameter (date produced on or before)
		String toParam = req.queryParams("to");
		if (toParam != null) {
			sql += " AND p.productionDate <= ?";
			values.add(toParam);
		}

		// Handling the 'cookie' query parameter (filter by cookie name)
		String cookieParam = req.queryParams("cookie");
		if (cookieParam != null) {
			sql += " AND p.name = ?";
			values.add(cookieParam);
		}

		// Handling 'blocked' parameter (filter by blocked status)
		String blockedParam = req.queryParams("blocked");
		if (blockedParam != null) {
			String blockedValue = blockedParam.equalsIgnoreCase("yes") ? "1" : "0";
			sql += " AND p.blocked = ?";
			values.add(blockedValue);
		}

		// Add ORDER BY clause
		sql += " ORDER BY p.productionDate DESC;";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			// Set the values for the prepared statement
			for (int i = 0; i < values.size(); i++) {
				stmt.setString(i + 1, values.get(i));
			}

			// Execute the query and handle the results
			try (ResultSet rs = stmt.executeQuery()) {
				// Convert ResultSet to JSON
				String json = Jsonizer.toJson(rs, "pallets");
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

		String disableForeignKeyChecks = "SET FOREIGN_KEY_CHECKS=0;";
		String enableForeignKeyChecks = "SET FOREIGN_KEY_CHECKS=1;";
		String clearTables = "TRUNCATE TABLE pallet_Delivered; \n"
		+ "TRUNCATE TABLE cookies; \n"
		+ "TRUNCATE TABLE pallets; \n"
		+ "TRUNCATE TABLE storages; \n"
		+ "TRUNCATE TABLE customers; \n"
		+ "TRUNCATE TABLE storageUpdates; \n"
		+ "TRUNCATE TABLE ingredientInCookies; \n"
		+ "TRUNCATE TABLE orders; \n"
		+ "TRUNCATE TABLE orderSpec; \n";


		try(Statement stmt = conn.createStatement()) {
			System.out.println(clearTables);
			conn.setAutoCommit(false);
			stmt.addBatch(disableForeignKeyChecks);
			
			stmt.addBatch(clearTables);
			//getDefaultValuesCookie(req, res);
			//getDefaultValuesCustomer(req, res);
			//getDefaultValuesIngredientInCookie(req, res);
			//getDefaultValuesStorage(req, res);

			stmt.addBatch(enableForeignKeyChecks);
			stmt.executeBatch();
			
			conn.commit();
			System.out.println("clearTables");

		} catch(SQLException e) {
			conn.rollback();
			e.printStackTrace();
			return "{\"status\": \"error\"}";
			  
		} finally {
			
			conn.setAutoCommit(true);
		}
		return "{\"status\": \"ok\"}";
	}

	/**
	 * @param req
	 * @param res
	 * @return
	 * @throws SQLException
	 */
	private String getDefaultValuesCustomer(Request req, Response res) throws SQLException {

		 String defaultValuesForCustomer = "INSERT INTO customers (Name, Address) VALUES (?,?)";

		 Map<String, String> customer = new HashMap<>();
		 customer.put("Bjudkakor AB", "Ystad");
		 customer.put("Finkakor AB", "Helsingborg");
		 customer.put("GästKakor AB", "Hässleholm");
		 customer.put("Kaffebröd AB", "Landskrona");
		 customer.put("Kalaskakor AB", "Trelleborg");
		 customer.put("Partykakor AB", "Kristianstad");
		 customer.put("Skånekakor AB", "Perstorp");
		 customer.put("Småbröd AB", "Malmö");

		
		try(PreparedStatement ps = conn.prepareStatement(defaultValuesForCustomer)) {
			conn.setAutoCommit(false);

			StringBuilder sb = new StringBuilder();

			for(Map.Entry<String, String> customers : customer.entrySet()) {
				ps.setString(1, customers.getKey());
				ps.setString(2, customers.getValue());
				ps.executeUpdate();

			}

			conn.commit();

		} catch(SQLException e) {
			conn.rollback();
			e.printStackTrace();
			return "{\"status\": \"error\"}";
			  
		} finally {
			conn.setAutoCommit(true);
		}
		return "{\"status\": \"ok\"}";
	}

	/**
	 * @param req
	 * @param res
	 * @return
	 * @throws SQLException
	 */
	private String getDefaultValuesIngredientInCookie(Request req, Response res) throws SQLException {

		String defaultValuesForIngredientInCookie = "INSERT INTO IngredientInCookies(Quantity, Unit, ingredient_Name, cookie_name) VALUES (?,?,?,?)";

		
		Map<String, Map<String, Map<Integer, String>>> recipes = new HashMap<>();
		recipes.put("Almond delight", Map.of(
				"Butter", Map.of(400, "g"),
				"Chopped almonds", Map.of(279, "g"),
				"Cinnamon", Map.of(10, "g"),
				"Flour", Map.of(400, "g"),
				"Sugar", Map.of(270, "g")
		));
		recipes.put("Amneris", Map.of(
				"Butter", Map.of(250, "g"),
				"Eggs", Map.of(250, "g"),
				"Marzipan", Map.of(750, "g"),
				"Potato starch", Map.of(25, "g"),
				"Wheat flour", Map.of(25, "g")
		));
		recipes.put("Berliner", Map.of(
				"Butter", Map.of(250, "g"),
				"Chocolate", Map.of(50, "g"),
				"Eggs", Map.of(50, "g"),
				"Flour", Map.of(350, "g"),
				"Icing sugar", Map.of(100, "g"),
				"Vanilla sugar", Map.of(5, "g")
		));

		recipes.put("Nut cookie", Map.of(
    	"Bread crumbs", Map.of(125, "g"),
    	"Chocolate", Map.of(50, "g"),
   	    "Egg whites", Map.of(350, "ml"),
    	"Fine-ground nuts", Map.of(750, "g"),
    	"Ground, roasted nuts", Map.of(625, "g"),
   	 	"Sugar", Map.of(375, "g")
		));
		recipes.put("Nut ring", Map.of(
    	"Butter", Map.of(450, "g"),
    	"Flour", Map.of(450, "g"),
    	"Icing sugar", Map.of(190, "g"),
    	"Roasted, chopped nuts", Map.of(225, "g")
		));
		recipes.put("Tango", Map.of(
			"Butter", Map.of(200, "g"),
			"Flour", Map.of(300, "g"),
			"Sodium bicarbonate", Map.of(4, "g"),
			"Sugar", Map.of(250, "g"),
			"Vanilla", Map.of(2, "g")
		));
	   
	   try(PreparedStatement ps = conn.prepareStatement(defaultValuesForIngredientInCookie)) {
		   conn.setAutoCommit(false);

		   for (Map.Entry<String, Map<String, Map<Integer, String>>> recipeEntry : recipes.entrySet()) {
            String recipeName = recipeEntry.getKey();
            Map<String, Map<Integer, String>> ingredients = recipeEntry.getValue();

            for (Map.Entry<String, Map<Integer, String>> ingredientEntry : ingredients.entrySet()) {
                String ingredientName = ingredientEntry.getKey();
                Map<Integer, String> unitAndQuantity = ingredientEntry.getValue();

                for (Map.Entry<Integer, String> unitQuantityEntry : unitAndQuantity.entrySet()) {
                    int quantity = unitQuantityEntry.getKey();
                    String unit = unitQuantityEntry.getValue();

                    ps.setInt(1, quantity);
                    ps.setString(2, unit);
                    ps.setString(3, ingredientName);
                    ps.setString(4, recipeName);
                    ps.executeUpdate();
                }
            }
        }

		   conn.commit();

	   } catch(SQLException e) {
		   conn.rollback();
		   e.printStackTrace();
		   return "{\"status\": \"error\"}";
			 
	   } finally {
		   conn.setAutoCommit(true);
	   }
	   return "{\"status\": \"ok\"}";
   }

	/**
	 * @param req
	 * @param res
	 * @return
	 * @throws SQLException
	 */
	private String getDefaultValuesCookie(Request req, Response res) throws SQLException {

		
		 String defaultValuesForCookie = "INSERT INTO cookies (Name) VALUES (?)";

		String[] cookie = {"Almond delight", "Amneris", "Berliner", "Nut cookie", "Nut ring", "Tango"};


	   
	     try(PreparedStatement ps = conn.prepareStatement(defaultValuesForCookie)) {
		 conn.setAutoCommit(false);

			for(String c : cookie) {
				ps.setString(1, c);
				ps.executeUpdate();
			}
		   
			conn.commit();
	 	  } catch(SQLException e) {
			conn.rollback();
		    e.printStackTrace();
			return "{\"status\": \"error\"}";
		   
	   } finally {
		conn.setAutoCommit(true);
	   }
	   return "{\"status\": \"ok\"}";
   }

   /**
	 * @param req
	 * @param res
	 * @return
	 * @throws SQLException
	 */
	private String getDefaultValuesStorage(Request req, Response res) throws SQLException {

		String defaultValuesForStorage = "INSERT INTO storages (IngredientName, StorageAmount) VALUES (?,?)";

		String[] ingredients = {"Bread crumbs", "Butter", "Chocolate", "Chopped almonds", "Cinnamon",
		   "Egg whites", "Eggs", "Fine-ground nuts", "Flour", "Ground, roasted nuts", "Icing sugar",
		   "Marzipan", "Potato starch", "Roasted, chopped nuts", "Sodium bicarbonate", "Sugar",
		   "Vanilla sugar", "Vanilla", "Wheat flour"};

		   int amountInStock = 500000;

	   try(PreparedStatement ps = conn.prepareStatement(defaultValuesForStorage)) {
		   conn.setAutoCommit(false);

		   
	   	for (String ingredient : ingredients) {
		 ps.setString(1, ingredient);
	   	 ps.setInt(2, amountInStock);
	  	 ps.executeUpdate();
   }

		   conn.commit();

	   } catch(SQLException e) {
		   conn.rollback();
		   e.printStackTrace();
		   return "{\"status\": \"error\"}";
			 
	   } finally {
		   conn.setAutoCommit(true);
	   }
	   return "{\"status\": \"ok\"}";
   }


	// Todo by Martin
	public String createPallet(Request req, Response res) {
		String cookieName = req.queryParams("cookie");
		if (cookieName == null || cookieName.isEmpty()) {
			res.status(400); // Bad Request
			return "{\"status\":\"error\",\"message\":\"Missing or empty 'cookie' parameter\"}";
		}

		String checkCookieSql = "SELECT COUNT(*) FROM cookies WHERE name = ?";
		String insertPalletSql = "INSERT INTO pallets (productionDate, blocked, location, name) VALUES (NOW(), false, ?, ?)";
		String selectIngredientsSql = "SELECT ingredient_name, quantity FROM ingredientInCookies WHERE cookie_name = ?";
		String updateStoragesSql = "UPDATE storages SET storage_amount = storage_amount - ? WHERE ingredient_name = ?";

		try (
				PreparedStatement checkCookieStmt = conn.prepareStatement(checkCookieSql);
				PreparedStatement insertPalletStmt = conn.prepareStatement(insertPalletSql,
						Statement.RETURN_GENERATED_KEYS);
				PreparedStatement selectIngredientsStmt = conn.prepareStatement(selectIngredientsSql);
				PreparedStatement updateStoragesStmt = conn.prepareStatement(updateStoragesSql)) {

			conn.setAutoCommit(false); // Start transaction

			// Check if the specified cookie exists
			checkCookieStmt.setString(1, cookieName);
			try (ResultSet cookieResult = checkCookieStmt.executeQuery()) {
				if (cookieResult.next() && cookieResult.getInt(1) > 0) {
					// Get ingredients for the specified cookie
					selectIngredientsStmt.setString(1, cookieName);
					try (ResultSet ingredientsResult = selectIngredientsStmt.executeQuery()) {
						// Create the pallet
						int randomLocation = (int) (Math.random() * 99) + 1; // Random location between 1 and 99
						insertPalletStmt.setInt(1, randomLocation);
						insertPalletStmt.setString(2, cookieName);
						int affectedRows = insertPalletStmt.executeUpdate();
						if (affectedRows > 0) {
							// Retrieve and process generated pallet ID
							try (ResultSet generatedKeys = insertPalletStmt.getGeneratedKeys()) {
								if (generatedKeys.next()) {
									long palletId = generatedKeys.getLong(1);

									// Deduct ingredients from storages
									while (ingredientsResult.next()) {
										String ingredientName = ingredientsResult.getString("ingredient_name");
										int quantity = ingredientsResult.getInt("quantity");
										updateStoragesStmt.setInt(1, quantity);
										updateStoragesStmt.setString(2, ingredientName);
										updateStoragesStmt.addBatch();
									}

									// Execute batch update for storages
									updateStoragesStmt.executeBatch();

									conn.commit(); // Commit transaction
									res.status(201); // Created
									return String.format("{\"status\":\"ok\",\"id\":%d}", palletId);
								}
							}
						}
					}
				}
			}
			// If execution reaches here, handle errors
			conn.rollback(); // Rollback transaction
			res.status(500); // Internal Server Error
			return "{\"status\":\"error\",\"message\":\"Failed to create pallet\"}";
		} catch (SQLException e) {
			// Log the exception
			e.printStackTrace();
			res.status(500); // Internal Server Error
			return "{\"status\":\"error\",\"message\":\"An unexpected error occurred\"}";
		}
	}

}