package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;

import database.DatabaseConnector;


/**
 * Servlet implementation class ExampleServlet
 */
@WebServlet("/ExampleServlet")
public class ExampleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB ExampleBean bean;
	@EJB DatabaseConnector connector;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExampleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // TODO Auto-generated constructor stub
		
		PrintWriter out = response.getWriter();
		out.print("<html><body>");
		out.print("<h1>Request served at: " + request.getServletPath() + "</h1>");
		connector.getPlayerList();
		out.print("<h2> sad </h2>");
		out.print("</html></body>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
