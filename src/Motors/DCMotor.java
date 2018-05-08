package Motors;

import java.io.PrintStream;

import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_PWM;


public class DCMotor {
	private static DCMotor dcm;  // Diese Klasse hat eine Instanz von sich selber

	private final boolean useTPUA =false;
	private final int chnL = 8, chnR = 9;
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
	
	public static void stop()
	{
		dcm.update(0, 0);
	}
	
	public static void driveForward(int i)
	{
		dcm.update(pwmPeriod/i, 0);
	}
	
	public static void driveReverse(int i)
	{
		dcm.update(0, pwmPeriod/3);
	}
	
	
	private void update(int hightimeL, int hightimeR) {
		currHightimeL = hightimeL;
		currHightimeR = hightimeR;
		pwmL.update(hightimeL);
		pwmR.update(hightimeR);
		
		System.out.print(hightimeL); System.out.print("\t/\t");
		System.out.print(hightimeR); System.out.print("\t/\t");
		System.out.print(pwmPeriod); System.out.println();
	}
	
	
	static {
		// 1) Initialize SCI1 (9600 8N1)
		SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(9600, SCI.NO_PARITY, (short)8);
		
		// 2) Use SCI1 for stdout
		System.out = new PrintStream(sci1.out);
		
		// Objekt erzeugen
		dcm = new DCMotor();
		
		// Kopfzeile Ausgeben
		System.out.println("Hightime Left \t/\t Hightime Right \t/\t Period");
	}
	

}
