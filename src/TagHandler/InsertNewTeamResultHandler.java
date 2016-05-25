package TagHandler;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import JavaObject.DataLoader;
import JavaObject.Player;

public class InsertNewTeamResultHandler extends SimpleTagSupport{
	
	private String newTeamName;
	private ArrayList<Player> PlayerList = new ArrayList<Player>();
	
	public void doTag() throws JspException, IOException{
		JspWriter out = getJspContext().getOut();
		out.println("<h1>INSERT NEW TEAM </h1> <br>");
		out.println("<form name = 'InsertNewTeamForm' method = 'post' action = 'InsertNewTeamResult'>");
		out.println("Team Name:<br>");
		out.println("<input type = 'text' name='team_name'><br>");
		out.println("Select Players to add to team (Max 4):<br><br>");
		for(Player player : DataLoader.getLoadedPlayerList()){
			out.println("<input type='checkbox' name='addPlayerToTeam'>");
			out.println("ID: " + player.getPlayerID() + " Name:" + player.getFirstName() + " "
			+ player.getLastName() + "<br><br>");
		}
		out.println("<input type = 'submit' value = 'Submit'");
		out.println("</form>");
		
	}
}
