package Motors;

import java.io.PrintStream;

import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_PWM;


public class DCMotor {
	private static DCMotor dcm;  // Diese Klasse hat eine Instanz von sich selber

	private final boolean useTPUA =false;
	private final int chnL = 12, chnR = 13;
	private final static int pwmPeriod = 50000 / TPU_PWM.tpuTimeBase;

	private int currHightimeL, currHightimeR;
	private TPU_PWM pwmL, pwmR;
	
	
	DCMotor() {
	// PWM-Kanaele initialisieren
		currHightimeL = 0;
		currHightimeR = 0;
		pwmL = new TPU_PWM(useTPUA, chnL, pwmPeriod, currHightimeL);
		pwmR = new TPU_PWM(useTPUA, chnR, pwmPeriod, currHightimeR);
	}
	
	
	/*public static void leftFull() {
		dm.update(pwmPeriod, 0);
	}
	
	public static void leftHalf() {
		dm.update(pwmPeriod/2, 0);
	}
	
	public static void rightFull() {
		dm.update(0, pwmPeriod);
	}
	
	public static void rightHalf() {
		dm.update(0, pwmPeriod/2);
	}*/
	
	public void stop()
	{
		dcm.update(0, 0);
	}
	
	public void driveForward(int i)
	{
		dcm.update(pwmPeriod/i, 0);
	}
	
	public void driveReverse(int i)
	{
		dcm.update(0, pwmPeriod/i);
	}
	
	
	private void update(int hightimeL, int hightimeR) {
		currHightimeL = hightimeL;
		currHightimeR = hightimeR;
		pwmL.update(hightimeL);
		pwmR.update(hightimeR);
		
	}
	
	
	static {
	
		dcm = new DCMotor();
		
		
	}
	

}
