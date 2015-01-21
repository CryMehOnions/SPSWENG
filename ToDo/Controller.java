public interface Controller {
	public void addTask(String taskName);
	public void clearComplete();
	public void setTask(String taskName, boolean isComp);
	public int getCount();
}