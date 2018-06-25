

import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_PWM;
import ch.ntb.inf.deep.runtime.ppc32.Task;

public class fadeLED extends Task{
	//Variables
	private TPU_PWM pwm1;
	private TPU_PWM pwm2;
	int mils = 200000;
	boolean dir = true;
	
	public fadeLED() {
		pwm1 = new TPU_PWM(true, 6, 10000000 / TPU_PWM.tpuTimeBase, 0);
		pwm2 = new TPU_PWM(true, 7, 10000000 / TPU_PWM.tpuTimeBase, 0);
	}
	
	public void action() {
		if (dir) {
			mils+=200000;
		} else {
			mils-=200000;
		}
		
		if (mils == 10000000) {
			dir = false;
		} else if (mils == 0) {
			dir = true;
		}
		pwm1.update(mils/ TPU_PWM.tpuTimeBase);
		pwm2.update(0/ TPU_PWM.tpuTimeBase);
	}
	
	static {
		Task pwm = new fadeLED();
		pwm.period = 50;
		Task.install(pwm);
	}
}
