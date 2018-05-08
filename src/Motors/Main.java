package Motors;

import ch.ntb.inf.deep.runtime.ppc32.*;

public class Main extends Task
{
	ServoMotor Servo1 = new ServoMotor(11, 45);
	ServoMotor Servo2 = new ServoMotor(9, 45);
	long time = System.currentTimeMillis();
	
	
	public void action()
	{
		Servo1.setPositionSlow(90);
		Servo2.setPositionSlow(90);
		
		
	}
	
	static
	{
		Task t = new Main();
		t.period = 1;
		Task.install(t);
	}
	
}
