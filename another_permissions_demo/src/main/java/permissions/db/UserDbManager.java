package permissions.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import permissions.domain.Person;
import permissions.domain.User;

public class UserDbManager {

	private Connection connection;
	private String url = "jdbc:hsqldb:hsql://localhost/workdb";
	
	private String createUserTable =""
			+ "CREATE TABLE User("
			+ "id bigint GENERATED BY DEFAULT AS IDENTITY,"
			+ "username VARCHAR(20),"
			+ "password VARCHAR(50)"
			+ ")";
	
	private String insertSql ="INSERT INTO user(username,password) VALUES(?,?)"; 
	private String selectSql ="SELECT * FROM user";
	private String deleteSql = "DELETE FROM user WHERE id=?";
	private String updateSql = "UPDATE user SET (username,password)=(?,?) WHERE id=?";
	
	private PreparedStatement insert;
	private PreparedStatement select;
	private PreparedStatement delete;
	private PreparedStatement update;
	
public UserDbManager(){
		
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
				if(rs.getString("TABLE_NAME").equalsIgnoreCase("user")){
					tableExists=true;
					break;
				}
			}
			
			if(!tableExists){
				Statement createTable = connection.createStatement();
				createTable.executeUpdate(createUserTable);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

public void add(User person){
	
	try {
		insert.setString(1, person.getUsername());
		insert.setString(2, person.getPassword());
		
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

public void update(User p){
	
	try {
		update.setString(1, p.getUsername());
		update.setString(2, p.getPassword());
		update.setInt(3, p.getId());
		update.executeUpdate();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	
}

public List<User> getAll(){
	
	List<User> result = new ArrayList<User>();
	
	try {
		ResultSet rs = select.executeQuery();
		while(rs.next()){
			User person = new User();
			person.setUsername(rs.getString("username"));
			person.setPassword(rs.getString("password"));
			person.setId(rs.getInt("id"));
			result.add(person);
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return result;
}
	
}