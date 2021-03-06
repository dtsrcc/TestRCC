package Motors;

import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_PWM;
import ch.ntb.inf.deep.runtime.ppc32.Task;

public class ServoMotorTest extends Task {
	 private final int testChannel = 0;
	 private final boolean useTPUA = true;
	 private boolean position = true;
	 //private int value1 = 1200000;
	 //private int value2 = 2200000;
	
	 //pwmPeriod in TimeBase Unit (50'000 ns)
	 private final int pwmPeriod = 10000000 / TPU_PWM.tpuTimeBase;
	
	 public int highTime = 0;
	
	 private TPU_PWM pwm;
	
	 public ServoMotorTest(){
		pwm = new TPU_PWM(useTPUA, testChannel, pwmPeriod, highTime);
	 	period = 2000; // Periodenlaenge des Tasks in ms
	 	Task.install(this);
	 }
	 
	 /*public void action(){
		 // highTime = (highTime + pwmPeriod / 10) % pwmPeriod;
		 if (position) {
			 pwm.update(650000/ TPU_PWM.tpuTimeBase);
		 } else {
			 pwm.update(2550000/ TPU_PWM.tpuTimeBase);
		 }
		 position = !position;
	 }*/
	 
	 public void action(){
		 // highTime = (highTime + pwmPeriod / 10) % pwmPeriod;
		 if (position) {
			 pwm.update(1200000/ TPU_PWM.tpuTimeBase);
		 } else {
			 pwm.update(2200000/ TPU_PWM.tpuTimeBase);
		 }
		 position = !position;
	 }

	 static { // Task Initialisierung
			 new ServoMotorTest();
	 }

}
