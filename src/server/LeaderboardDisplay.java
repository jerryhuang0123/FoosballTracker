package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

import com.sun.javafx.scene.paint.GradientUtils.Point;
import com.sun.tools.xjc.reader.gbind.Choice;
import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;

import JavaObject.DataLoader;
import JavaObject.Player;
import database.DatabaseConnector;

/**
 * Servlet implementation class LeaderboardDisplay
 */
@WebServlet("/LeaderboardDisplay")
public class LeaderboardDisplay extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	//VALID CHOICES
	private static final String WIN_PERCENTAGE = "WinPercentage";
	private static final String WIN_TOTAL = "WinTotal";
	private static final String POINT_DIFF = "PointDifference";
	
	@EJB DatabaseConnector connector;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LeaderboardDisplay() {
        super();
        // TODO Auto-generated constructor stub
    }
    
  ////////////Comparators///////////////
  	public Comparator<Player> WinPercentageComparator = new Comparator<Player>(){
  		@Override
  		public int compare(Player p1, Player p2){
  			double p1Percent = p1.getWinPercentage();
  			double p2Percent = p2.getWinPercentage();
  			if(p2Percent > p1Percent) return 1;
  			else if(p2Percent == p1Percent) return 0;
  			else return -1;
  		}

  	};
  	
  	public Comparator<Player> WinTotalComparator = new Comparator<Player>(){
  		@Override
  		public int compare(Player p1, Player p2){
  			return p2.getWinTotal() - p1.getWinTotal();
  		}

  	};
  	
  	public Comparator<Player> PointDifferenceComparator = new Comparator<Player>(){
  		@Override
  		public int compare(Player p1, Player p2){
  			return p2.getPointDifference() - p1.getPointDifference();
  		}
  	};
  	//////////////////////////////
  	
  	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("LeaderboardChoiceForm.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Process form from TeamGameReport.jsp
		String choice = request.getParameter("LeaderboardChoice").replaceAll("\\s","");
		if(choice != null){
			System.out.println("User selected choice: " + choice);
			ArrayList<Player> leaderboardList = GetLeaderBoardList(choice);
			request.setAttribute("leaderboardList", leaderboardList);
			request.getRequestDispatcher("LeaderboardDisplay.jsp").forward(request, response);
		}
		else{
			request.getRequestDispatcher("InsertFailed.jsp").forward(request, response);
		}
		
	}
	
	private ArrayList<Player> GetLeaderBoardList(String choice){
		ArrayList<Player> leaderboardList = new ArrayList<Player>(DataLoader.getIdToPlayerMap().values());
		switch(choice){
		case WIN_PERCENTAGE:
			Collections.sort(leaderboardList, WinPercentageComparator);
			break;
			///
		case WIN_TOTAL:
			Collections.sort(leaderboardList, WinTotalComparator);
			break;
			///
		case POINT_DIFF:
			Collections.sort(leaderboardList, PointDifferenceComparator);
			break;
			///
		default:
			System.out.println("Invalid Choice " + choice);
			return null;
		}
		return leaderboardList;
	}

}
