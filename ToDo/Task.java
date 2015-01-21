public class Task {
	private String taskName;
	private boolean isComplete;

	public Task(String taskName, boolean isComplete) {
		this.taskName = taskName;
		this.isComplete = isComplete;
	}

	public Task(String taskName) {	//automatically creates unfinished task
		this(taskName, false);
	}

	public String getTaskName() {
		return taskName;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean comp) {
		isComplete = comp;
	}

	public void setName(String name) {
		taskName = name;
	}

	@Override
	public String toString() {
		return taskName;
	}
}
