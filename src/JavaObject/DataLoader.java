package JavaObject;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import database.DatabaseConnector;

@Startup
@Singleton
public class DataLoader {

	
	//TODO: Add a Lock for Read and Write
	@EJB
	private DatabaseConnector databaseConnector;
	
	
	private static ArrayList<Player> loadedPlayerList = new ArrayList<Player>();
	private static ArrayList<Team> loadedTeamList = new ArrayList<Team>();
	private static HashMap<Integer, Player> IdToPlayerMap = new HashMap<Integer, Player>();
	private static HashMap<Integer, Team> IdToTeamMap = new HashMap<Integer, Team>();
	private static HashMap<Team, ArrayList<Player>> teamToPlayerMap = new HashMap<Team, ArrayList<Player>>();
	



	@PostConstruct
	public void LoadData(){
		if(databaseConnector == null){
			System.out.println("ERROR: DBConnector is null!");
		}
		else{
			databaseConnector.LoadPlayers(false);
			databaseConnector.LoadTeams(true); 
			System.out.println("Players Loaded Successfully");
			
		}
	}
	
	public static void ClearPlayers(){
		IdToPlayerMap.clear();
		loadedPlayerList.clear();
	}
	
	public static void ClearTeams(){
		IdToTeamMap.clear();
		loadedTeamList.clear();
		teamToPlayerMap.clear();
	}
	
	public static void AddTeam(Team team){
		if(!IdToTeamMap.containsKey(team))IdToTeamMap.put(team.getTeamID(), team);
		loadedTeamList.add(team);
	}
	
	public static void AddNewPlayer(Player newPlayer){
		loadedPlayerList.add(newPlayer);
		if(!IdToPlayerMap.containsKey(newPlayer.getPlayerID()))IdToPlayerMap.put(newPlayer.getPlayerID(), newPlayer);
	}
	
	//returns true if successful (ie. the team has less than 4 players)
	public static boolean AddPlayerToTeam(Player player, Team team){
		if(teamToPlayerMap.containsKey(team)){
			if(teamToPlayerMap.get(team).size() < 4){
				teamToPlayerMap.get(team).add(player);
				return true;
			}
		}
		return false;
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
	
	
	/////////////////GETTERS AND SETTERS
	public static ArrayList<Player> getLoadedPlayerList() {
		return loadedPlayerList;
	}
	
	public static void setLoadedPlayerList(ArrayList<Player> newPlayerList){
		loadedPlayerList = newPlayerList;
	}

	public static ArrayList<Team> getLoadedTeamList() {
		return loadedTeamList;
	}

	public static HashMap<Team, ArrayList<Player>> getPlayerToTeamMap() {
		return teamToPlayerMap;
	}
	public static void setLoadedTeamList(ArrayList<Team> loadedTeamList) {
		DataLoader.loadedTeamList = loadedTeamList;
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

}
