package TagHandler;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import JavaObject.DataLoader;
import JavaObject.Player;

public class OneOnOneGameReportHandler extends SimpleTagSupport {
	public void doTag() throws JspException, IOException{
		JspWriter out = getJspContext().getOut();
		out.println("<h1>1v1 Game Report </h1> <br>");
		out.println("<form name = 'InsertNewTeamForm' method = 'post' action = 'OneOnOneGameReport'>");
		out.println("Winner:<br><br>");
		for(Player player: DataLoader.getIdToPlayerMap().values()){
			out.println("<input type='radio' name='Winner' value = '");
			out.println(player.getPlayerID() + "'>");
			out.println("ID: " + player.getPlayerID() + " Name:" + player.getFirstName() + " "
			+ player.getLastName() + "<br><br>");
		}
		out.println("Loser:<br><br>");
		for(Player player: DataLoader.getIdToPlayerMap().values()){
			out.println("<input type='radio' name='Loser' value = '");
			out.println(player.getPlayerID() + "'>");
			out.println("ID: " + player.getPlayerID() + " Name:" + player.getFirstName() + " "
			+ player.getLastName() + "<br><br>");
		}
		out.println("Winner Score(Max 10, Min 0): <br>");
		out.println("<input type = 'number' name='WinnerScore' min='0' max='10' value='10'><br>");
		out.println("Loser Score(Max 9, Min 0): <br>");
		out.println("<input type = 'number' name='LoserScore' min='0' max='9'><br>");
		out.println("<input type = 'submit' value = 'Submit'");
		out.println("</form>");
		
	}
}
