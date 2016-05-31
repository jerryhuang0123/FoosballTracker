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
		boolean isWinnerATeam = false;
		boolean isLoserATeam = false;
		int winnerID = -1;
		int loserID = -1;
		//Process form
		if(request.getParameter("Winner") != null){
			String winnerParam = request.getParameter("Winner").replaceAll("\\s","");
			winnerID = Integer.parseInt(winnerParam);
			isWinnerATeam = true;
		}
		if(request.getParameter("Loser") != null){
			String loserParam = request.getParameter("Loser").replaceAll("\\s","");
			loserID = Integer.parseInt(loserParam);
			isLoserATeam = true;
		}
		if(request.getParameter("WinnerPlayer") != null){
			String winnerPlayer = request.getParameter("WinnerPlayer").replaceAll("\\s", "");
			winnerID = Integer.parseInt(winnerPlayer);
		}
		if(request.getParameter("LoserPlayer") != null){
			String loserPlayer = request.getParameter("LoserPlayer").replaceAll("\\s", "");
			loserID = Integer.parseInt(loserPlayer);
		}
		int winnerScore = Integer.parseInt(request.getParameter("WinnerScore"));
		int loserScore = Integer.parseInt(request.getParameter("LoserScore"));
		System.out.println("WinnerTeam ID: " + winnerID + " Score: " + winnerScore);
		System.out.println("LoserTeam ID: " + loserID + " Score: " + loserScore);
		//get the teams/players
		Team winnerTeam = (isWinnerATeam) ? DataLoader.GetTeam(winnerID) : null;
		Team loserTeam = (isLoserATeam) ? DataLoader.GetTeam(loserID) : null;
		Player winnerPlayer = (!isWinnerATeam) ? DataLoader.GetPlayer(winnerID) : null;
		Player loserPlayer = (!isLoserATeam) ? DataLoader.GetPlayer(loserID) : null;

		//Update Stats for each player on each team
		try {
			if((winnerTeam != null && winnerPlayer != null) || (loserTeam != null && loserPlayer != null)){
				request.getRequestDispatcher("GameReportFailed.jsp").forward(request, response);
				return;
			}
			//case when both are teams
			if(winnerTeam != null && loserTeam != null){
				if(winnerID == loserID || winnerScore <= loserScore){
					request.getRequestDispatcher("GameReportFailed.jsp").forward(request, response);
					return;
				}
			}
			
			//case when winner is team/loser is player
			else if(winnerTeam != null && loserPlayer != null){
				//check if team exists with only that player
				if(!DataLoader.isTeamExisted(loserPlayer)){
					//if not, create that team with that player
					connector.AddNewTeam(loserPlayer);
				}
				loserTeam = DataLoader.GetTeam(loserPlayer);
			}
			//case when winner is player/loser is team
			else if(winnerPlayer != null && loserTeam != null){
				//check if team exists with only that player
				if(!DataLoader.isTeamExisted(winnerPlayer)){
					//if not, create that team with that player
					connector.AddNewTeam(winnerPlayer);
				}
				winnerTeam = DataLoader.GetTeam(winnerPlayer);
			}
			//case when both players
			else if(winnerPlayer != null && loserPlayer != null){
				//check if team exists with only that player
				if(!DataLoader.isTeamExisted(winnerPlayer)){
					//if not, create that team with that player
					connector.AddNewTeam(winnerPlayer);
				}
				//check if team exists with only that player
				if(!DataLoader.isTeamExisted(loserPlayer)){
					//if not, create that team with that player
					connector.AddNewTeam(loserPlayer);
				}
				winnerTeam = DataLoader.GetTeam(winnerPlayer);
				loserTeam = DataLoader.GetTeam(loserPlayer);
			}else{
				request.getRequestDispatcher("GameReportFailed.jsp").forward(request, response);
				return;
			}
			//Update 
			connector.UpdateTeamScore(winnerTeam, loserTeam, winnerScore, loserScore);
			connector.CloseConnection();
			request.getRequestDispatcher("GameReportSuccessful.jsp").forward(request, response);
		} catch (SQLException e) {
			request.getRequestDispatcher("GameReportFailed.jsp").forward(request, response);
			e.printStackTrace();
		}
	}

}
