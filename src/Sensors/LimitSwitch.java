package Sensors;

import ch.ntb.inf.deep.runtime.mpc555.driver.MPIOSM_DIO;

import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_PWM;


public class LimitSwitch {
	
	
	//public static boolean switch1 = false;

	
	// PWM's for LEDs
	//private TPU_PWM pwm;
	private MPIOSM_DIO Input;
		
	public LimitSwitch(int switchchannel) {
		
		// LED PWM init
		//pwm = new TPU_PWM(true, 8, pwmchannel, 0); // LED OFF
		Input = new MPIOSM_DIO(switchchannel, false); // false = Input
		
	}

	
	
	public boolean getSwitchInputs() {
		
		/* if (Input.get() != switch1) {
			switch1 = Input.get();
			System.out.print("Switch 1 state is now: ");
			System.out.println(Input.get()); */ 
		
			return Input.get();
		}
		
		/*if (switch1 == true) {
			pwm.update(10000000/ TPU_PWM.tpuTimeBase);
		} else {
			pwm.update(00000000/ TPU_PWM.tpuTimeBase);
			}	*/
		
}
			
			
	 
