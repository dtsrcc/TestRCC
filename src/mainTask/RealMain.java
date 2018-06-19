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


/*
 * Speed Optimization && drive Optimization*
 * slow servo angle*
 * stacking height
 * IR Sensor, not drive fwd*
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
	long setTimeStamp = 0;
	
	boolean useWifi=false;
	
	boolean switch1State;
	boolean switch2State;
	
	private RN131 wifi;	
	
	int timeCounter = 0;
	
	LimitSwitchold switchWhite;
	LimitSwitchold switchRed;
	
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
	public boolean truevariable = true;
	public int counter = 0;
	public int counterCase7 = 0;
	
	public RealMain() throws Exception{
		time = System.currentTimeMillis();
		
		switch1State = false;
		
		
		switch2State = false;
		
		switchWhite = new LimitSwitchold(9);
		switchRed = new LimitSwitchold(7);
		
		ServoGripper = new ServoMotor(3, 5, 6, 81);	// Pin, DefaultPos, MinPos, MaxPos
		ServoGuide = new ServoMotor(1,-10, -20, 66);	// Pin, DefaultPos, MinPos, MaxPos
		ServoAngle = new ServoMotor(0, 15, 7, 30);
		
		dcmLift = new DCMotorEncoderLift();  // Pins for testboard
		dcmDrive = new DCMotorEncoderDrive(); //switchpin, int fqdpin, int pwm1, int pwm2
		
		SCI sci = SCI.getInstance(SCI.pSCI2);
		sci.start(115200, SCI.NO_PARITY, (short)8);

		wifi = new RN131(sci.in , sci.out, new MPIOSM_DIO(5, true));
		
		
		
	}

	
	public void action(){
		
		
	
	if((switchRed.getSwitchInputs() == true) && (switch1State == false)) {
		useWifi = true;
		switch1State = true;
	}else if ((switchWhite.getSwitchInputs() == true) && (switch1State == false)) {
		useWifi = false;
		switch1State = true;
	}
		
		
		
	
	if(switch1State == true) {
					
		switch (state) {
			case 0:  //Init
				System.out.print("RealposLift = : ");
				System.out.println(dcmLift.getActualPos());
				if (init == false) {
					init = true;
					
					wifiState = 310;
					System.out.println("Case 0	//Init");
					
					ServoGuide.setPosition(-14);	//Working Pos
					System.out.println("// Open Cube Guide");
					
					dcmLift.setZeroSwitch();
					System.out.println("// Move Gripper down until Switch");
	
					ServoAngle.setPosition(8);	//Working Pos.
					System.out.println("// AngleServo to working Pos");
					
					ServoGripper.setPosition(30);	//Open
					System.out.println("// Open Gripper");
					
				}

				if ((!useWifi) && (switchRed.getSwitchInputs() == true)) {
					if ((switchWhite.getSwitchInputs() == true) && (switchIsEnabled == false)){
						state = 9;
						switchIsEnabled = true;
					} else if (switchWhite.getSwitchInputs() == false) {
						 switchIsEnabled = false;
					}
				}else if (useWifi) {
					if(useWifi) {
						wifi.cmd.writeCmd(400);
					}
					CmdInt.Type type = wifi.cmd.readCmd();
					if (type == CmdInt.Type.Cmd) {
						message = wifi.cmd.getInt();
						System.out.println(message);
						if (message == 411) {
							state = 9;	
						} else {
							System.out.println("error Case 1");
						}
		
					}
				}
				
			break;
			
			
			
			
			case 1:  //Set Gripper Height
				System.out.print("RealposLift = : ");
				System.out.println(dcmLift.getActualPos());
				
				if (useWifi) {
					timeCounter++;
				}
	
				if (counter == 1) {
					dcmLift.setTargetPos(0); 
					System.out.println("Case 1	//Set Gripper Height Towertop");
				} else if (counter == 2){
					dcmLift.setTargetPos(-1300000); // WERT NOCH ANPASSEN FÜR GENAUE HÖHE DER STACKING POSITION
					System.out.println("Case 1	//Set Gripper Height First Position with Beacon");
					// 18.5mm differenz von oberen zu unterem Stein 
				} else if (counter >= 3) {
					dcmLift.setTargetPos(-2700000); // WERT NOCH ANPASSEN FÜR GENAUE HÖHE DER STACKING POSITION
					System.out.println("Case 1	//Set Gripper Height Second Position");
				}
				
				state = 2;	
			break;
		
			
			
			
			case 2:		//******************************************************Case 2 check if area clear*************************************************************************
				if(useWifi) {
					wifiState = 314;	
				}
				
				System.out.println("Case 2	//Check if Area Clear");
				
				if (!useWifi) {
					if ((switchWhite.getSwitchInputs() == true) && (switchIsEnabled == false)){
						state = 3;
						switchIsEnabled = true;
					} else if (switchWhite.getSwitchInputs() == false) {
						 switchIsEnabled = false;
					}
				}else if (useWifi) {
					if(useWifi) {
						wifi.cmd.writeCmd(300);
					}
					CmdInt.Type type = wifi.cmd.readCmd();
					if (type == CmdInt.Type.Cmd) {
						message = wifi.cmd.getInt();
						System.out.println(message);
						if (message == 316) {
							state = 3;	
						} else {
							System.out.println("Waiting for clear area");
							wifi.cmd.writeCmd(wifiState);
							state = 2;
						}
		
					}
				}
			break;
			
			case 3:		//******************************************************Case 3 drive forward*************************************************************************
				ServoAngle.setPosition(8);	//Working Pos.
				System.out.println("// AngleServo to working Pos");
				
				if(case3 == false) {
					case3 = true;
					System.out.println("Case 3	// Drive forward");
					ServoGuide.setPosition(-14);	//Working Pos
					dcmDrive.setZeroSwitch();
					if(useWifi) {
						wifiState = 311;	
					}	
				}
				
				if (useWifi && dcmDrive.getSwitchDrive() == true) {
					state = 4;
				}
				
				if(useWifi) {
					wifi.cmd.writeCmd(wifiState);
				}
				
				if (!useWifi) {
					if ((switchWhite.getSwitchInputs() == true) && (switchIsEnabled == false)){
						state = 4;
						switchIsEnabled = true;
					}else if (switchWhite.getSwitchInputs() == false) {
						 switchIsEnabled = false;
					}
				}
			break;	
				
			
			
			
			case 4:		//******************************************************Case 4 grab or stack*************************************************************************
				System.out.print("RealposLift= : ");
				System.out.println(dcmLift.getActualPos());
				time = System.currentTimeMillis();
				System.out.println("time = : ");
				System.out.println(time);
				
				ServoAngle.setPosition(8);	//Working Pos.
				System.out.println("// AngleServo to working Pos");
				
				case3 = false;
				
				if (counter == 1) {
					ServoGripper.setPosition(6);	// Close Gripper 
					System.out.println("Case 4 // Close Gripper");
					if (truevariable) {
						setTimeStamp = time;
						truevariable = false;
					}
					
					if (time >= setTimeStamp + 100000) { // 100000 = 0.1s
					dcmLift.setTargetPos(-200000);
					System.out.println("Case 4 // Lift Tower");
					ServoGuide.setPosition(66);	//deploy	//Close Cube Guide
					state = 5;
					truevariable = true;
					}
					
				} else if (counter == 2 && dcmLift.getActualPos() < -1250000 ) {
					dcmLift.setTargetPos(-500000); // 
					System.out.println("Case 3 // Stacking with beacon");
					if (truevariable) {
						setTimeStamp = time;
						truevariable = false;
					}
					wifiState = 312;
					System.out.println("time = : ");
					System.out.println(time);
					System.out.println("SetTimeStamp = : ");
					System.out.println(setTimeStamp);
				
				} else if (counter >= 3 && dcmLift.getActualPos() < -2600000) {
					dcmLift.setTargetPos(-1900000); // 
					System.out.println("Case 3 // Stacking with package");
					if (truevariable) {
						setTimeStamp = time;
						truevariable = false;
					}
					
					}
		
				
				
				if ((dcmLift.getActualPos() > -505000) && (dcmLift.getActualPos() < - 495000) && (time >= setTimeStamp + 3000000)) {
					ServoGripper.setPosition(30);	// Open Gripper
					System.out.println("Case 3 // Open Gripper");
						
					dcmLift.setZeroSwitch();
					System.out.println("Case 3 // Lower grabbing position");
				}
							
				
				
				if (dcmLift.getActualPos() > -1905000 && dcmLift.getActualPos() < - 1895000 && time >= setTimeStamp + 3000000)  {
					ServoGripper.setPosition(30);	// Open Gripper
					System.out.println("Case 3 // Open Gripper");
					
					dcmLift.setTargetPos(-2700000); //
					
					state = 11;
					
//					dcmLift.setZeroSwitch();
//					System.out.println("Case 3 // Lower grabbing position");
				}
				
				if (dcmLift.getActualPos() < 1000 && dcmLift.getActualPos() > -1000)  { // && time >= timestamp + 1000000{
					ServoGripper.setPosition(6);	// Close Gripper
					System.out.println("Case 3 // Close Gripper");
					ServoGuide.setPosition(66);	//deploy	//Close Cube Guide
					
					dcmLift.setTargetPos(-400000);
					System.out.println("Case 3 // Lift package");
					state = 5;
					truevariable = true;
				}
				
				if (!useWifi) {
					if ((switchWhite.getSwitchInputs() == true) && (switchIsEnabled == false)){
						state = 5;
						switchIsEnabled = true;
					} else if (switchWhite.getSwitchInputs() == false) {
						 switchIsEnabled = false;
					}
				}
			break;
			
			
			
			
			
			case 5:		//******************************************************Case 5 drive backwards*************************************************************************
				
				if(useWifi) {
					wifiState = 315;	
				}
				alreadyTookBeacon = true;
				System.out.println("Case 5	//drive bwd");
				dcmDrive.setTargetPos(600000);
				wifiState = 313;
				
				if (dcmDrive.getActualPos() < 602000 && dcmDrive.getActualPos() > 598000) {
					state = 6;
				}
			break;
			
			
			
			
			case 6:  //******************************************************Case Decide Time*************************************************************************
				System.out.println("Case 6	//decide time");
				
				if(useWifi) {
					wifiState = 314;	
				}
				
				if (!useWifi) {
					if ((switchWhite.getSwitchInputs() == true) && (switchIsEnabled == false)){
						state = 9;
						switchIsEnabled = true;
					} else if (switchRed.getSwitchInputs() == true) {
						state = 7;
						switchIsEnabled = true;
					} else if (switchWhite.getSwitchInputs() == false) {
						switchIsEnabled = false;
					} else if (switchRed.getSwitchInputs() == false) {
						switchIsEnabled = false;
					}
				}
				
				if (useWifi) {
					if(timeCounter <= (100*4)) {// *4 depends on t.period
						state = 9;
					}else {
						state = 7;
					}
				}
			break;
			
			
			
			
			case 7:		//******************************************************Case 7 deploy*************************************************************************
				System.out.println("Case 6	//deploy");
				counterCase7++;
				
				if(useWifi) {
					wifiState = 316;	
					wifi.cmd.writeCmd(wifiState);
				}
				
				if (counterCase7 >= 28) {		
					state = 8;
				}else if (counterCase7 >= 24) {
					dcmDrive.setTargetPos(800000);
					ServoAngle.setPosition(8);	//work Pos//10
				}else if (counterCase7 >= 20) {
					ServoGripper.setPosition(45);//Open wide
				}else if (counterCase7 >= 16) {
					ServoGripper.setPosition(15);	//Open small
				}else if (counterCase7 >= 4) {
					ServoAngle.setPosition(7 + counterCase7);	//deploy pos//26
				}else{
					ServoGuide.setPosition(66);	//deploy	//Close Cube Guide
				}
				
			break;
			
			case 8:		//******************************************************Case 8 finish*************************************************************************  
				System.out.println("Case 7	//Finish");
				if(useWifi) {
					wifiState = 318;	
					wifi.cmd.writeCmd(wifiState);
				}

			break;
			
			case 9:		//******************************************************Case 9 counter*************************************************************************
				counter++;
				state = 1;
			break;
			
			case 11:
				System.out.println("Case 11 // Nachdrücken");
				System.out.print("RealposLift= : ");
				System.out.println(dcmLift.getActualPos());
				
				
				time = System.currentTimeMillis();
				
				if (dcmLift.getActualPos() < -2690000) {
					if (truevariable == true ) {
						setTimeStamp = time;
						truevariable = false;
					}
					ServoGripper.setPosition(6);	// Close Gripper
					System.out.println("Case 11 // Close Gripper");
					
					if (time >= setTimeStamp + 200000) {
						dcmLift.setTargetPos(-2300000);
						System.out.println("Case 11 // Nachdrücken");
					}

				}
				
				
				if (dcmLift.getActualPos() > -2305000 && dcmLift.getActualPos() < - 2295000 && time >= setTimeStamp + 1700000) {

					ServoGripper.setPosition(30);	// Open Gripper
					System.out.println("Case 11 // Open Gripper");
					
					dcmLift.setZeroSwitch();
					System.out.println("Case 3 // Lower grabbing position");
					
				}
				
				if (dcmLift.getActualPos() < 1000 && dcmLift.getActualPos() > -1000)  { // && time >= timestamp + 1000000{
					ServoGripper.setPosition(6);	// Close Gripper
					System.out.println("Case 3 // Close Gripper");
					ServoGuide.setPosition(66);	//deploy	//Close Cube Guide
					
					dcmLift.setTargetPos(-400000);
					System.out.println("Case 3 // Lift package");
					state = 5;
					truevariable = true;
				}
				
				
			break;
			
			}
		}
	}
	
	static{
		SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(19200, SCI.NO_PARITY, (short)8);
		System.out = new PrintStream(sci1.out);
		
		try{
			Task t = new RealMain();
			t.period = 250;
			Task.install(t);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
