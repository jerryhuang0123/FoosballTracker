package server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import JavaObject.DataLoader;
import JavaObject.Player;
import database.DatabaseConnector;

/**
 * Servlet implementation class InsertNewTeamResult
 */
@WebServlet("/InsertNewTeamResult")
public class InsertNewTeamResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB DatabaseConnector connector;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertNewTeamResult() {
        super();
        // TODO Auto-generated constructor stub
    }

    @PreDestroy
    public void destruct(){
    	connector.CloseConnection();
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int newTeamID = connector.GetAutoIncrementValue("Team");
		String[] selectedCheckBoxes = request.getParameterValues("addPlayerToTeam");
		String newTeam = request.getParameter("team_name");
		try {
			if(selectedCheckBoxes != null && selectedCheckBoxes.length > 0 && selectedCheckBoxes.length <= 4){
				//construct playerList
				ArrayList<Player> playerList = new ArrayList<Player>();
				for(String playerId : selectedCheckBoxes){
					playerId = playerId.replaceAll("\\s","");
					playerList.add(DataLoader.GetPlayer(Integer.parseInt(playerId)));
				}
				//Check if a team with the following players already exists
				if(DataLoader.isTeamExisted(playerList)){
					System.out.println("Team already exists!");
					request.getRequestDispatcher("TeamAlreadyExist.jsp").forward(request, response);
				}
				//no such team exists
				else{
					//Add team into DB
					connector.AddNewTeam(newTeam);
					
					//Add players into team
					for(String playerId : selectedCheckBoxes){
						playerId = playerId.replaceAll("\\s","");
						connector.AddPlayerToTeam(newTeamID, Integer.parseInt(playerId));
					}
					request.getRequestDispatcher("InsertNewTeamResult.jsp").forward(request, response);
				}
				
			}
			else{
				request.getRequestDispatcher("InsertFailed.jsp").forward(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			request.getRequestDispatcher("InsertFailed.jsp").forward(request, response);
		}
		
		
	}

}
