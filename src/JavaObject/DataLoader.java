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
	



	@PostConstruct
	public void LoadData(){
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
			IdToTeamMap.put(team.getTeamID(), team);
			loadedTeamList.add(team);
			System.out.println("Loaded team to DataLoader : " + team.getTeamName());
		}
	}
	
	public static void AddNewPlayer(Player newPlayer){
		loadedPlayerList.add(newPlayer);
		if(!IdToPlayerMap.containsKey(newPlayer.getPlayerID()))IdToPlayerMap.put(newPlayer.getPlayerID(), newPlayer);
	}
	
	//returns true if successful (ie. the team has less than 4 players)
	public static void AddPlayerToTeam(Player player, Team team){
		if(!team.isPlayerOnTeam(player)){
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
	public static ArrayList<Player> getLoadedPlayerList() {
		return loadedPlayerList;
	}
	
	public static void setLoadedPlayerList(ArrayList<Player> newPlayerList){
		loadedPlayerList = newPlayerList;
	}

	public static ArrayList<Team> getLoadedTeamList() {
		return loadedTeamList;
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
