package permissions.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import permissions.domain.Permission;

public class PermissionDbManager {

	private Connection connection;
	private String url = "jdbc:hsqldb:hsql://localhost/workdb";
	
	private String createPermissionTable =""
			+ "CREATE TABLE Permission("
			+ "id bigint GENERATED BY DEFAULT AS IDENTITY,"
			+ "name VARCHAR(20),"
			+ ")";
	
	private String insertSql ="INSERT INTO permission(name) VALUES(?)"; 
	private String selectSql ="SELECT * FROM permission";
	private String deleteSql = "DELETE FROM permission WHERE id=?";
	private String updateSql = "UPDATE permission SET (name)=(?) WHERE id=?";
	
	private PreparedStatement insert;
	private PreparedStatement select;
	private PreparedStatement delete;
	private PreparedStatement update;
	
	public PermissionDbManager(){
		
		try {
			connection = DriverManager.getConnection(url);
			
			insert = connection.prepareStatement(insertSql);
			select = connection.prepareStatement(selectSql);
			delete = connection.prepareStatement(deleteSql);
			update = connection.prepareStatement(updateSql);
			ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
			
			boolean tableExists =false;
			while(rs.next())
			{
				if(rs.getString("TABLE_NAME").equalsIgnoreCase("Permission")){
					tableExists=true;
					break;
				}
			}
			
			if(!tableExists){
				Statement createTable = connection.createStatement();
				createTable.executeUpdate(createPermissionTable);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void add(Permission permission){
		
		try {
			insert.setString(1, permission.getName());
			insert.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void deleteById(int id){
		
		try {
			delete.setInt(1, id);
			delete.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void update(Permission p){
		
		try {
			update.setString(1, p.getName());
			update.setInt(2, p.getId());
			update.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public List<Permission> getAll(){
		
		List<Permission> result = new ArrayList<Permission>();
		
		try {
			ResultSet rs = select.executeQuery();
			while(rs.next()){
				Permission permission = new Permission();
				permission.setName(rs.getString("name"));
				permission.setId(rs.getInt("id"));
				result.add(permission);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
}



























