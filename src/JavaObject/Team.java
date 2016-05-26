package JavaObject;

import java.util.HashMap;

public class Team {

	private int TeamID;
	private HashMap<Player, Integer> PlayerMap = new HashMap<Player,Integer>();
	private String TeamName;
	
	public int GetSize(){
		return PlayerMap.size();
	}
	
	public boolean isPlayerOnTeam(Player player){
		if(PlayerMap.containsKey(player) && PlayerMap != null)return true;
		else return false;
	}
	
	public void Log(){
		System.out.println("TeamID:" + TeamID + " TeamName:" + TeamName);
	}
	
	public void AddPlayer(Player player){
		if(PlayerMap.containsKey(player)) return;
		else{
			if(PlayerMap.size() < 4)PlayerMap.put(player, player.getPlayerID());
		}
	}
	
	public void RemovePlayer(Player player){
		if(PlayerMap.containsKey(player)) PlayerMap.remove(player);
	}
	
	public HashMap<Player, Integer> getPlayerMap() {
		return PlayerMap;
	}
	
	public void setPlayerMap(HashMap<Player, Integer> playerMap) {
		PlayerMap = playerMap;
	}
	public int getTeamID() {
		return TeamID;
	}
	public void setTeamID(int teamID) {
		TeamID = teamID;
	}
	public String getTeamName() {
		return TeamName;
	}
	public void setTeamName(String teamName) {
		TeamName = teamName;
	}
}
