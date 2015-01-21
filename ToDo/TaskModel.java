import java.util.*;

public class TaskModel implements Model, Observable {
	private ArrayList<Task> taskList;
	private Observer controller;

	public TaskModel(Observer controller) {
		this.controller = controller;
		taskList = new ArrayList<Task>();
	}

	/*** Finds and Returns the Specific Task Object given a String, 
	returns null if not in the list ***/
	private Task findTask(String task) {
		for(Task t: taskList)
			if(t.getTaskName() == task)
				return t;
		return null;
	}

	public void addTask(String taskName) {
		taskList.add(new Task(taskName));
		notifyController();
	}

	public void removeTask(String taskName) {
		taskList.remove(findTask(taskName));
		notifyController();
	}

	public void setTask(String taskName, boolean chk) {
		Task task = findTask(taskName);
		int index = taskList.indexOf(task);
		taskList.get(index).setComplete(chk);
		notifyController();
	}

	//returns list of tasks
	public Iterator getTasks() {
		return taskList.iterator();
	}

	//remove completed tasks from list
	public void clearComplete() {
		ArrayList<Task> newList = new ArrayList<Task>();
		for(Task t: taskList)
			if(!t.isComplete())
				newList.add(t);

		taskList = newList;
		notifyController();
	}

	//counts completed tasks
	public int countCompletedTasks() {
		ArrayList<Task> completeTasks = new ArrayList<Task>();

		for(Task t: taskList)
			if(t.isComplete())
				completeTasks.add(t);
		
		return completeTasks.size();
	}

	public void notifyController() {
		controller.update(getTasks());
	}
}