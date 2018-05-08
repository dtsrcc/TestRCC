package Motors;
import Motors.ServoMotor;


public class MotorTest {

	
	public MotorTest()
	{
		
	}
	
	public static void forward()
	{
		DCMotor.driveForward(2);
	}
	
	public static void reverse()
	{
		DCMotor.driveReverse(5);
	}
	
	public static void stop()
	{
		DCMotor.stop();
	}
	
	public static void angle1()
	{
		ServoMotor.setPosition1();
	}
	
	public static void angle2()
	{
		ServoMotor.setPosition2();
	}

}
