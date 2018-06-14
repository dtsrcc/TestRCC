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

package mainTask;

import java.io.PrintStream;

//import Motors.DCMotorEncoder;
import Motors.ServoMotor;
import Sensors.LimitSwitchold;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_FQD;
import ch.ntb.inf.deep.runtime.ppc32.*;

public class Main extends Task
{
	long time;
	
	boolean switch1State;
	boolean switch2State;
	
	
	LimitSwitchold switchWhite;
	LimitSwitchold switchRed;
	LimitSwitchold switchLimit1;
	LimitSwitchold switchLimit2;
	LimitSwitchold switchLimit3;
	LimitSwitchold switchLimit4;
	
	ServoMotor ServoGripper;
	ServoMotor ServoGuide;
	ServoMotor ServoAngle;
	
	TPU_FQD fqd;
	
	//DCMotorEncoder dcm;
	
	long pos=0;
	
	
	
	public Main() throws Exception{
		time = System.currentTimeMillis();
		
		switch1State = false;
		switch2State = false;
		
		switchWhite = new LimitSwitchold(7);
		switchRed = new LimitSwitchold(9);
		switchLimit1 = new LimitSwitchold(11);
		switchLimit2 = new LimitSwitchold(12);
		switchLimit3 = new LimitSwitchold(10);
		switchLimit4 = new LimitSwitchold(8);
		
		ServoGripper = new ServoMotor(3, 5, 8, 81);	// Pin, DefaultPos, MinPos, MaxPos
		ServoGuide = new ServoMotor(1,-10, -7, 68);	// Pin, DefaultPos, MinPos, MaxPos
		ServoAngle = new ServoMotor(0, 15, 10, 30);
		
		//dcm = new DCMotorEncoder();
		fqd = new TPU_FQD(true, 4);

		
		
	}

	
	public void action(){
		
		
		/*if (switchWhite.getSwitchInputs() == false) {
			System.out.println("Switch1 false  ");
			ServoGripper.setPosition(81);	//Open
			ServoAngle.setPosition(13);	//Working Pos
		}
		if (switchWhite.getSwitchInputs() == true) {
			System.out.println("Switch1 true  ");
			ServoGripper.setPosition(8);	//Close
			ServoAngle.setPosition(30);	//Deploy Pos
		}
		
		
		if (switchRed.getSwitchInputs() == false) {
			System.out.println("Switch1 false  ");
			ServoGuide.setPosition(68);	// Deploy pos
		}
		if (switchRed.getSwitchInputs() == true) {
			System.out.println("Switch1 true  ");
			ServoGuide.setPosition(-7);	//Working Pos
			
		}*/
		
		
		if(switchLimit1.getSwitchInputs() == false){
			
		}
		if(switchLimit1.getSwitchInputs() == true){
		//	dcm.setZeroSwitch();
		}
		
		
		/*if(switchLimit2.getSwitchInputs() == false){

		}
		if(switchLimit2.getSwitchInputs() == true){
			//System.out.println(dcm.getActualPos());
			dcm.getActualPos();
		}*/
		
		
		//if(switchLimit3.getSwitchInputs() == false){
			//dcm.setTargetPos(32767*5);
		//}
		if(switchLimit3.getSwitchInputs() == true){
		//	dcm.setTargetPos(32767*2);
		}
		
		
		

	}
	
	static
	{
		/*SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(19200, SCI.NO_PARITY, (short)8);
		System.out = new PrintStream(sci1.out);*/
		
		try{
			Task t = new Main();
			t.period = 10;
			Task.install(t);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
}
