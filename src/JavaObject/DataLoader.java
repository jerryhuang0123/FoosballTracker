package JavaObject;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.sun.xml.ws.xmlfilter.PrivateElementFilteringStateMachine;

import database.DatabaseConnector;

@Startup
@Singleton
public class DataLoader {
	
	private DatabaseConnector databaseConnector;
	private static ArrayList<Player> loadedPlayerList = new ArrayList<Player>();
	private static ArrayList<Team> loadedTeamList = new ArrayList<Team>();
	private static HashMap<Integer, Player> IdToPlayerMap = new HashMap<Integer, Player>();
	private static HashMap<Integer, Team> IdToTeamMap = new HashMap<Integer, Team>();
	private static int NewestTeamID = 0;
	private static int NewestPlayerID = 0;



	@PostConstruct
	public void LoadData(){
		databaseConnector = DatabaseConnector.getInstance();
		if(databaseConnector == null){
			System.out.println("ERROR: DBConnector is null!");
		}
		else{
			databaseConnector.LoadPlayers();
			databaseConnector.LoadTeams(); 
			System.out.println("Players Loaded Successfully");
			
		}
		for(Team team : IdToTeamMap.values()){
			System.out.println("Loaded team :" + team.getTeamName());
		}
		
		NewestTeamID = databaseConnector.GetAutoIncrementValue("Team");
		NewestPlayerID = databaseConnector.GetAutoIncrementValue("Player");
		System.out.println("Loaded next team ID:" + NewestTeamID);
		System.out.println("Loaded next player ID:" + NewestPlayerID);
	}
	
	public static void ClearPlayers(){
		IdToPlayerMap.clear();
		loadedPlayerList.clear();
	}
	
	public static boolean isTeamExisted(ArrayList<Player> playerList){
		int count = 0;
		for(Team team: IdToTeamMap.values()){
			count = 0;
			for(Player player: playerList){
				if(!team.isPlayerOnTeam(player)){
					//reset count to 0 and go to the next team
					break;
				}
				else{
					System.out.println("Player " + player.getFirstName() + " exists in " + team.getTeamName());
					count++;
				}
			}
			if(count == playerList.size()) return true;
		}
		return false;
	}
	
	public static boolean isTeamExisted(Player player){
		for(Team team: IdToTeamMap.values()){
			if(team.GetSize() == 1 && team.isPlayerOnTeam(player)) return true;
		}
		return false;
	}
	
	public static void ClearTeams(){
		IdToTeamMap.clear();
		loadedTeamList.clear();
	}
	
	public static void AddTeam(Team team){
		if(!IdToTeamMap.containsKey(team.getTeamID())){
			if(team.getTeamID() > NewestTeamID) NewestTeamID = team.getTeamID();
			IdToTeamMap.put(team.getTeamID(), team);
			loadedTeamList.add(team);
			System.out.println("Loaded team to DataLoader : " + team.getTeamName());
		}
	}
	
	public static void AddNewPlayer(Player newPlayer){
		loadedPlayerList.add(newPlayer);
		if(newPlayer.getPlayerID() > NewestPlayerID) NewestPlayerID = newPlayer.getPlayerID();
		if(!IdToPlayerMap.containsKey(newPlayer.getPlayerID()))IdToPlayerMap.put(newPlayer.getPlayerID(), newPlayer);
	}
	
	//returns true if successful (ie. the team has less than 4 players)
	public static void AddPlayerToTeam(Player player, Team team){
		if(!team.isPlayerOnTeam(player)){
			System.out.println("DataLoader: Adding player : " + player.getFirstName() + " " + player.getLastName()
			+ " to team: " + team.getTeamName());
			team.AddPlayer(player);
		}
	}
	
	public static Player GetPlayer(int playerId){
		if(IdToPlayerMap.containsKey(playerId)){
			return IdToPlayerMap.get(playerId);
		}
		return null;
	}

	public static Team GetTeam(int teamId){
		if(IdToTeamMap.containsKey(teamId)){
			return IdToTeamMap.get(teamId);
		}
		return null;
	}
	
	public static Team GetTeam(ArrayList<Player> playerList){
		int count = 0;
		for(Team team: IdToTeamMap.values()){
			count = 0;
			for(Player player: playerList){
				if(!team.isPlayerOnTeam(player)){
					//reset count to 0 and go to the next team
					break;
				}
				else{
					count++;
				}
			}
			if(count == playerList.size()) return team;
		}
		return null;
	}
	
	public static Team GetTeam(Player player){
		for(Team team: IdToTeamMap.values()){
			if(team.GetSize() == 1 && team.isPlayerOnTeam(player)) return team;
		}
		return null;
	}
	
	/////////////////GETTERS AND SETTERS

	public static int getNewestTeamID(){
		return NewestTeamID;
	}
	
	public static void setNewestTeamID(int teamID){
		NewestTeamID = teamID;
	}
	
	public static int getNewestPlayerID(){
		return NewestPlayerID;
	}
	
	public static void IncrementNewestPlayerID(){
		NewestPlayerID++;
	}
	
	public static void IncrementNewestTeamID(){
		NewestTeamID++;
	}
	
	public static void setNewestPlayerID(int playerID){
		NewestPlayerID = playerID;
	}
	
	public static HashMap<Integer, Player> getIdToPlayerMap() {
		return IdToPlayerMap;
	}

	public static void setIdToPlayerMap(HashMap<Integer, Player> idToPlayerMap) {
		IdToPlayerMap = idToPlayerMap;
	}

	public static HashMap<Integer, Team> getIdToTeamMap() {
		return IdToTeamMap;
	}

	public static void setIdToTeamMap(HashMap<Integer, Team> idToTeamMap) {
		IdToTeamMap = idToTeamMap;
	}
	public static ArrayList<Player> getLoadedPlayerList() {
		return loadedPlayerList;
	}

}
