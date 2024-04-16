package krusty;

import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import static krusty.Jsonizer.toJson;

public class Database {
	/**
	 * Modify it to fit your environment and then use this string when connecting to your database!
	 */
	private static final String jdbcString = "jdbc:mysql://localhost/krusty";

	// For use with MySQL or PostgreSQL
	private static final String jdbcUsername = "<CHANGE ME>";
	private static final String jdbcPassword = "<CHANGE ME>";

	public void connect() {
		// Connect to database here
		System.out.println("tjena");
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
