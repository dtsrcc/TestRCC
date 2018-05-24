/*
 * 		Servo TPU-A 3
 * 		Servo TPU-A 1
 * 		Servo TPU-A 0
 * 		Servo TPU-A 2
 * 
 * 		Taster	7
 * 		Taster	9
 * 		Taster	11
 * 		Taster	12
 * 		Taster	10
 * 		Taster	8
 * 
 */





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
	LimitSwitch switch4;
	LimitSwitch switch5;
	LimitSwitch switch6;
	
	ServoMotor Servo1;
	ServoMotor Servo2;
	
	TPU_FQD fqd;
	
	DCMotor dc1;
	
	long pos=0;
	
	public Main() {
		time = System.currentTimeMillis();
		
		switch1State = false;
		switch2State = false;
		
		switch1 = new LimitSwitch(7);
		switch2 = new LimitSwitch(8);
		switch3 = new LimitSwitch(9);
		switch4 = new LimitSwitch(10);
		switch5 = new LimitSwitch(11);
		switch6 = new LimitSwitch(12);
		
		Servo1 = new ServoMotor(11, 45, 5, 80);	// Pin, DefaultPos, MinPos, MaxPos
		Servo2 = new ServoMotor(2, 15, 1, 30);	// Pin, DefaultPos, MinPos, MaxPos
		//Servo1.speedValue = 50000;
		//Servo2.speedValue = 500;
		
		dc1 = new DCMotor();
		fqd = new TPU_FQD(true, 4);
		
	}

	
	public void action(){
		
		
		if (switch1.getSwitchInputs() == false) {
			System.out.print("Switch1 false  ");
		}
		if (switch1.getSwitchInputs() == true) {
			System.out.print("Switch1 true  ");
		}
		
		if (switch2.getSwitchInputs() == false) {
			System.out.print("Switch2 false  ");
		}
		if (switch2.getSwitchInputs() == true) {
			System.out.print("Switch2 true  ");
		}
		
		if (switch3.getSwitchInputs() == false) {
			System.out.print("Switch3 false  ");
		}
		if (switch3.getSwitchInputs() == true) {
			System.out.print("Switch3 true  ");
		}
		
		if (switch4.getSwitchInputs() == false) {
			System.out.print("Switch4 false  ");
		}
		if (switch4.getSwitchInputs() == true) {
			System.out.print("Switch4 true  ");
		}
		
		if (switch5.getSwitchInputs() == false) {
			System.out.print("Switch5 false  ");
		}
		if (switch5.getSwitchInputs() == true) {
			System.out.print("Switch5 true  ");
		}
		
		if (switch6.getSwitchInputs() == false) {
			System.out.println("Switch6 false");
		}
		if (switch6.getSwitchInputs() == true) {
			System.out.println("Switch6 true");
		}		

	}
		
	
	static
	{
		SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(19200, SCI.NO_PARITY, (short)8);
		System.out = new PrintStream(sci1.out);
		
		Task t = new Main();
		t.period = 100;
		Task.install(t);

	}
	
}
