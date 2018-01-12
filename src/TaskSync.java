import ch.ntb.inf.deep.runtime.ppc32.Task;
// taks counter 
public class TaskSync extends Task{

	public static int counter;
	
	public TaskSync() {
		period = 1000;
		Task.install(this);
		
		
	}
	
	public void action() {
		counter++;
		
	}

	

}
