package Motors;

import java.io.PrintStream;

import ch.ntb.inf.deep.runtime.ppc32.Task;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_FQD;

public class FqdDemo extends Task {
	final byte tpuPin4 = 4;

	final boolean useTPUA = true;
	private TPU_FQD fqd;
	
	public FqdDemo(){
		fqd = new TPU_FQD(useTPUA, tpuPin4);
	}

	public void action() {
		int pos = fqd.getPosition();
		System.out.print('>');
		System.out.println(pos);
	}

	static {
		// 1) Initialize SCI1 (9600 8N1)
		SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(9600, SCI.NO_PARITY, (short)8);
 
		// 2) Use SCI1 for stdout
		System.out = new PrintStream(sci1.out);
	
		Task t = new FqdDemo();
		t.period = 2000;
		Task.install(t);
	}
}
