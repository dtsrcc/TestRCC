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
	
	private static SpeedController4DCMotor speedcon;
	
	LimitSwitchold switch1;
	LimitSwitchold switch2;
	LimitSwitchold switch3;
	
	private TPU_FQD fqd;
	
	int overflow;
	short oldpos = 0;
	long realpos = 0;
	long actualpos = 0;
	int a = 0;
	int b = 0;
	
	public DCMotorEncoder()
	{
		speedcon = new SpeedController4DCMotor(ts, TPU_PWM_CH0, TPU_PWM_CH1, TPU_A, TPU_FQD_A, TPU_A, ticksPerRotation, motorVoltage, gearRatio, kp, tn);
		switch1 = new LimitSwitchold(7);
		switch2 = new LimitSwitchold(8);
		switch3 = new LimitSwitchold(9);
		fqd = new TPU_FQD(true, 4);
		
	}
	
	
	
	public void action()
	{
		
		short pos = fqd.getPosition();
		
		if(oldpos > 0 && pos < 0)
		{
			overflow++;
		}
		
		realpos = overflow * 32767 * 2 + 32767 + pos;
		
		oldpos = pos;
		
		if(switch1.getSwitchInputs() == false)
		{
			speedcon.setDesiredSpeed(10);
		}
		
		if(switch1.getSwitchInputs() == true)
		{
			speedcon.setDesiredSpeed(0);
			
			if(b == 0)
			{
				actualpos = realpos;
				b++;
			}

		}
				
		if(switch2.getSwitchInputs() == true)
		{
			speedcon.setDesiredSpeed(-10);
			
			if(a == 0)
			{
				realpos = 0;
				a++;
			}

			
			if(realpos >= actualpos)
			{
				speedcon.setDesiredSpeed(0);
				
			}
		}
		
		if(switch3.getSwitchInputs() == true)
		{
			a = 0;
			b = 0;
			actualpos = 0;
			realpos = 0;
		}
		
		//speedcon.setDesiredSpeed(10);
		
		//System.out.print("  ");


		speedcon.run();
		
		
	}

	static
	{
		Task t = new DCMotorEncoder();
		t.period = (int) (ts * 1000);
		Task.install(t);
		
		
		SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(9600, SCI.NO_PARITY, (short)8);
		System.out = new PrintStream(sci1.out);
		
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
