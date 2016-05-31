package server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import JavaObject.DataLoader;
import JavaObject.Player;
import JavaObject.Team;
import database.DatabaseConnector;

/**
 * Servlet implementation class OneOnOneGameResult
 */
@WebServlet("/OneOnOneGameReport")
public class OneOnOneGameReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OneOnOneGameReport() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("OneOnOneGameReport.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DatabaseConnector connector = new DatabaseConnector();
		//Process form
		String winnerParam = request.getParameter("Winner").replaceAll("\\s","");
		String loserParam = request.getParameter("Loser").replaceAll("\\s","");
		int winnerID = Integer.parseInt(winnerParam);
		int loserID = Integer.parseInt(loserParam);
		int winnerScore = Integer.parseInt(request.getParameter("WinnerScore"));
		int loserScore = Integer.parseInt(request.getParameter("LoserScore"));
		System.out.println("Winner ID: " + winnerID + " Score: " + winnerScore);
		System.out.println("Loser ID: " + loserID + " Score: " + loserScore);
		//get the players
		Player winner = DataLoader.GetPlayer(winnerID);
		Player loser = DataLoader.GetPlayer(loserID);
		//check if there is a team that exists with each player
		if (!DataLoader.isTeamExisted(winner)){
			//Create team with that one player
			CreateTeam(winner, connector);
		}
		if(!DataLoader.isTeamExisted(loser)){
			CreateTeam(loser, connector);
		}
		Team winnerTeam = DataLoader.GetTeam(winner);
		Team loserTeam = DataLoader.GetTeam(loser);
		//Update Stats for each player
		try {
			if(winner == loser || winnerScore <= loserScore){
				request.getRequestDispatcher("GameReportFailed.jsp").forward(request, response);
			}
			else{
				try{
					connector.UpdateTeamScore(winnerTeam, loserTeam,winnerScore, loserScore);
					connector.CloseConnection();
					request.getRequestDispatcher("GameReportSuccessful.jsp").forward(request, response);
				}catch(NullPointerException e){
					connector = new DatabaseConnector();
				}
			}
		} catch (SQLException e) {
			request.getRequestDispatcher("GameReportFailed.jsp").forward(request, response);
			e.printStackTrace();
		}
	}
	
	private void CreateTeam(Player player, DatabaseConnector connector){
		try {
			connector.AddNewTeam(player);
		} catch (SQLException e) {
			e.printStackTrace();
		}catch(NullPointerException npe){
			if(connector == null) connector = new DatabaseConnector();
		}
	}

}
