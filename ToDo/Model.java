import java.util.Iterator;

public interface Model {
	public void addTask(String taskName);
	public void setTask(String taskName, boolean isComp);
	public void removeTask(String taskName);
	public void clearComplete();
	public int countCompletedTasks();
	public Iterator getTasks();
}
