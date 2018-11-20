import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.*;

public class JDBCGUI extends JFrame implements ActionListener
	{
	
	private JButton viewCoursesButton, viewBookingsButton, addBookingButton;
	private JPanel jp, jp2;
	private JComboBox courseList;
	private JLabel memberLabel;
	private JTextField memberTextField;
	
	public JDBCGUI() 
	 {
		
		setTitle ("Course Bookings");
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setSize (600,300);
		setLocation (100,100);
		jp=new JPanel();
		jp2=new JPanel();
		
		viewCoursesButton=new JButton("View Courses");
    		viewCoursesButton.addActionListener(this);
    	
    		viewBookingsButton=new JButton("View Bookings");
    		viewBookingsButton.addActionListener(this);
    
    
    		jp.add(viewCoursesButton);
    		jp.add(viewBookingsButton);
    		
		
    		String[] course=new String[] {"Yoga", "Dance", "HIIT", "Pilates", "Spinning"};
    		courseList= new JComboBox(course);
		jp.add(courseList);
		courseList.addActionListener(this);
	    
		add(jp,BorderLayout.NORTH);
		
		addBookingButton=new JButton("Add Booking");
		addBookingButton.addActionListener(this);
		jp2.add(addBookingButton);
		
		memberLabel= new JLabel ("Member ID: ");
		jp2.add(memberLabel);
		memberTextField= new JTextField ("", 10);
		memberTextField.addActionListener(this);
		jp2.add(memberTextField);
		
		add(jp2,BorderLayout.SOUTH);
		
	 }
	
	    		
	 public void actionPerformed (ActionEvent event) {
		 
		Gym gym=new Gym();
		String memberno=memberTextField.getText();
		memberTextField.setText("");
		int memberID= Integer.parseInt (memberno);
		 
		 if (event.getSource()==viewCoursesButton) {
			 courseView(gym.viewCourses());
		 }
		 
		 else if(event.getSource()==viewBookingsButton) {
			 String selectedCourse = (String) courseList.getSelectedItem();
			 memberView(gym.viewMembers(selectedCourse));
		 }
		 
		 else if(event.getSource()==addBookingButton) {
			 String selectedCourse = (String) courseList.getSelectedItem();
			 gym.addBooking(gym.checkBooking(selectedCourse), selectedCourse, memberID);
		 }
	 }
	
	 
	 public void courseView (ArrayList<String[]> array)	
	 {
		 String [] headings= {"Course", "No of Bookings", "Max Capcity", "Instructor Name"};
		 String[][] table= new String[array.size()][];
		 table=array.toArray(table);
		 
		 this.setSize(600, 300);
	     this.setTitle("Course viewer");
	     
		 JTable display= new JTable(table,headings);
		 JScrollPane scroll= new JScrollPane (display);
		 add(scroll, BorderLayout.CENTER);
		 
		 setVisible(true);

	 }
	 
	 public void memberView (ArrayList<String[]> array)	
	 {
		 String [] headings= {"Course", "Member's Name", "Membership Number"};
		 String[][] table= new String[array.size()][];
		 table=array.toArray(table);
		 
		 this.setSize(600, 300);
	     this.setTitle("Member viewer");
	     
		 JTable display= new JTable(table,headings);
		 JScrollPane scroll= new JScrollPane (display);
		 add(scroll, BorderLayout.CENTER);
		 
		 setVisible(true);

	 }
	 
}
