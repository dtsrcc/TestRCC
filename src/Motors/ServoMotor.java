package Motors;

//import ch.ntb.inf.deep.runtime.mpc555.*;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_PWM;


public class ServoMotor {
	final boolean useTPUA = true;
	final int pwmChannel = 0;
	final int pwmPeriode = 20000000 / TPU_PWM.tpuTimeBase;
	final static int minHighTime = 1200000 / TPU_PWM.tpuTimeBase;
	final static int maxHighTime = 2200000 / TPU_PWM.tpuTimeBase;
	//final int offsetPerDegree = 0;// Ihr Code
	
	private static TPU_PWM pwm;
	
	public ServoMotor() {
	pwm = new TPU_PWM(useTPUA, pwmChannel, pwmPeriode, minHighTime);
	}
	
	public static void setPosition1() {
		
		pwm.update(maxHighTime);
	}
	
	public static void setPosition2() {
		pwm.update(minHighTime);
	}
	
	static {
		
		new ServoMotor();
	}
}
