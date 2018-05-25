package Motors;

import java.io.PrintStream;

import Sensors.LimitSwitchold;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_PWM;
import ch.ntb.inf.deep.runtime.ppc32.Task;
import ch.ntb.sysp.lib.SpeedController4DCMotor;

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
	
	private static SpeedController4DCMotor speedcon;
	
	LimitSwitchold switch1;
	
	
	public DCMotorEncoder()
	{
		speedcon = new SpeedController4DCMotor(ts, TPU_PWM_CH0, TPU_PWM_CH1, TPU_A, TPU_FQD_A, TPU_A, ticksPerRotation, motorVoltage, gearRatio, kp, tn);
		switch1 = new LimitSwitchold(7);
		
	}
	
	
	public void action()
	{
		
		/*if(switch1.getSwitchInputs() == false)
		{
			speedcon.setDesiredSpeed(30);
			
		}
		
		if(switch1.getSwitchInputs() == true)
		{
			speedcon.setDesiredSpeed(0);
		}*/
		
		if(speedcon.getActualPosition() < -1000)
		{
			
			//System.out.print("< 0       ");
			speedcon.setDesiredSpeed(-10);
			
		}
		
		if(speedcon.getActualPosition() > 1000)
		{
			
			//System.out.print("> 0       ");
			speedcon.setDesiredSpeed(10);
		}
		
		
		//speedcon.setDesiredSpeed(-10);
		//System.out.println(speedcon.getActualPosition());
		
		
		speedcon.run();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	static
	{
		Task t = new DCMotorEncoder();
		  t.period = (int) (ts * 1000);
		  Task.install(t);
		
		
		SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(38400, SCI.NO_PARITY, (short)8);
		System.out = new PrintStream(sci1.out);
	}
	
	
	
	
	
	

}
