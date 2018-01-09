package helloworld;

import java.io.PrintStream;

import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.ppc32.Task;

public class EmptyTask extends Task {
	
	public SCI sci1;
	int counter = 0;
	
	public void action() {
		 
		// 3) Say hello to the world
			System.out.println("Hello, world");
			System.out.println(counter);
			counter++;
	}
	
	static { // Klassenkonstruktor
	// 1) Initialize SCI1 (9600 8N1)
			SCI sci1 = SCI.getInstance(SCI.pSCI1);
			sci1.start(9600, SCI.NO_PARITY, (short)8);
				 
	// 2) Use SCI1 for stdout
			System.out = new PrintStream(sci1.out);
		
	Task et = new EmptyTask(); // Task erzeugen
	et.period = 1000; // Periodenlaenge des Tasks in ms
	 Task.install(et); // Task installieren
 }
}


