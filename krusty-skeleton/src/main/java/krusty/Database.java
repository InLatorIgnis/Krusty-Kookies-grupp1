package krusty;

import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
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
			String json = Jsonizer.toJson(resultSet, "customers");
			return json;
		} catch (SQLException e) {
			e.printStackTrace();
		}
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

		String clearTables = "TRUNCATE TABLE Storage"
		+ "TRUNCATE TABLE IngredientName"
		+ "TRUNCATE TABLE Pallet_Delivered"
		+ "TRUNCATE TABLE StorageAmount"
		+ "TRUNCATE TABLE Cookie"
		+ "TRUNCATE TABLE Pallet"
		+ "TRUNCATE TABLE Customer"
		+ "TRUNCATE TABLE StorageUpdate"
		+ "TRUNCATE TABLE IngredientInCookie"
		+ "TRUNCATE TABLE Order"
		+ "TRUNCATE TABLE OrderSpec";

		try(PreparedStatement resetAll = conn.prepareStatement(clearTables)) {
			ResultSet rs = resetAll.executeQuery(clearTables);
			defaultValuesCustomer(req, res);
			defaultValuesCookie(req, res);

		} catch(SQLException e) {
			e.printStackTrace();

			
		}
		return { 
			"status": "ok" 
		  };
	}

	/**
	 * @param req
	 * @param res
	 * @return
	 * @throws SQLException
	 */
	private String defaultValuesCustomer(Request req, Response res) throws SQLException {

		 String defaultValuesForCustomer = "INSERT INTO Customer (Name, Address ) VALUES (?,?)";

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
			return { 
				"status": "error" 
			  };
			  
		} finally {
			conn.setAutoCommit(true);
		}
		return { 
			"status": "ok" 
		  };
	}

	/**
	 * @param req
	 * @param res
	 * @return
	 * @throws SQLException
	 */
	private String defaultValuesCookie(Request req, Response res) throws SQLException {

		 //String defaultValuesForCustomer = "INSERT INTO Customer VALUES (?,?)";
		 //String defaultValuesForOrderSpec = "";
		 // String defaultValuesForOrder = "";
		 //String defaultValuesForIngredientInCookie = "INSERT INTO IngredientInCookie VALUES (?,?,?)";
		 //String defaultValuesForStorageUpdate = "";
		 //String defaultValuesForPallet = "";
		 String defaultValuesForCookie = "INSERT INTO Cookie (Name) VALUES (?)";
		 //String defaultValuesForStorageAmount = "";
		 //String defaultValuesForPallet_Delivered = "";
		 //String defaultValuesForIngredientName = "";


		String[] cookie = "Almond delight", "Amneris", "Berliner", "Nut cookie", "Nut ring", "Tango";


	   
	   try(PreparedStatement ps = conn.prepareStatement(backToDefaultValues)) {
		   ps.executeQuery(backToDefaultValues);

	   } catch(SQLException e) {
		   e.printStackTrace();

		   
	   }
	   return { 
		   "status": "ok" 
		 };
   }

	public String createPallet(Request req, Response res) {
		return "{}";
	}
}
