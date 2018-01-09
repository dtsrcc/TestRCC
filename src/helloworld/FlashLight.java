package helloworld;

import ch.ntb.inf.deep.runtime.mpc555.driver.MPIOSM_DIO;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.ppc32.Task;

public class FlashLight extends Task{
	boolean INPUT = false;
	//public final byte switch1 = 13;
	MPIOSM_DIO switch1 = new MPIOSM_DIO(13, INPUT);
	int counter = 5;
	boolean inv = false;
	boolean dir = false;
	boolean lastinv = false;
	
	MPIOSM_DIO led1;
	MPIOSM_DIO led2;
	MPIOSM_DIO led3;
	MPIOSM_DIO led4;
	MPIOSM_DIO led5;
	MPIOSM_DIO led6;
	MPIOSM_DIO led7;
	MPIOSM_DIO led8;
	
	
	public FlashLight() {
		led1 = new MPIOSM_DIO(5, true);
		led2 = new MPIOSM_DIO(6, true);
		led3 = new MPIOSM_DIO(7, true);
		led4 = new MPIOSM_DIO(8, true);
		led5 = new MPIOSM_DIO(9, true);
		led6 = new MPIOSM_DIO(10, true);
		led7 = new MPIOSM_DIO(11, true);
		led8 = new MPIOSM_DIO(12, true);
		
	}
	
	public void action() {
		
		
		led1.set(false);
		led2.set(false);
		led3.set(false);
		led4.set(false);
		led5.set(false);
		led6.set(false);
		led7.set(false);
		led8.set(false);
		
		led1.set(counter == 1);
		led2.set(counter == 2);
		led3.set(counter == 3);
		led4.set(counter == 4);
		led5.set(counter == 5);
		led6.set(counter == 6);
		led7.set(counter == 7);
		led8.set(counter == 8);
		
		if(dir) {
			counter--;
			
		}else {
			counter ++;
		}
		
		inv = switch1.get();
		
		if (counter >= 8) {
			dir = true;
			
		}else if(counter <= 1) {
			dir = false;
		}
		if(inv != lastinv) {
			dir = !dir;
		}
		lastinv = inv;
	}
	
	static {
		Task t = new FlashLight();
		t.period = 35;
		Task.install(t);
	}

}
