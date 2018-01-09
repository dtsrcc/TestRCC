

package helloworld;

import java.io.PrintStream;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.ppc32.Task;

public class HelloWorld {
	public static int i = 0;
	public static void sayHello() {
		System.out.println("Hello, world again.");
	}
	static {
		// 1) Initialize SCI1 (9600 8N1)
		SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(9600, SCI.NO_PARITY, (short)8);
 
		// 2) Use SCI1 for stdout
		System.out = new PrintStream(sci1.out);
 
		// 3) Say hello to the world
		System.out.println("Hello, world");
	}

}
