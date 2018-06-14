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

import Motors.DCMotorEncoderDrive;
import Motors.DCMotorEncoderLift;
import Motors.ServoMotor;
import Sensors.LimitSwitchold;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.ppc32.*;
import ch.ntb.inf.deep.runtime.util.CmdInt;
import ch.ntb.inf.deep.runtime.mpc555.driver.MPIOSM_DIO;
import ch.ntb.inf.deep.runtime.mpc555.driver.RN131;

public class RealMain extends Task
{
	long time;
	
	boolean switch1State;
	boolean switch2State;
	
	private RN131 wifi;	
	
	LimitSwitchold switchWhite;
	LimitSwitchold switchRed;
	/*LimitSwitchold switchLimit1;
	LimitSwitchold switchLimit2;
	LimitSwitchold switchLimit3;
	LimitSwitchold switchLimit4;*/
	
	public static boolean switchIsEnabled = false;
	
	ServoMotor ServoGripper;
	ServoMotor ServoGuide;
	ServoMotor ServoAngle;
	
	DCMotorEncoderDrive dcmDrive;
	DCMotorEncoderLift dcmLift;
	
	int state = 0;
	int wifiState = 0;
	int message = 0;
	
	long pos=0;
	
	public boolean init = false;
	public boolean case3 = false;
	public boolean alreadyTookBeacon = false;
	
	public RealMain() throws Exception{
		time = System.currentTimeMillis();
		
		switch1State = false;
		switch2State = false;
		
		switchWhite = new LimitSwitchold(9);
		switchRed = new LimitSwitchold(7);
		//switchLimit1 = new LimitSwitchold(8);
		// switchLimit2 = new LimitSwitchold(10);
		//switchLimit3 = new LimitSwitchold(11);
		//switchLimit4 = new LimitSwitchold(12);
		
		ServoGripper = new ServoMotor(3, 5, 8, 81);	// Pin, DefaultPos, MinPos, MaxPos
		ServoGuide = new ServoMotor(1,-10, -20, 66);	// Pin, DefaultPos, MinPos, MaxPos
		ServoAngle = new ServoMotor(0, 15, 10, 30);
		
		dcmLift = new DCMotorEncoderLift();  // Pins for testboard
		dcmDrive = new DCMotorEncoderDrive(); //switchpin, int fqdpin, int pwm1, int pwm2
		
		SCI sci = SCI.getInstance(SCI.pSCI2);
		sci.start(115200, SCI.NO_PARITY, (short)8);

		wifi = new RN131(sci.in , sci.out, new MPIOSM_DIO(5, true));
		
		
		
	}

	
	public void action(){
			
	switch (state) {
		case 0:  //Init
			System.out.print("Realpos = : ");
			System.out.println(dcmLift.getActualPos());
			if (init == false) {
				init = true;
				
				wifiState = 310;
				System.out.println("Case 0	//Init");
				
				ServoGuide.setPosition(-11);	//Working Pos
				System.out.println("// Open Cube Guide");
				
				//dcmLift.setZeroSwitch();
				System.out.println("// Move Gripper down until Switch");

				ServoAngle.setPosition(11);	//Working Pos.
				System.out.println("// AngleServo to working Pos");
				
				ServoGripper.setPosition(30);	//Open
				System.out.println("// Open Gripper");
			}
			if (true) {
				wifi.cmd.writeCmd(400);
			}
			
			if ((switchWhite.getSwitchInputs() == true) && (switchIsEnabled == false)){
				state = 1;
				switchIsEnabled = true;
			} else if (switchWhite.getSwitchInputs() == false) {
				 switchIsEnabled = false;
				}
			
			CmdInt.Type type = wifi.cmd.readCmd();
			if (type == CmdInt.Type.Cmd) {
				message = wifi.cmd.getInt();
				System.out.println(message);
				if (message == 411) {
					state = 1;	
				} else {
					System.out.println("error Case 1");
				}

			}

			// If Connection Yes && Tower Yes send Start (Wifi 400)
			// if response 411
			//state = 1;	
			// else Wait (Ask for Start)
			//wifiState = 314;
			
		break;
		
		case 1:  //Set Gripper Height
			System.out.print("Realpos = : ");
			System.out.println(dcmLift.getActualPos());

			if (wifiState == 310) {
				dcmLift.setTargetPos(-50000); // WERT NOCH ANPASSEN FÜR GENAUE HÖHE DES TURMS
				System.out.println("Case 1	//Set Gripper Height Towertop");
			} else {
				dcmLift.setTargetPos(-50000); // WERT NOCH ANPASSEN FÜR GENAUE HÖHE DER STACKING POSITION
				System.out.println("Case 1	//Set Gripper Height Stacking Position");
				}
			
			dcmLift.setTargetPos(-50000); // WERT NOCH ANPASSEN FÜR GENAUE HÖHE DER STACKING POSITION
			System.out.println("Case 1	//Set Gripper Height Stacking Position");
			
			wifiState = 314;
			
			state = 2;
			
			// DIMITROV WAS SOLLEN DIESE ZWEI KOMMENTARE
			//if 1 executuion height Towertop
			//else execution height over Stack
				
			
			// If wifiState == 310 (Init)
			// move Gripper to TowerPosition
			// else move Gripper to Stacking Position
			// wifiState = 314
			// state 2;
				
			
		break;
	
		case 2:  //Check if Area Clear
			
			System.out.println("Case 2	//Check if Area Clear");
			System.out.print("RealposLift = : ");
			System.out.println(dcmLift.getActualPos());
			
			wifi.cmd.writeCmd(300);
			CmdInt.Type type1 = wifi.cmd.readCmd();
			if (type1 == CmdInt.Type.Cmd) {
				message = wifi.cmd.getInt();
				System.out.println(message);
				if (message == 316) {
					state = 3;	
				} else {
					System.out.println("Waiting for clear area");
					wifi.cmd.writeCmd(314);
					state = 2;
					
				}
			}

			
			if ((switchWhite.getSwitchInputs() == true) && (switchIsEnabled == false)){
				state = 3;
				switchIsEnabled = true;
			} else if (switchWhite.getSwitchInputs() == false) {
				 switchIsEnabled = false;
				}
			
			// If Area clear (send Wifi 300)
			// if response 316
			// state = 3;
			// else Wait (for clear Area)
			// wifiState = 314; (stays 314)
			
		break;
		
		case 3:		//drive forward and Grab or stack
			
			System.out.print("RealposDrive = : ");
			System.out.println(dcmDrive.getActualPos());

			if(case3 == false) {
				case3 = true;
				
				System.out.println("Case 3	// Drive forward and Grab or Stack");
				
				dcmDrive.setZeroSwitch();
				wifiState = 311;	
			}
			
			wifi.cmd.writeCmd(wifiState);
			
			if (alreadyTookBeacon == false && dcmDrive.getActualPos() < 100) {
				alreadyTookBeacon = true;
				
				ServoGripper.setPosition(8);	// Close Gripper // NOT SURE
				System.out.println("Close Gripper");
				
			} else if (dcmDrive.getActualPos() < 100) {
				dcmLift.setTargetPos(5000); // 
				System.out.println("Case 3 // Stacking");
				
				wifiState = 312;
				}
			
			
			
			
			
			
			if ((switchWhite.getSwitchInputs() == true) && (switchIsEnabled == false)){
				state = 4;
				switchIsEnabled = true;
			} else if (switchWhite.getSwitchInputs() == false) {
				 switchIsEnabled = false;
				}
			
			
			
			//if 1 execution Grab Tovertop, lift
			//state = 4;	
			//else Stack, Open, down, Close, lift
			//state = 4;
			
			//Set WifiState 311
			
			
			
			//Set WifiState 311
			
			
			
			
			
			
			// If wifiState == 310 (Init)
			// move Gripper to TowerPosition
			// else move Gripper to Stacking Position
			// wifiState = 314
			// state 2;
			
		break;
		
		case 4:  //drive bwd
			System.out.println("Case 4	//drive bwd");
			wifiState = 313;
			//drive bwd time or distance
			state = 5;
		break;
		
		case 5:  //decide time
			System.out.println("Case 5	//decide time");
			//if enough time set WifiState 314
			wifiState = 314;
			state = 1;
			//else
			state = 6;
		break;
		
		case 6:  //deploy
			System.out.println("Case 6	//deploy");
			wifiState = 316;
			//Lower Gripper
			//Close Cube Guide
			//Angle to deploy pos
			//Open Gripper full
			//Angle Gripper to working Pos
			wifiState = 317;
			//drive bwd distance
			state = 7;
		break;
		
		case 7:		//Finish  
			System.out.println("Case 7	//Finish");
			wifiState = 318;
		break;
		
		}
	
	}
	
	static
	{
		SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(19200, SCI.NO_PARITY, (short)8);
		System.out = new PrintStream(sci1.out);
		
		try{
			Task t = new RealMain();
			t.period = 300;
			Task.install(t);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
}
