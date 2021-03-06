package com.shopping;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(name = "login", urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private String dburl,dbuname,dbpass;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init()
    {
    	dburl=getServletContext().getInitParameter("dburl");
    	dbuname=getServletContext().getInitParameter("dbuname");
    	dbpass=getServletContext().getInitParameter("dbpass");
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String uname=request.getParameter("username");
		String password=request.getParameter("password");
		PrintWriter out = response.getWriter();
		DAO dao=new DAO(dburl,dbuname,dbpass);
		ResultSet rs=null;
		try 
		{
			rs = dao.loginCheck(uname, password);
		} 
		catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(rs!=null)
		{
			User u=null;
			//Set the session
			try
			{
				u = new User(rs.getString("name"), rs.getString("uname"), rs.getString("gender"), rs.getString("choice"), rs.getString("password"));
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpSession session=request.getSession();
			session.setAttribute("user", u);
			session.setAttribute("dao", dao);
			
			//Then redirect to dashboard
			response.sendRedirect("dashboard.jsp");
		}
		else
		{
			out.println("<script type='text/javascript'>");
//			out.println("document.getElementById('duplicateUname').show()");
			out.println("alert('Wrong Login Credentials');");
			out.println("</script>");
			
			RequestDispatcher rd=request.getRequestDispatcher("index.html");  
	        rd.include(request, response);
	        
	        return;
		}
	}

}
