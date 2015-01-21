public class DatabaseTask extends Task {
	private int taskID;

	public DatabaseTask(String taskName, boolean isComp, int taskID) {
		super(taskName, isComp);
		this.taskID = taskID;
	}

	public DatabaseTask(String taskName, int taskID) {
		super(taskName);
		this.taskID = taskID;
	}

	public int getID(){
		return taskID;
	}

	public void setID(int taskID) {
		this.taskID = taskID;
	}
}