package Motors;

public class DCMotorTest {
	
	
	public DCMotorTest()
	{
		
	}
	
	public static void forward()
	{
		DCMotorDrive.driveForward(5);
	}
	
	public static void reverse()
	{
		DCMotorDrive.driveReverse(5);
	}
	
	public static void stop()
	{
		DCMotorDrive.stop();
	}

}
