package Motors;

import java.io.PrintStream;

import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_PWM;

public class DCMotorTest {
	private static DCMotorTest lm;  // Diese Klasse hat eine Instanz von sich selber

	private final boolean useTPUA =false;
	private final int chnL = 12, chnR = 13;
	private final static int pwmPeriod = 50000 / TPU_PWM.tpuTimeBase;
	
	public SpeedController speedcon;

	private int currHightimeL, currHightimeR;
	private TPU_PWM pwmL, pwmR;
	
	
	DCMotorTest() {
	// PWM-Kanaele initialisieren
		currHightimeL = 0;
		currHightimeR = 0;
		pwmL = new TPU_PWM(useTPUA, chnL, pwmPeriod, currHightimeL);
		pwmR = new TPU_PWM(useTPUA, chnR, pwmPeriod, currHightimeR);
		
		speedcon = new SpeedController();
	}
	
	
	public static void leftFull() {
		lm.update(pwmPeriod, 0);
	}
	
	public static void leftHalf() {
		lm.update(pwmPeriod/2, 0);
	}
	
	public static void rightFull() {
		lm.update(0, pwmPeriod);
	}
	
	public static void rightHalf() {
		lm.update(0, pwmPeriod/2);
	}
	
	public static void stop() {
		lm.update(0, 0);
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
		lm = new DCMotorTest();
		
		// Kopfzeile Ausgeben
		System.out.println("Hightime Left \t/\t Hightime Right \t/\t Period");
	}

}
