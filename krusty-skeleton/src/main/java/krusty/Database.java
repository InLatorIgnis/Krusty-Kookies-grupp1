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

import com.mysql.cj.protocol.Resultset;

import static krusty.Jsonizer.anythingToJson;
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
		return "{}";
	}

	public String getRawMaterials(Request req, Response res) {
		return "{}";
	}

	public String getCookies(Request req, Response res) {
		String query = "SELECT Name\n" + "FROM Cookie\n" + "ORDER BY Name";

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
		String query = "SELECT Name, IngredientName, Unit, Quantity\n" + "FROM IngredientInCookie JOIN Cookie\n" + "ORDER BY Cookie, IngredientInCookie";

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
		/*try(PreparedStatement conn = DriverManager.getConnection(jdbcString, jdbcUsername, jdbcPassword); 
		PreparedStatement resetAll = conn.prepareStatement(clearTables)) {

		} catch(SQLException e) {
			e.printStackTrace(); 

			
		}*/
		return "{}"; 
	}

	public String createPallet(Request req, Response res) {
		return "{}";
	}
}
