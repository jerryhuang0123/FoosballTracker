package database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.sql.Connection;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.eclipse.persistence.internal.descriptors.QueryArgument;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.sun.appserv.jdbc.DataSource;

import JavaObject.DataLoader;
import JavaObject.Player;
import JavaObject.Team;
import sun.net.www.content.audio.x_aiff;

import java.awt.List;
import java.security.spec.DSAGenParameterSpec;
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
	
	private String dbJNDILookUp = "java:comp/env/jdbc/sql3120305";
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
    
    public void AddPlayerToTeam(int teamID, int playerID) throws SQLException{
    	StringBuilder query = new StringBuilder("INSERT INTO TeamInfo (TeamID, PlayerID) VALUES('");
    	query.append(teamID);
    	query.append("', '");
    	query.append(playerID);
    	query.append("');");
    	System.out.println(query.toString());
    	DatabaseQuery(query.toString(), false);
    	
    	//Load it locally
    	DataLoader.AddPlayerToTeam(DataLoader.GetPlayer(playerID), DataLoader.GetTeam(teamID));
    	
    	CloseConnection();
    }
    
    public void AddNewTeam(String teamName) throws SQLException{
    	StringBuilder query = new StringBuilder("INSERT INTO Team (TeamName) VALUES('");
    	query.append(teamName);
    	query.append("');");
    	System.out.println(query.toString());
    	System.out.println("Adding new team " + teamName);
    	DatabaseQuery(query.toString(), false);
    	
    	CloseConnection();
    	//reload teams
    	LoadTeams();
    }
    
    public void UpdateTeamScore(Team winnerTeam, Team loserTeam, int winnerPointTotal, int loserPointTotal) throws SQLException{
    	StringBuilder query = new StringBuilder();
    	for(Player player: winnerTeam.getPlayerMap().keySet()){
    		query.setLength(0);
    		query.append("UPDATE Player SET ");
			query.append("WinTotal=");
			query.append(player.getWinTotal()+1);   			
    		query.append(", PointTotal=");
			query.append(player.getPointTotal() + winnerPointTotal);
			query.append(", GivenUpPointTotal=");
			query.append(player.getGivenUpPointTotal() + loserPointTotal);
			query.append(" WHERE PlayerID=");
			query.append(player.getPlayerID());
			query.append(";");
			
	    	System.out.println(query.toString());
	    	DatabaseQuery(query.toString(), false);
    	}
    	
    	for(Player player: loserTeam.getPlayerMap().keySet()){
        	query.setLength(0);
    		query.append("UPDATE Player SET ");
			query.append("LossTotal=");
			query.append(player.getLossTotal() + 1);
    		query.append(", GivenUpPointTotal=");
			query.append(player.getGivenUpPointTotal() + winnerPointTotal);
			query.append(", PointTotal=");
			query.append(player.getPointTotal() + loserPointTotal);
			query.append(" WHERE PlayerID=");
			query.append(player.getPlayerID());
			query.append(";");		

	    	System.out.println(query.toString());
			DatabaseQuery(query.toString(), false);
    	}
    	//reload teams
		LoadTeams();
		LoadPlayers();
		//Update Game Table
		
    }
    
    public void UpdateGame(){
    	
    }
    
    public void AddNewTeam(Player player) throws SQLException{
    	int newTeamID = GetAutoIncrementValue("Team");
    	StringBuilder query = new StringBuilder("INSERT INTO Team(TeamName) VALUES('");
    	query.append(player.getFirstName() + " " + player.getLastName());
    	query.append("');");
    	System.out.println(query.toString());
    	DatabaseQuery(query.toString(), false);
    	//reload Teams
    	LoadTeams();
    	//add player to team 
    	AddPlayerToTeam(newTeamID, player.getPlayerID());
    	
    	CloseConnection();
    }
    
    public void LoadTeams(){
    	ResultSet rSet;
    	//Clear out data for teams
    	DataLoader.ClearTeams();
    	StringBuilder sBuilder = new StringBuilder("SELECT Team.TeamID, TeamInfo.PlayerID, Team.TeamName ");
    	sBuilder.append(" FROM Team LEFT JOIN TeamInfo ");
    	sBuilder.append("ON Team.TeamID = TeamInfo.TeamID");
    	System.out.println(sBuilder.toString());
    	try{
    		rSet = DatabaseQuery(sBuilder.toString(), true);
    		while(rSet.next()){
				//Create the team and store in DataLoader if DataLoader does not have the team
    			if(DataLoader.GetTeam(rSet.getInt("TeamID")) == null){
    				Team teamToAdd = new Team();
    				teamToAdd.setTeamID(rSet.getInt("TeamID"));
    				teamToAdd.setTeamName(rSet.getString("TeamName"));
    				DataLoader.AddTeam(teamToAdd);
    			}
    			//load player into the team
    			Player player = DataLoader.GetPlayer(rSet.getInt("PlayerID"));
    			Team team = DataLoader.GetTeam(rSet.getInt("TeamID"));
    			if(player != null && team != null){
    				System.out.println("Adding player " + player.LogString() + " to team " + team.getTeamName());
        			DataLoader.AddPlayerToTeam(player, team);
    			}
    			else{
    				System.out.println("ERROR: Cannot add player to team because of null");
    			}
    			
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		CloseConnection();
    	}
    }
    
    public void LoadPlayers(){
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
			CloseConnection();
		}
    }
    
    public ResultSet DatabaseQuery(String SQLQuery, boolean isSelectionQuery) throws SQLException{
    	ResultSet set = null;
		while(conn == null || conn.isClosed()){
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
    
    public int GetAutoIncrementValue(String tableName){
    	StringBuilder query = new StringBuilder("SHOW TABLE STATUS FROM `");
    	query.append(dbName);
    	query.append("` ");
    	query.append(" LIKE '");
    	query.append(tableName);
    	query.append("';");
    	System.out.println(query.toString());
    	ResultSet set;
		try {
			
			set = DatabaseQuery(query.toString(), true);
			if(set.next()){
				System.out.println("Auto_Increment value for table " + tableName + " is " + set.getInt("Auto_increment"));
		    	return set.getInt("Auto_increment");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
		return 0;
    }
    
    public void CloseConnection(){
    	try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Cannot close connection");
		}
    }
}
