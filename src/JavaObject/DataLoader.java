package JavaObject;

import java.util.ArrayList;

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
	
	@PostConstruct
	public void LoadData(){
		if(databaseConnector == null){
			System.out.println("ERROR: DBConnector is null!");
		}
		else{
			loadedPlayerList = databaseConnector.getPlayerList();
			System.out.println("Players Loaded Successfully");
		}
	}
	
	public static void AddNewPlayer(Player newPlayer){
		loadedPlayerList.add(newPlayer);
	}
	
	public static ArrayList<Player> getLoadedPlayerList() {
		return loadedPlayerList;
	}
	
	public static void setLoadedPlayerList(ArrayList<Player> newPlayerList){
		loadedPlayerList = newPlayerList;
	}

}
