package TagHandler;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import JavaObject.DataLoader;
import JavaObject.Player;

public class InsertNewTeamResultHandler extends SimpleTagSupport{
	

	public void doTag() throws JspException, IOException{
		JspWriter out = getJspContext().getOut();
		out.println("Congratulations! You have added a new team!");
		//Button to go back to home page
				out.println("<a href = '/JSPExample'>Go Back to Home Page</a><br>");
		
	}
}
