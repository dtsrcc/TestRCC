package Sensors;

import java.io.PrintStream;

import ch.ntb.inf.deep.runtime.mpc555.driver.MPIOSM_DIO;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_PWM;
import ch.ntb.inf.deep.runtime.ppc32.Task;

public class tictac extends Task{

	static MPIOSM_DIO Input1 = new MPIOSM_DIO(7, false); // false = Input
	static private TPU_PWM pwm1;
	
	
	
	
	public tictac() {
		
		pwm1 = new TPU_PWM(true, 7, 10000000, 0);
		System.out.println("Start");
	}
	
	public void action() {
		
		if (Input1.get() == true)  {
			pwm1.update(10000000/ TPU_PWM.tpuTimeBase);
			System.out.println("Switch state is now true: ");
		} else if (Input1.get() == false) {
			pwm1.update(0/ TPU_PWM.tpuTimeBase);
			System.out.println("Switch state is now false: ");
		}
			
			
		
	}
	
	
	
	static {
		
		// 1) Initialize SCI1 (9600 Bd,8N1)
		SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(19200, SCI.NO_PARITY, (short)8);
 
		// 2) Use SCI1 for stdout
		System.out = new PrintStream(sci1.out);
		
		
		Task t = new tictac();
		t.period = 1000;
		Task.install(t);
		
		
		
	}
	
}
