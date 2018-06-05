package Motors;

import java.io.PrintStream;

import Sensors.LimitSwitchold;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_PWM;
import ch.ntb.inf.deep.runtime.ppc32.Task;
import ch.ntb.sysp.lib.SpeedController4DCMotor;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_FQD;

public class DCMotorEncoder extends Task
{
	private static final boolean TPU_A = true;
	private static final int TPU_PWM_CH0 = 12;
	private static final int TPU_PWM_CH1 = 13;
	private static final int TPU_FQD_A = 4;
	
	//public int i=0;

	/* Controller parameters */
	private static final float ts = 0.001f;
	private static final float kp = 1f;
	private static final float tn = 0.01f;

	/* Ticks per rotation from encoder datasheet */
	private static final int ticksPerRotation = 64;
	private static final float gearRatio = 3249f/196f;
	private static final float motorVoltage = 5f;
	
	public static SpeedController4DCMotor speedcon;
	
	LimitSwitchold switchLimit;

	public static int state = 0;
	
	private TPU_FQD fqd;
	
	public static boolean switchAvailable = false;
	public static boolean zeroingWithSwitch = false;
	
	public static long targetPos = 0;

	public static int overflow = 0;
	public static short oldpos = 0;
	public static short posOfset = 0;
	public static long realpos;
	public static long actualpos = 0;
	public int a = 0;
	public int b = 0;
	
	public DCMotorEncoder(int switchpin)
	{
		speedcon = new SpeedController4DCMotor(ts, TPU_PWM_CH0, TPU_PWM_CH1, TPU_A, TPU_FQD_A, TPU_A, ticksPerRotation, motorVoltage, gearRatio, kp, tn);
		switchLimit = new LimitSwitchold(switchpin);
		fqd = new TPU_FQD(true, 4);
		switchAvailable = true;
		speedcon.setDesiredSpeed(0);
		realpos = 0;
	}
	
	public DCMotorEncoder()
	{
		speedcon = new SpeedController4DCMotor(ts, TPU_PWM_CH0, TPU_PWM_CH1, TPU_A, TPU_FQD_A, TPU_A, ticksPerRotation, motorVoltage, gearRatio, kp, tn);
		fqd = new TPU_FQD(true, 4);
		speedcon.setDesiredSpeed(0);
		realpos = 0;
	}
	
	
	
	public void action(){
		
		short pos = fqd.getPosition();
		
		if(oldpos > 0 && pos < 0)
		{
			overflow++;
		}
		
		realpos = overflow * 32767 * 2  + pos - posOfset; //+ 32767
		
		oldpos = pos;
		
		if (switchAvailable) {
			if(switchLimit.getSwitchInputs() == true){
				speedcon.setDesiredSpeed(0);
				if(zeroingWithSwitch) {
					state=1;
				}
			}
		}
		
        switch (state) {
        
        case 1:
        	overflow = 0;
        	posOfset =  fqd.getPosition();
        	realpos = overflow * 32767 * 2 + fqd.getPosition() - posOfset; //+ 32767
        	System.out.println(realpos);
        	realpos = 0;
        	state = 5;
            break;
            
        case 2:  
        	if ((targetPos+100)<realpos) {
        		speedcon.setDesiredSpeed(-30);
        	}else if ((targetPos-100)>realpos) {
        		speedcon.setDesiredSpeed(30);
        	}else {
        		System.out.println("else");
        		speedcon.setDesiredSpeed(0);
        		state = 5;
        	}
            break;
            
        case 3:
        	System.out.println("SetZeroWithSwitch");
    		speedcon.setDesiredSpeed(-10); // Drehrichtung beachten
    		zeroingWithSwitch = true;
    		state = 5;
        	break;
            
        // default:
        	// break;
    }
		
		speedcon.run();
		
		
	}

	public boolean setZero() {
		state = 1;
		return true;
	}
	
	public boolean setZeroSwitch() {
		state = 3;
		return true;
	}
	
	public boolean setTargetPos(int target) {
		state = 2;
		targetPos = target;
		
		return true;
	}
	
	public int getActualPos() {
		int i = overflow * 32767 * 2  + fqd.getPosition() - posOfset; //+ 32767
		System.out.println(i);
		return i;
		
	}
	
	static
	{
		SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(19200, SCI.NO_PARITY, (short)8);
		System.out = new PrintStream(sci1.out);
		
		Task t = new DCMotorEncoder(12);	//Pin beachten
		t.period = (int) (ts * 1000);
		Task.install(t);
		
		
		
		
	}
	
	
	
	
	
	
	/*if(switch1.getSwitchInputs() == false)
	{
		//System.out.println(fqd.getPosition());
		speedcon.setDesiredSpeed(30);
	}
	
	if(fqd.getPosition() >= -30000 && fqd.getPosition() <= -25000)
	{
		//System.out.print("> 0       ");
		speedcon.setDesiredSpeed(0);
	}
	
	if(switch1.getSwitchInputs() == true)
	{
		speedcon.setDesiredSpeed(-30);
	}*/
	
    //System.out.println(realpos);
	//speedcon.setDesiredSpeed(0);
}
