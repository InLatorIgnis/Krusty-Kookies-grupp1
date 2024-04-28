package krusty;

import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static krusty.Jsonizer.toJson;

public class Database {

	/**
	 * Modify it to fit your environment and then use this string when connecting to your database!
	 */
	private static final String jdbcString = "jdbc:mysql://puccini.cs.lth.se/hbg03";   //"jdbc:mysql://localhost/krusty";

	// For use with MySQL or PostgreSQL
	private static final String jdbcUsername = "hbg03";
	private static final String jdbcPassword = "jav922za";
	
	//lagt till själv!!!!!!!!
	private Connection conn;
	// !!!!!!!!

	/**
     * Create the database object. Connection i performed later
     */
    public Database() {
        conn = null;
    }
	
	public void connect() {
		// Connect to database here
		try {
            conn = DriverManager.getConnection(jdbcString, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
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
			String json = Jsonizer.toJson(resultSet, "raw materials");
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
			String result = krusty.Jsonizer.toJson(rs, "cookies");
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return "{\"cookies\":[],\"error\":\"Database error occurred.\"}";
		}
	}

	public String getRecipes(Request req, Response res) {
		String query = "SELECT *\n" + "FROM ingredientInCookies\n" + "ORDER BY cookie_name";
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			String result = krusty.Jsonizer.toJson(rs, "recipes");
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return "{\"recipes\":[],\"error\":\"Database error occurred\"}";
		}
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

		String clearTables = 
		"TRUNCATE TABLE Storages"
		+ "TRUNCATE TABLE Cookies"
		+ "TRUNCATE TABLE Pallets"
		+ "TRUNCATE TABLE Customers"
		+ "TRUNCATE TABLE StorageUpdates"
		+ "TRUNCATE TABLE IngredientInCookies"
		+ "TRUNCATE TABLE Orders"
		+ "TRUNCATE TABLE Pallet_Delivered"
		+ "TRUNCATE TABLE OrderSpec";
		
		try(PreparedStatement resetAll = conn.prepareStatement("set FOREIGN_KEY_CHECKS = 0;")) {
			conn.setAutoCommit(false);
			resetAll.addBatch(clearTables);
			resetAll.addBatch("set FOREIGN_KEY_CHECKS = 1;");
			resetAll.executeUpdate();
			//getDefaultValuesCookie(req, res);
			//getDefaultValuesCustomer(req, res);
			//getDefaultValuesIngredientInCookie(req, res);
			//getDefaultValuesStorage(req, res);

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
	private String getDefaultValuesCustomer(Request req, Response res) throws SQLException {

		 String defaultValuesForCustomer = "INSERT INTO Customer (Name, Address) VALUES (?,?)";

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

		String defaultValuesForIngredientInCookie = "INSERT INTO IngredientInCookie(Quantity, Unit, IngredientName, Name) VALUES (?,?,?,?)";

		
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

		
		 String defaultValuesForCookie = "INSERT INTO Cookie (Name) VALUES (?)";

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

		String defaultValuesForStorage = "INSERT INTO Storage (IngredientName, StorageAmount) VALUES (?,?)";

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


   

	public String createPallet(Request req, Response res) {
		return "{}";
	}
}
