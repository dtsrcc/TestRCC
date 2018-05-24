package wifi;


import java.io.PrintStream;

import ch.ntb.inf.deep.runtime.ppc32.Task;
import ch.ntb.inf.deep.runtime.mpc555.driver.MPIOSM_DIO;
import ch.ntb.inf.deep.runtime.mpc555.driver.RN131;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;

public class WifiTransmitterLED extends Task{
	final int  resetPin = 11;
	int sendData = 12;
	private boolean send;
	
	private RN131 wifi;
	
	public MPIOSM_DIO led;
	boolean ledState;
	
	public WifiTransmitterLED() throws Exception {
		//period = 500;
		
		SCI sci = SCI.getInstance(SCI.pSCI2);
		sci.start(115200, SCI.NO_PARITY, (short)8);

		wifi = new RN131(sci.in , sci.out, new MPIOSM_DIO(11, true));
		
		send = false;
		led = new MPIOSM_DIO(14, true);
		ledState = false;
		led.set(ledState);
	}
	
		
	public void sendCmd() {
		if (wifi.connected()) {
			wifi.cmd.writeCmd(sendData);
		}
	}
	
	public void action(){
		RN131.State state = wifi.getState();
		ledState = !ledState;
		switch(state){
		case wait:
			led.set(ledState);
			break;
		case ready:
			led.set(true);
			break;
		default:
			led.set(false);
			break;
		}
		if (send) {
			sendCmd();
			send = false;
		}else{
			send = true;
		}
	}
	
	static{
		//Init SCI1 (19200 8N1)
		SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(19200, SCI.NO_PARITY, (short)8);
		//Hook SCI1.out on System.out
		System.out = new PrintStream(sci1.out);	
		
		// Install and start the task
		try{
			WifiTransmitterLED task = new WifiTransmitterLED();
			task.period = 500;
			Task.install(task);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
