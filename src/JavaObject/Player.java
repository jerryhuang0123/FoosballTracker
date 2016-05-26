package JavaObject;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;

public class Player {
	private int playerID;
	private String firstName;
	private String lastName;
	private int winTotal = 0;
	private int lossTotal = 0;
	private int pointTotal = 0;
	private int givenUpPointTotal = 0;
	
	//getters and setters
	public int getPlayerID() {
		return playerID;
	}
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void Log(){
		System.out.println("PlayerID: " + playerID + " Player Name: " + firstName + " " + lastName);
	}
	
	public String LogString(){
		return "PlayerID: " + playerID + " Player Name: " + firstName + " " + lastName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getWinTotal() {
		return winTotal;
	}
	public void setWinTotal(int winTotal) {
		this.winTotal = winTotal;
	}
	public int getLossTotal() {
		return lossTotal;
	}
	public void setLossTotal(int lossTotal) {
		this.lossTotal = lossTotal;
	}
	public int getPointTotal() {
		return pointTotal;
	}
	public void setPointTotal(int pointTotal) {
		this.pointTotal = pointTotal;
	}
	public int getGivenUpPointTotal() {
		return givenUpPointTotal;
	}
	public void setGivenUpPointTotal(int givenUpPointTotal) {
		this.givenUpPointTotal = givenUpPointTotal;
	}
	
	public String toString(){
		return "Player " + this.firstName + " " + this.lastName + " has " + this.winTotal + " wins, "
				+ this.lossTotal + " losses, scored " + this.pointTotal + " points in total and gave up " 
				+ this.givenUpPointTotal + "points";
	}
}
