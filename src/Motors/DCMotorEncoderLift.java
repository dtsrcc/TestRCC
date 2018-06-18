package Motors;

import Sensors.LimitSwitchold;
import ch.ntb.inf.deep.runtime.ppc32.Task;
import ch.ntb.sysp.lib.SpeedController4DCMotor;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_FQD;

public class DCMotorEncoderLift extends Task
{
	private static final boolean TPU_A = true;
	
	/* Controller parameters */
	private static final float ts = 0.001f;
	private static final float kp = 1f;
	private static final float tn = 0.01f;

	/* Ticks per rotation from encoder datasheet */
	protected static final int ticksPerRotation = 1024;
	protected static final float gearRatio = 37f;
	protected static final float motorVoltage = 5f;
	
	private float speed = 0;
	
	public static SpeedController4DCMotor speedcon;
	
	public static LimitSwitchold switchLimitTop;
	public static LimitSwitchold switchLimitBottom;

	public static int state = 0;
	
	public TPU_FQD fqd;
	
	public static boolean switchAvailable = false;
	public static boolean zeroingWithSwitch = false;
	public static boolean switchIsEnabled = false;
	
	public boolean atTarget;
	
	public static long targetPos = 0;

	public static int  overflow = 0;
	public static short oldpos = 0;
	public static short posOfset = 0;
	public static long realpos;
	public static long actualpos = 0;

	
	public DCMotorEncoderLift()
	{
		speedcon = new SpeedController4DCMotor(ts, 4, 5, TPU_A, 10, TPU_A, ticksPerRotation, motorVoltage, gearRatio, kp, tn);
		switchLimitTop = new LimitSwitchold(11);
		switchLimitBottom = new LimitSwitchold(12);
		fqd = new TPU_FQD(true, 10);
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
			
	        if((switchLimitBottom.getSwitchInputs() == true) && (switchIsEnabled == false)){
	        	speed = 0;
	        	atTarget = true;
				switchIsEnabled = true;
				if(zeroingWithSwitch == true) {
					state = 1;
					zeroingWithSwitch = false;
				}	
			}
	        else if (switchLimitBottom.getSwitchInputs() == false) {
				 switchIsEnabled = false;
			}
	        
	        
	        if((switchLimitTop.getSwitchInputs() == true) && (switchIsEnabled == false)){
	        	speed = 0;
	        	atTarget = true;
				switchIsEnabled = true;
			}
	        else if (switchLimitTop.getSwitchInputs() == false) {
				 switchIsEnabled = false;
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
	        		speed = -20;
	        		atTarget = false;
	        	}else if ((targetPos-100)>realpos) {
	        		speed = 20;
	        		atTarget = false;
	        	}else {
	        		speed = 0;
	        		atTarget = true;
	        		state = 5;
	        	}
	            break;  
	        case 3:
	        	speed = 15; // Drehrichtung beachten
	        	atTarget = false;
	    		zeroingWithSwitch = true;
	    		state = 5;
	        	break;        	
	        }
			
	        if((switchLimitTop.getSwitchInputs() == true) && (speed <=0)) {
	        	speed = 0;
	        	atTarget = true;
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
			if(switchLimitTop.getSwitchInputs() == false) {
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
			
		
		public boolean motorAtTarget(){
			return atTarget;
		}
		
		//********************************************************************************************************************************************//
		static{
			Task t = new DCMotorEncoderLift();
			t.period = (int) (ts * 1000);
			Task.install(t);
		}
	}