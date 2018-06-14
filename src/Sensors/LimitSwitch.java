package Sensors;

import java.io.PrintStream;

import ch.ntb.inf.deep.runtime.mpc555.driver.MPIOSM_DIO;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_PWM;
import ch.ntb.inf.deep.runtime.ppc32.Task;



public class LimitSwitch extends Task {
	
	
	boolean switch1 = false;

	
	// PWM's for LEDs
	private TPU_PWM pwm;
	private MPIOSM_DIO Input;
		
	public LimitSwitch() {
		
		// LED PWM init
		
		Input = new MPIOSM_DIO(10, false); // false = Input
		
	}

	
	
	public void action() {
		
		
			switch1 = Input.get();
			System.out.print("Switch 1 state is now: ");
			System.out.println(Input.get());
		
		
		
		
	}
	
	static
	{
		SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(19200, SCI.NO_PARITY, (short)8);
		System.out = new PrintStream(sci1.out);
		
		
			Task t = new LimitSwitch();
			t.period = 500;
			Task.install(t);
		
		

	}
	
	
}
			
			
	 
