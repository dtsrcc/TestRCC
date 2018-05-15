package Motors;

import java.io.PrintStream;

import Sensors.LimitSwitch;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_FQD;
import ch.ntb.inf.deep.runtime.ppc32.*;

public class Main extends Task
{
	long time;
	
	boolean switch1State;
	boolean switch2State;
	
	LimitSwitch switch1;
	LimitSwitch switch2;
	LimitSwitch switch3;
	//LimitSwitch switch4;
	//LimitSwitch switch5;
	
	ServoMotor Servo1;
	ServoMotor Servo2;
	
	TPU_FQD fqd;
	
	DCMotor dc1;
	
	long pos=0;
	
	public Main() {
		time = System.currentTimeMillis();
		
		switch1State = false;
		switch2State = false;
		
		switch1 = new LimitSwitch(5);
		switch2 = new LimitSwitch(6);
		switch3 = new LimitSwitch(7);
		//switch4 = new LimitSwitch(8);
		//switch5 = new LimitSwitch(9);
		
		Servo1 = new ServoMotor(11, 45);
		Servo2 = new ServoMotor(9, 45);
		Servo1.speedValue = 50000;
		Servo2.speedValue = 500;
		
		dc1 = new DCMotor();
		fqd = new TPU_FQD(true, 4);
		
	}

	
	public void action(){
		//System.out.println("start");

		if(switch1.getSwitchInputs() == false)
		{
			dc1.driveForward(4);
			pos = fqd.getPosition();
			System.out.println(pos);
			
		}
		if(switch1.getSwitchInputs() == true)
		{
			dc1.stop();
			pos = fqd.getPosition();
			System.out.println(pos);

		}
		
		if(switch2.getSwitchInputs() == false)
		{
			Servo2.setPositionSlow(90);
			//switch1State = switch1.getSwitchInputs();
			//System.out.print("Switch 1 state is now: ");
			//System.out.println("false");
		}
		if(switch2.getSwitchInputs() == true)
		{
			Servo2.setPositionSlow(0);
			//switch1State = switch1.getSwitchInputs();
			//System.out.print("Switch 1 state is now: ");
			//System.out.println("true");
		}
		
		if (switch3.getSwitchInputs() == true) {
			//System.out.println("start true");
			Servo1.speedValue = 50000;
			//Servo2.speedValue = 50000;
			
			
		} else {
			//System.out.println("start false");
			Servo1.speedValue = 500;
			//Servo2.speedValue = 500;
		}
		
		

	}
		
	
	static
	{
		SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(38400, SCI.NO_PARITY, (short)8);
		System.out = new PrintStream(sci1.out);
		
		Task t = new Main();
		t.period = 10;
		Task.install(t);

	}
	
}
