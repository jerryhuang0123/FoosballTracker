package TagHandler;

import java.awt.List;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.sun.javafx.collections.MappingChange.Map;

import JavaObject.DataLoader;
import JavaObject.Player;
import database.DatabaseConnector;

public class InsertNewTeamHandler extends SimpleTagSupport{
	
	public void doTag() throws JspException, IOException{
		JspWriter out = getJspContext().getOut();
		out.println("<h1>INSERT NEW TEAM </h1> <br>");
		out.println("<form name = 'InsertNewTeamForm' method = 'post' action = 'InsertNewTeamResult'>");
		out.println("Team Name:<br>");
		out.println("<input type = 'text' name='team_name'><br>");
		out.println("Select Players to add to team (Max 4):<br><br>");
		for(Player player: DataLoader.getIdToPlayerMap().values()){
			out.println("<input type='checkbox' name='addPlayerToTeam' value = '");
			out.println(player.getPlayerID() + "'>");
			out.println("ID: " + player.getPlayerID() + " Name:" + player.getFirstName() + " "
			+ player.getLastName() + "<br><br>");
		}
		out.println("<input type = 'submit' value = 'Submit'");
		out.println("</form>");
		
	}

}