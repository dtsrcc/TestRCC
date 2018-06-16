package Motors;

import Sensors.LimitSwitchold;
import Sensors.IRSensor;
import ch.ntb.inf.deep.runtime.ppc32.Task;
import ch.ntb.sysp.lib.SpeedController4DCMotor;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_FQD;

public class DCMotorEncoderDrive extends Task
{
	private static final boolean TPU_A = true;
	
	//public int i=0;

	/* Controller parameters */
	private static final float ts = 0.001f;
	private static final float kp = 1f;
	private static final float tn = 0.01f;

	/* Ticks per rotation from encoder datasheet */
	protected static final int ticksPerRotation = 1024;
	protected static final float gearRatio = 37f;
	protected static final float motorVoltage = 5f;
	
	private int speed = 0;
	
	public static SpeedController4DCMotor speedcon;
	
	public static LimitSwitchold switchLimit;
	
	public static IRSensor sensor;

	public static int state = 1;
	
	public TPU_FQD fqd;
	
	public static boolean switchAvailable = false;
	public static boolean zeroingWithSwitch = false;
	public static boolean switchIsEnabled = false;
	public static boolean sensorIsEnabled = false;
	
	public static long targetPos = 0;

	public static int overflow = 0;
	public static short oldpos = 0;
	public static short posOfset = 0;
	public static long realpos;
	public static long actualpos = 0;

	
	public DCMotorEncoderDrive()	// Switch 9; TPU 6, PWM 8 9
	{
		speedcon = new SpeedController4DCMotor(ts, 8, 9, TPU_A, 6, TPU_A, ticksPerRotation, motorVoltage, gearRatio, kp, tn);
		switchLimit = new LimitSwitchold(10);
		sensor = new IRSensor();
		fqd = new TPU_FQD(true, 6);
		switchAvailable = true;
		speed = 0;
		realpos = 0;
	}
	
	//********************************************************************************************************************************************//
	public void action(){
		
		short pos = fqd.getPosition();
		
		if(oldpos > 0 && pos < 0 && speed > 0){
			overflow++;
		}else if(oldpos < 0 && pos > 0 && speed < 0){
			overflow--;
		}
		
		realpos = overflow * 32767 * 2  + pos - posOfset;
		oldpos = pos;
		
		// FrontSwitch
        if((switchLimit.getSwitchInputs() == true) && (switchIsEnabled == false)){
        	speed = 0;
			switchIsEnabled = true;
			if(zeroingWithSwitch == true) {
				state = 1;
				zeroingWithSwitch = false;
			}	
		}
        else if (switchLimit.getSwitchInputs() == false) {
			 switchIsEnabled = false;
		}
        
        
         //IR Sensor
        if((sensor.getSensorInput() == true) && (sensorIsEnabled == false) && (switchIsEnabled == false)){
        	speed = -1;
        	sensorIsEnabled = true;	
		} else if (sensor.getSensorInput() == false) {
        	sensorIsEnabled = false;
			}
		
        switch (state) {
        
        case 1:
        	overflow = 0;
        	posOfset =  fqd.getPosition();
        	realpos = overflow * 32767 * 2 + fqd.getPosition() - posOfset;
        	state = 5;
            break;
            
        case 2:  
        	if ((targetPos+100)<realpos) {
        		speed = -2;
        	}else if ((targetPos-100)>realpos) {
        		speed = 2;
        	}else {
        		speed = 0;
        		state = 5;
        	}
            break;
            
        case 3:
        	speed = -3; // Drehrichtung beachten
    		zeroingWithSwitch = true;
    		state = 5;
        	break;        	
        }
		
        if((switchLimit.getSwitchInputs() == true) && (speed <=0)) {
        	speed = 0;
        }
        
        speedcon.setDesiredSpeed(speed);
		speedcon.run();
		
	}
	//********************************************************************************************************************************************//
	public boolean setZero() {
		state = 1;
		return true;
	}
	
	public boolean setZeroSwitch() {
		if(switchLimit.getSwitchInputs() == false) {
			state = 3;
		}else {
			state = 1;
		}

		return true;
	}
	
	public boolean setTargetPos(int target) {
		state = 2;
		targetPos = target;
		
		return true;
	}
	
	public int getActualPos() {
		int i = overflow * 32767 * 2  + fqd.getPosition() - posOfset;
		return i;
		
	}
	
	public void setSpeed(){
		speed = 0;
	}
	
	//********************************************************************************************************************************************//
	static{
		Task t = new DCMotorEncoderDrive();
		t.period = (int) (ts * 1000);
		Task.install(t);
	}
}
