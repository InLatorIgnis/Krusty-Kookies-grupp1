package krusty;

import spark.Request;
import spark.Response;

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
public String reset(Request req, Response res) throws SQLException, FileNotFoundException {
    String disableForeignKeyChecks = "SET FOREIGN_KEY_CHECKS=0;";
    String enableForeignKeyChecks = "SET FOREIGN_KEY_CHECKS=1;";

    try (Statement stmt = conn.createStatement()) {
        conn.setAutoCommit(false);
        stmt.execute(disableForeignKeyChecks); // Disable foreign key checks

        // Each table, ready to be truncated
        String[] tablesToClear = {
            "pallet_Delivered",
            "cookies",
            "pallets",
            "storages",
            "customers",
            "storageUpdates",
            "ingredientInCookies",
            "orders",
            "orderSpec"
        };
		//Loop for TRUNCATING each table
        for (String table : tablesToClear) {
            String clearTableQuery = "TRUNCATE TABLE " + table;
            stmt.executeUpdate(clearTableQuery);
        }

        // Read SQL commands from file
        File sqlFile = new File("krusty-skeleton/src/main/resources/initial-data.sql");
        try (Scanner scanner = new Scanner(sqlFile)) {
            StringBuilder sql = new StringBuilder();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    sql.append(line);
                    if (line.endsWith(";")) {
                        // Execute SQL
                        stmt.executeUpdate(sql.toString());
                        // Clear StringBuilder for the next command
                        sql.setLength(0);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "{\"status\": \"error\", \"message\": \"SQL file not found\"}";
        }

        stmt.execute(enableForeignKeyChecks); // Enable foreign key checks
        conn.commit();

    } catch (SQLException e) {
        conn.rollback();
        e.printStackTrace();
        return "{\"status\": \"error\", \"message\": \"Failed to reset database\"}";
    } finally {
        conn.setAutoCommit(true);
    }
    return "{\"status\": \"ok\", \"message\": \"Database reset successful\"}";
}


	public String createPallet(Request req, Response res) {
		return "{}";
	}
}
