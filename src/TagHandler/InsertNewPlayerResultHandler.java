package TagHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.mysql.jdbc.exceptions.MySQLNonTransientConnectionException;
import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;

import JavaObject.DataLoader;
import JavaObject.Player;
import database.DatabaseConnector;

public class InsertNewPlayerResultHandler extends SimpleTagSupport{
	
	private String newPlayerFirstName;
	private String newPlayerLastName;
	
	public void doTag() throws JspException, IOException{
		JspWriter out = getJspContext().getOut();
		DatabaseConnector connector = new DatabaseConnector();
		try {
			connector.AddNewPlayer(newPlayerFirstName, newPlayerLastName, true);
		} catch (SQLException e) {
			out.println("Failed to Add " + newPlayerFirstName + " " + newPlayerLastName);
		} catch(Exception e){
			e.printStackTrace();
		}
		out.println("Added successfully!");
		out.println("<br><br>");
		//if gets to here, addition is successful. Reload players
		int attempts = 3;
		try{
			ReloadPlayerList();
		}catch(Exception e){
			try {
				if(attempts > 0){
					Thread.sleep(3000);
					attempts--;
					ReloadPlayerList();
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		//Button to go back to home page
		out.println("<a href = '/JSPExample'>Go Back to Home Page</a><br>");
		
	}
	
	public void ReloadPlayerList(){
		DatabaseConnector connector = new DatabaseConnector();
		connector.LoadPlayers(true);
	}
	public String getNewPlayerFirstName() {
		return newPlayerFirstName;
	}

	public void setNewPlayerFirstName(String newPlayerFirstName) {
		this.newPlayerFirstName = newPlayerFirstName;
	}

	public String getNewPlayerLastName() {
		return newPlayerLastName;
	}

	public void setNewPlayerLastName(String newPlayerLastName) {
		this.newPlayerLastName = newPlayerLastName;
	}
}