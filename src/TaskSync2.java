import java.io.PrintStream;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.ppc32.Task;
// task auf counter zugreifen

public class TaskSync2 extends Task{
	
	public TaskSync2() {
		new TaskSync();
		period = 2000;
		Task.install(this);
		
	}
	
	public void action() {
		// 3) print counter
		System.out.println(TaskSync.counter);
		
	}
	
	static {
		// 1) Initialize SCI1 (9600 8N1)
		SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(9600, SCI.NO_PARITY, (short)8);
		
		// 2) Use SCI1 for stdout
		System.out = new PrintStream(sci1.out);
		
		System.out.println("start");
		
		new TaskSync2();		
		
	}
}