import java.sql.*;
import java.util.*;

public class TaskDatabaseModel implements Model, Observable {
	private Observer controller;
	private Connection con;

	public TaskDatabaseModel(Observer controller) {
		this.controller = controller;
		String user = "root"; //change if necessary
    	String pass = "p@ssword"; //change if necesary
    	String url = "jdbc:mysql://localhost:3306/task_database";

    	try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch(ClassNotFoundException err) {}//do nothing 

        try {
            con = DriverManager.getConnection(url, user, pass);
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch(SQLException err) {
        	con = null;
            System.out.println(err.getMessage());
        } //do nothing        
	}

	private int booleanToInt(boolean isComp) {
		if(isComp)
			return 1;
		else return 0;
	}

	private boolean intToBoolean(int isComp) {
		if(isComp == 1)
			return true;
		else return false;
	}

	public void addTask(String taskName) {
		PreparedStatement ps;

		try {
			ps = con.prepareStatement("INSERT INTO task(task_name, isComplete) VALUES (?, ?)");
			ps.setString(1, taskName);
			ps.setInt(2, 0);
			ps.executeUpdate();
			notifyController();
		}catch(SQLException err) {
			ps = null;
			System.out.println(err.getMessage());
		}
	}

	public void removeTask(String taskName) {
		PreparedStatement ps;

		try {
			ps = con.prepareStatement("DELETE FROM task WHERE task_name = ?");
			ps.setString(1, taskName);
			ps.executeUpdate();
			notifyController();
		}catch(SQLException err) {
			ps = null;
			System.out.println(err.getMessage());
		}
	}

	public void setTask(String taskName, boolean isComp) {
		PreparedStatement ps;

		try{
			ps = con.prepareStatement("UPDATE task SET isComplete = ? WHERE task_name = ?");
			ps.setInt(1, booleanToInt(isComp));
			ps.setString(2, taskName);
			ps.executeUpdate();
			notifyController();
		} catch(SQLException err) {
			ps = null;
			System.out.println(err.getMessage());
		}
	}

	public void clearComplete() {
		PreparedStatement ps;

		try{
			ps = con.prepareStatement("DELETE FROM task WHERE isComplete = 1");
			ps.executeUpdate();
			notifyController();
		} catch(SQLException err) {
			ps = null;
			System.out.println(err.getMessage());
			err.printStackTrace();
		}
	}

	public int countCompletedTasks() {
		PreparedStatement ps;
		ResultSet rs;
		int count;

		try {
			ps = con.prepareStatement("SELECT COUNT(*) FROM task WHERE isComplete = 1");
			rs = ps.executeQuery();
			rs.first();
			count = rs.getInt("COUNT(*)");
		} catch(SQLException err) {
			ps = null;
			count = 0;
			System.out.println(err.getMessage());
		}

		return count;
	}

	public Iterator getTasks() {
		ArrayList<Task> temp = new ArrayList<Task>();

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM task");
			ResultSet rs = ps.executeQuery();

			for(rs.first(); !rs.isAfterLast(); rs.next()) 
				temp.add(new Task(rs.getString("task_name"), intToBoolean(rs.getInt("isComplete"))));
		} catch(SQLException err) {	
			System.out.println(err.getMessage()); //if no tasks in database it will still return an empty iterator
		}

		return temp.iterator();
	}

	public void notifyController(){
		controller.update(getTasks());
	}
}