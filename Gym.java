import java.awt.BorderLayout;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
public class Gym extends JFrame	 {
	
	
		//instance variable for connection
		private Connection connection=null;
		
		// method for establishing connection
		public Gym() 
		{
		    
			String dbname="m_17_2078855h";
			String username="m_17_2078855h";
			String password="2078855h";
		
			try {
				connection= DriverManager.getConnection("jdbc:postgresql://yacata.dcs.gla.ac.uk:5432/"+dbname,username,password);
			}
			catch (SQLException e)	{
				System.err.println("Connection Failed");
				e.printStackTrace();
				return;
			}
			
			if(connection!=null)	{
				System.out.println("Connection successful!");
			}
			else {
				System.err.println("Failed to make connection!");
			}
		}
		
		public ArrayList<String[]> viewCourses ()	{
			
			
			Statement stmt=null;
			
			String query= "SELECT coursename, booking, maxcapacity, firstname, lastname "
					+ "FROM gym.course "
					+ "LEFT JOIN gym.instructor ON (course.instructor=instructor.instructorID) ";
			
			ArrayList<String[]>list=new ArrayList<String[]>();
			
			try {
				   
				stmt=connection.createStatement();
				ResultSet rs=stmt.executeQuery(query);
				
				JDBCGUI gui=new JDBCGUI();
				
				while (rs.next())	{
					String coursename=rs.getString("coursename");
					int booking=rs.getInt("booking");
					int maxcapacity=rs.getInt("maxcapacity");
					String firstname=rs.getString("firstname");
					String lastname=rs.getString("lastname");
					String fullname=firstname+" "+lastname;
					String table[]= {coursename, booking+"", maxcapacity+"", fullname};
					list.add(table);
				}
				
			}
			catch (SQLException e)	{
				e.printStackTrace();
				System.err.println("error executing query"+query);
			}
			return list;
		}
		
		public ArrayList<String[]> viewMembers (String courseinput)	{
			
			String message="WHERE coursename='"+courseinput+"' ";
			Statement stmt=null;
			
			String query= "SELECT firstname, lastname, membershipnumber, coursename "
					+ "FROM gym.member "
					+ "INNER JOIN gym.membercourse ON (member.membershipnumber=membercourse.memberid) "
					+ "INNER JOIN gym.course ON (membercourse.courseid=course.courseid) +message";
			
			ArrayList<String[]>list=new ArrayList<String[]>();
			
			try {
				
				stmt=connection.createStatement();
				ResultSet rs=stmt.executeQuery(query);
				while (rs.next())	{
					String firstname=rs.getString("firstname");
					String lastname=rs.getString("lastname");
					String fullname=firstname+" "+lastname;
					String coursename=rs.getString("coursename");
					String membershipnumber=rs.getString("membershipnumber");
					String table[]= {coursename, fullname, membershipnumber};
					list.add(table);
				}
			}
			catch (SQLException e)	{
				e.printStackTrace();
				System.err.println("error executing query"+query);
			}
			return list;
		}
		
		
		public boolean checkBooking (String courseinput)	{
			
			Statement stmt=null;
			int counter=0;
			int courseid=0;
			
			// identifies course id from coursename
			String query="SELECT courseid FROM gym.course where coursename='"+courseinput+"'";
			try {
				stmt=connection.createStatement();
				ResultSet rs=stmt.executeQuery(query);
				while (rs.next())	{
					courseid=rs.getInt("courseid");	
					System.out.println(courseid);
				}
			}
			catch (SQLException e)	{
				e.printStackTrace();
			}
			
			// checks for the number people booked to a course
			String query2= "SELECT COUNT (bookingnumber) AS booking FROM gym.membercourse "
					+ "WHERE courseid= "+courseid;
			try {
				
				stmt=connection.createStatement();
				ResultSet rs=stmt.executeQuery(query2);
				while (rs.next())	{
					counter=rs.getInt("booking");
					System.out.println("There are: "+counter+" people booked on to this course.");
				}
			}
			
			catch (SQLException e)	{
				e.printStackTrace();
			}
			
			// updates course booking capacity
			// booking has a constraint where it cannot be> max capacity
			// returns false if booking capacity is full
			String query3= "UPDATE gym.course SET booking="+(counter)+" WHERE courseid="+courseid;
			
			try {
				stmt=connection.createStatement();
				int rs=stmt.executeUpdate(query3);
				System.out.println("Space available.");
			}
			catch (SQLException e)	{
				System.err.println("Fully booked.");
				return false;
			}
			counter=counter+1;
			return true;
		}
		
			public void addBooking (boolean book, String courseinput, int membernumber)	{
			
			Statement stmt=null;
			int courseid=0;
			int bookingnumber=0;
			
			System.out.println(book);
			
			// performs queries only if booking capacity is available
			// selecting courseid from course name
			if (book==true) {
		
			String query="SELECT courseid FROM gym.course where coursename='"+courseinput+"'";
			try {
				stmt=connection.createStatement();
				ResultSet rs=stmt.executeQuery(query);
				while (rs.next())	{
					courseid=rs.getInt("courseid");	
					System.out.println(courseid);
				}
			}
				catch (SQLException e)	{
					e.printStackTrace();
				}
			
			// detecting next booking number to use
			String query2="SELECT MAX(courseid) as nextbook FROM gym.membercourse";
			try {
				stmt=connection.createStatement();
				ResultSet rs=stmt.executeQuery(query2);
				while (rs.next())	{
					bookingnumber=rs.getInt("nextbook")+1;	
					System.out.println(bookingnumber);
				}
			}
			
			catch (SQLException e)	{
				e.printStackTrace();
			}
			
			// makes a booking 
			// @param membership number and selected course
			String query3= "INSERT INTO gym.membercourse VALUES("+membernumber+", "+courseid+", "+bookingnumber+")";
			try {
				stmt=connection.createStatement();
				int rs=stmt.executeUpdate(query3);
				
				System.out.println(membernumber +" was successfully booked into "+ courseinput+". The booking ID is: "+bookingnumber);
			}
			
			catch (SQLException e)	{
				e.printStackTrace();
				System.err.println("error executing query"+query3);
			}
			
			}
			
			else if (book==false)	{
				System.out.println("Cannot book, completely full!");
			}
			
			//
			
		}
}
