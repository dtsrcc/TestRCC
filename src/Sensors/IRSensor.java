package Sensors;

import java.io.PrintStream;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.mpc555.driver.QADC_AIN;

public class IRSensor {

	// Analog Input
	QADC_AIN AIN = new QADC_AIN();
	// > 1000 -> kein Objekt, < 50 -> Objekt
	
	boolean IRSensor = false;
	 

		public IRSensor() {
			
			QADC_AIN.init(true);
			
		}
	
		
		public boolean getSensorInput() {
			
			if (QADC_AIN.read(true, 0) > 1000) {
				IRSensor = false;
			} else if (QADC_AIN.read(true, 0) < 50) {
				IRSensor = true;
				}
			
			return IRSensor;
		
		}
	
	
}
