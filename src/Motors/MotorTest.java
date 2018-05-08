package Motors;
import Motors.ServoMotorGrab;


public class DCMotorTest {

	
	public DCMotorTest()
	{
		
	}
	
	public static void forward()
	{
		DCMotorDrive.driveForward(2);
	}
	
	public static void reverse()
	{
		DCMotorDrive.driveReverse(5);
	}
	
	public static void stop()
	{
		DCMotorDrive.stop();
	}
	
	public static void angle1()
	{
		ServoMotorGrab.setPosition1();
	}
	
	public static void angle2()
	{
		ServoMotorGrab.setPosition2();
	}

}
