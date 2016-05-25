package database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.sql.Connection;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.eclipse.persistence.internal.descriptors.QueryArgument;

import JavaObject.DataLoader;
import JavaObject.Player;
import JavaObject.Team;
import sun.net.www.content.audio.x_aiff;

import java.awt.List;
import java.sql.*;

/**
 * Session Bean implementation class DatabaseConnector
 */
@Stateless
@LocalBean
public class DatabaseConnector {
	private final String dbms = "mysql";
	private Connection conn = null;
	private String serverName = "sql3.freemysqlhosting.net";
	private String portNumber = "3306";
	private final String dbName = "sql3120305";
	private final String userName = "sql3120305";
	private final String password = "GIavDwjLqD";
    /**
     * Default constructor. 
     */
    public DatabaseConnector() {
    }
    

    public Connection getConnection(){
    	try{
    		Class.forName("com.mysql.jdbc.Driver");
    	}catch(ClassNotFoundException e){
    		e.printStackTrace();
    	}
    	try {
			Properties connectionProps = new Properties();
			connectionProps.put("user", this.userName);
			connectionProps.put("password", this.password);

			this.conn = DriverManager.getConnection(
			        "jdbc:" + this.dbms + "://" +
			        this.serverName +
			        ":" + this.portNumber + "/" + this.dbName, connectionProps);
			System.out.println("Successfully connected to DB!");
		} catch (SQLException e) {
			System.out.println("SQLException thrown at getConnection");
		}
    	return this.conn;
    }
    
    public void AddNewPlayer(String firstName, String lastName) throws SQLException{
    	StringBuilder query = new StringBuilder("INSERT INTO Player (FirstName, LastName) VALUES('");
    	query.append(firstName);
    	query.append("','");
    	query.append(lastName);
    	query.append("');");
		DatabaseQuery(query.toString(), false);
		CloseConnection();
    }
    
    public void LoadTeams(boolean isLoadFinished){
    	ResultSet rSet;
    	//Clear out data for teams
    	DataLoader.ClearTeams();
    	try{
    		rSet = DatabaseQuery("SELECT Team.TeamID, TeamInfo.PlayerID, Team.TeamName "
    	+ " FROM Team, TeamInfo " + "WHERE Team.TeamID = TeamInfo.TeamID", true);
    		while(rSet.next()){
    			//Check if team does not exist
    			if(DataLoader.GetTeam(rSet.getInt("TeamID")) == null){
    				//Create the team and store in DataLoader
    				Team teamToAdd = new Team();
    				teamToAdd.setTeamID(rSet.getInt("TeamID"));
    				teamToAdd.setTeamName(rSet.getString("TeamName"));
    				DataLoader.AddTeam(teamToAdd);
    			}
    			//load player into the team
    			Player player = DataLoader.GetPlayer(rSet.getInt("PlayerID"));
    			Team team = DataLoader.GetTeam(rSet.getInt("TeamID"));
    			if(player != null && team != null){
        			DataLoader.AddPlayerToTeam(player, team);
    			}
    			else{
    				System.out.println("ERROR: Cannot add player to team because of null");
    			}
    			
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		if(isLoadFinished)CloseConnection();
    	}
    }
    
    public void LoadPlayers(boolean isLoadFinished){
    	ResultSet rSet;
    	//clear out all data for players
    	DataLoader.ClearPlayers();
		try {
			rSet = DatabaseQuery("SELECT * FROM Player", true);
			while(rSet.next()){
				Player playerToAdd = new Player();
				playerToAdd.setFirstName(rSet.getString("FirstName"));
				playerToAdd.setLastName(rSet.getString("LastName"));
				playerToAdd.setPlayerID(rSet.getInt("PlayerID"));
				playerToAdd.setWinTotal(rSet.getInt("WinTotal"));
				playerToAdd.setLossTotal(rSet.getInt("LossTotal"));
				playerToAdd.setPointTotal(rSet.getInt("PointTotal"));
				playerToAdd.setGivenUpPointTotal(rSet.getInt("GivenUpPointTotal"));
				DataLoader.AddNewPlayer(playerToAdd);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(isLoadFinished)CloseConnection();
		}
    }
    
    public ResultSet DatabaseQuery(String SQLQuery, boolean isSelectionQuery) throws SQLException{
    	ResultSet set = null;
		if(conn == null){
			getConnection();
		}
		Statement statement = conn.createStatement();
		if(isSelectionQuery){
			set = statement.executeQuery(SQLQuery);
		}
		//No ResultSet is expected because this is not a selection type query!
		else{
			statement.executeUpdate(SQLQuery);
		}
		
    	
    	return set;
    }
    
    public void CloseConnection(){
    	try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Cannot close connection");
		}
    }
}
