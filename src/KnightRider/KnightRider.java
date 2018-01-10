package KnightRider;

import ch.ntb.inf.deep.runtime.mpc555.driver.MPIOSM_DIO;
//import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_PWM;
import ch.ntb.inf.deep.runtime.ppc32.Task;

public class KnightRider extends Task{
	
	MPIOSM_DIO switch1 = new MPIOSM_DIO(5, false); // false = Input
	MPIOSM_DIO switch2 = new MPIOSM_DIO(6, false); // false = Input
	MPIOSM_DIO switch3 = new MPIOSM_DIO(7, false); // false = Input
	MPIOSM_DIO switch4 = new MPIOSM_DIO(8, false); // false = Input

	int counter = 0; // Zähler für momentane LED Position
	boolean direction = false; // Richtung, True = links, false = rechts
	
	private final int pwmPeriod = 10000000 / TPU_PWM.tpuTimeBase; // pwmPeriode für Servo
	public int highTime = 0;
	
	private int boost = 4; // Time for 1 Periodic Task
	private int skip = 0; // Boost gets subtracted with this time for 1 switch
	
	private TPU_PWM pwm1;
	private TPU_PWM pwm2;
	private TPU_PWM pwm3;
	private TPU_PWM pwm4;
	private TPU_PWM pwm5;
	private TPU_PWM pwm6;
	private TPU_PWM pwm7;

	
	public KnightRider() {
		// Initialize pwms
		pwm1 = new TPU_PWM(false, 1, pwmPeriod, highTime);
		pwm2 = new TPU_PWM(false, 2, pwmPeriod, highTime);
		pwm3 = new TPU_PWM(false, 3, pwmPeriod, highTime);
		pwm4 = new TPU_PWM(false, 4, pwmPeriod, highTime);
		pwm5 = new TPU_PWM(false, 5, pwmPeriod, highTime);
		pwm6 = new TPU_PWM(false, 6, pwmPeriod, highTime);
		pwm7 = new TPU_PWM(false, 7, pwmPeriod, highTime);
		pwm8 = new TPU_PWM(false, 8, pwmPeriod, highTime);
		//pwm9 = new TPU_PWM(false, 9, pwmPeriod, highTime);
	}

	public void action() {
		
		boost = 0;
		
		if (switch1.get()) {
			boost = boost+1;
		}
		if (switch2.get()) {
			boost = boost+1;
		}
		if (switch3.get()) {
			boost = boost+1;
		}
		if (switch4.get()) {
			boost = boost+1;
		}
		
		if (skip < boost) {
			skip++;
		} else {
			skip=0;
			switch (counter) {
				case 0:
					pwm1.update(0000000/ TPU_PWM.tpuTimeBase);
					break;
				case 1:
					pwm1.update(1000000/ TPU_PWM.tpuTimeBase);
					pwm2.update(00000000/ TPU_PWM.tpuTimeBase);
					break;
				case 2:
					pwm1.update(10000000/ TPU_PWM.tpuTimeBase);
					pwm2.update(1000000/ TPU_PWM.tpuTimeBase);
					pwm3.update(00000000/ TPU_PWM.tpuTimeBase);
					break;
				case 3:
					pwm1.update(1000000/ TPU_PWM.tpuTimeBase);
					pwm2.update(100000000/ TPU_PWM.tpuTimeBase);
					pwm3.update(1000000/ TPU_PWM.tpuTimeBase);
					pwm4.update(00000000/ TPU_PWM.tpuTimeBase);
					break;
				case 4:
					pwm1.update(00000000/ TPU_PWM.tpuTimeBase);
					pwm2.update(1000000/ TPU_PWM.tpuTimeBase);
					pwm3.update(100000000/ TPU_PWM.tpuTimeBase);
					pwm4.update(1000000/ TPU_PWM.tpuTimeBase);
					pwm5.update(00000000/ TPU_PWM.tpuTimeBase);
					break;
				case 5:
					pwm2.update(00000000/ TPU_PWM.tpuTimeBase);
					pwm3.update(1000000/ TPU_PWM.tpuTimeBase);
					pwm4.update(100000000/ TPU_PWM.tpuTimeBase);
					pwm5.update(1000000/ TPU_PWM.tpuTimeBase);
					pwm6.update(00000000/ TPU_PWM.tpuTimeBase);
					break;
				case 6:
					pwm3.update(00000000/ TPU_PWM.tpuTimeBase);
					pwm4.update(1000000/ TPU_PWM.tpuTimeBase);
					pwm5.update(100000000/ TPU_PWM.tpuTimeBase);
					pwm6.update(1000000/ TPU_PWM.tpuTimeBase);
					pwm7.update(00000000/ TPU_PWM.tpuTimeBase);
					break;
				case 7:
					pwm4.update(00000000/ TPU_PWM.tpuTimeBase);
					pwm5.update(1000000/ TPU_PWM.tpuTimeBase);
					pwm6.update(100000000/ TPU_PWM.tpuTimeBase);
					pwm7.update(1000000/ TPU_PWM.tpuTimeBase);
					pwm8.update(00000000/ TPU_PWM.tpuTimeBase);
					break;
				case 8:
					pwm5.update(00000000/ TPU_PWM.tpuTimeBase);
					pwm6.update(1000000/ TPU_PWM.tpuTimeBase);
					pwm7.update(100000000/ TPU_PWM.tpuTimeBase);
					pwm8.update(1000000/ TPU_PWM.tpuTimeBase);
					break;
				case 9:
					pwm6.update(00000000/ TPU_PWM.tpuTimeBase);
					pwm7.update(1000000/ TPU_PWM.tpuTimeBase);
					pwm8.update(100000000/ TPU_PWM.tpuTimeBase);
					break;
				case 10:
					pwm7.update(00000000/ TPU_PWM.tpuTimeBase);
					pwm8.update(1000000/ TPU_PWM.tpuTimeBase);
					break;
				case 11:
					pwm8.update(00000000/ TPU_PWM.tpuTimeBase);
					break;
			}
		
			if (direction) {
				counter--;
			} else {
				counter++;
			}
			
			if (counter >=11) {
				direction = true;
			} else if (counter <=0) {
				direction = false;
			}
			
		}
	}
 	

	 
	 static { // Task Initialisierung
		Task pwm = new KnightRider();
		pwm.period = 50; // Periodenlaenge des Tasks in ms
		Task.install(pwm);
	 }
	 }