package server;

import java.io.IOException;
import java.sql.SQLException;

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
 * Servlet implementation class TeamGameReport
 */
@WebServlet("/TeamGameReport")
public class TeamGameReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TeamGameReport() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("TeamGameReport.jsp").forward(request, response);
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
		System.out.println("WinnerTeam ID: " + winnerID + " Score: " + winnerScore);
		System.out.println("LoserTeam ID: " + loserID + " Score: " + loserScore);
		//get the teams
		Team winnerTeam = DataLoader.GetTeam(winnerID);
		Team loserTeam = DataLoader.GetTeam(loserID);

		//Update Stats for each player on each team
		try {
			if(winnerID == loserID || winnerScore <= loserScore){
				request.getRequestDispatcher("GameReportFailed.jsp").forward(request, response);
			}
			else{
				try{
					connector.UpdateTeamScore(winnerTeam, true, winnerScore, loserScore);
					connector.UpdateTeamScore(loserTeam, false, loserScore, winnerScore);
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

}
