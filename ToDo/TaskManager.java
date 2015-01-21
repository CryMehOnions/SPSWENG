import java.util.Iterator;

public class TaskManager implements Controller, Observer{
	private Model model;
	private View view;

	public TaskManager() {
		model = new TaskDatabaseModel(this);
		view = new TaskView(this);

		update(model.getTasks());
	}

	public void addTask(String taskName) {
		model.addTask(taskName);
	}

	public void clearComplete() {
		model.clearComplete();
	}

	public void setTask(String taskName, boolean isComp) {
		model.setTask(taskName, isComp);
	}

	public void update(Iterator tasks) {
		view.update(tasks);
	}

	public int getCount() {
		return model.countCompletedTasks();
	}

}
