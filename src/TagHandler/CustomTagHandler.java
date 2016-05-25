package TagHandler;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;

import JavaObject.DataLoader;
import JavaObject.Player;

public class CustomTagHandler extends SimpleTagSupport{
	
	public void doTag() throws JspException, IOException{
		JspWriter out = getJspContext().getOut();
		out.println("Current Players in the DB:<br>");
		out.println("<ul>");
		for(Player player : DataLoader.getLoadedPlayerList()){
			out.println("<li>");
			out.println(player.getFirstName() + " " + player.getLastName() + "</li>");
		}
		out.println("</ul>");
		out.println("INSERT NEW PLAYER: <br>");
		out.println("<form name = 'InsertNewPlayerForm' method = 'post' action = 'InsertNewPlayerResult'>");
		out.println("First Name:<br>");
		out.println("<input type = 'text' name='first_name'><br>");
		out.println("Last Name: <br>");
		out.println("<input type = 'text' name = 'last_name'> ");
		out.println("<input type = 'submit' value = 'Submit'");
		out.println("</form>");
	}
}
