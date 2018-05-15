package Motors;

//import ch.ntb.inf.deep.runtime.mpc555.*;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_PWM;


public class ServoMotor {
	final boolean useTPUA = false;
	//final int pwmChannel = 8;
	final int pwmPeriode = 20000000 / TPU_PWM.tpuTimeBase;
	final int minHighTime = 1200000 / TPU_PWM.tpuTimeBase;
	long lastMovement = 0;
	int lastPos = 0;
	int speedValue = 50000;
	//final static int maxHighTime = 2200000 / TPU_PWM.tpuTimeBase;
	//final int offsetPerDegree = 0;// Ihr Code
	
	private TPU_PWM pwm;
	
	public ServoMotor(int pwmChannel, int defaultPos ) {
	pwm = new TPU_PWM(useTPUA, pwmChannel, pwmPeriode, minHighTime);
	int highTime = (1200000 + (defaultPos * 11111)) / TPU_PWM.tpuTimeBase;
	pwm.update(highTime);
	}
	
	/*public static void setPosition0() {
		int highTime = 1200000 / TPU_PWM.tpuTimeBase;
		pwm.update(highTime);
	}
	
	public static void setPosition10() {
		int highTime = 1311111 / TPU_PWM.tpuTimeBase;
		pwm.update(highTime);
	}
	
	public static void setPosition20() {
		int highTime = 1422222 / TPU_PWM.tpuTimeBase;
		pwm.update(highTime);
	}
	
	public static void setPosition60() {
		int highTime = 1866667 / TPU_PWM.tpuTimeBase;
		pwm.update(highTime);
	}
	
	public static void setPosition90() {
		int highTime = 2200000 / TPU_PWM.tpuTimeBase;
		pwm.update(highTime);
	}*/
	
	public void setPositionSlow(int degree)
	{
		if((lastMovement+speedValue)<System.currentTimeMillis()) { // 50000 equals 50ms
			int targetPos = (1200000 + (degree * 11111));
			int nextPos = 0;
			if (targetPos>lastPos) {
				nextPos = lastPos+11111;
			}
			else if (targetPos<lastPos) {
				nextPos = lastPos-11111;
			}
			else {
				nextPos = lastPos;
			}
			
			int highTime = nextPos / TPU_PWM.tpuTimeBase;
			pwm.update(highTime);
			lastPos = nextPos;
			lastMovement = System.currentTimeMillis();
		}	
	}
	
	public void setPosition(int degree)
	{
		int highTime = (1200000 + (degree * 11111)) / TPU_PWM.tpuTimeBase;
		pwm.update(highTime);
	}
	
}
