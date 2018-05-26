/*
 * 		Servo TPU-A 3
 * 		Servo TPU-A 1
 * 		Servo TPU-A 0
 * 		Servo TPU-A 2
 * 
 * 		Taster	MPIOB 7
 * 		Taster	MPIOB 9
 * 		Taster	MPIOB 11
 * 		Taster	MPIOB 12
 * 		Taster	MPIOB 10
 * 		Taster	MPIOB 8
 * 
 * 		Motor1 	TPU-A	4
 * 		Motor1 	TPU-A	5
 * 		Encoder1 TPU-A	10
 * 		Encoder1 TPU-A	11
 * 
 * 		Motor2 	TPU-A	8
 * 		Motor2 	TPU-A	9
 * 		Encoder2 TPU-A	6
 * 		Encoder2 TPU-A	7
 */

package Motors;

import java.io.PrintStream;

import Sensors.LimitSwitchold;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_FQD;
import ch.ntb.inf.deep.runtime.ppc32.*;

public class Main extends Task
{
	long time;
	
	boolean switch1State;
	boolean switch2State;
	
	
	LimitSwitchold switch1;
	LimitSwitchold switch2;
	LimitSwitchold switch3;
	LimitSwitchold switch4;
	LimitSwitchold switch5;
	LimitSwitchold switch6;
	
	ServoMotor Servo1;
	ServoMotor Servo2;
	
	TPU_FQD fqd;
	
	DCMotor dc1;
	
	long pos=0;
	
	public Main() throws Exception{
		time = System.currentTimeMillis();
		
		switch1State = false;
		switch2State = false;
		
		switch1 = new LimitSwitchold(7);
		switch2 = new LimitSwitchold(8);
		switch3 = new LimitSwitchold(9);
		switch4 = new LimitSwitchold(10);
		switch5 = new LimitSwitchold(11);
		switch6 = new LimitSwitchold(12);
		
		Servo1 = new ServoMotor(3, 45, 5, 85);	// Pin, DefaultPos, MinPos, MaxPos
		Servo2 = new ServoMotor(0, 15, 1, 30);	// Pin, DefaultPos, MinPos, MaxPos
		
		dc1 = new DCMotor();
		fqd = new TPU_FQD(true, 4);

		
	}

	
	public void action(){
		
		
		if (switch1.getSwitchInputs() == false) {
			System.out.println("Switch1 false  ");
			Servo1.setPosition(8);
			//dc1.driveForward(1);
		}
		if (switch1.getSwitchInputs() == true) {
			System.out.println("Switch1 true  ");
			Servo1.setPosition(25);
			//dc1.stop();
		}

	
	
	
	
	
	}
	
	static
	{
		SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(19200, SCI.NO_PARITY, (short)8);
		System.out = new PrintStream(sci1.out);
		
		try{
			Task t = new Main();
			t.period = 500;
			Task.install(t);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
}
