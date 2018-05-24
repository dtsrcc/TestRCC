package wifi;


import java.io.PrintStream;

import ch.ntb.inf.deep.runtime.ppc32.Task;
import ch.ntb.inf.deep.runtime.mpc555.driver.MPIOSM_DIO;
import ch.ntb.inf.deep.runtime.mpc555.driver.RN131;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;

public class WifiTransmitterSwitch extends Task{
	final int  resetPin = 11;
	public MPIOSM_DIO switchButton1, switchButton2;
	final int switch10 = 10, switch11 = 11;
	private int protokoll; //1: Schalter 1 on, 10: Schalter 2 on, 
						   //11:Schalter 1 und 2 on, 0: Schalter 1 und 2 off
	private int lastprotokoll;
	private RN131 wifi;
	
	public WifiTransmitterSwitch() throws Exception {
		//period = 500;
		
		SCI sci = SCI.getInstance(SCI.pSCI2);
		sci.start(115200, SCI.NO_PARITY, (short)8);

		wifi = new RN131(sci.in , sci.out, new MPIOSM_DIO(11, true));
				
		switchButton1 = new MPIOSM_DIO(switch10, false);
		switchButton2 = new MPIOSM_DIO(switch11, false);
		protokoll = 0;
		lastprotokoll = -1;
	}
	
	public void action(){
		
		if (switchButton1.get() && switchButton2.get()){
			//11
			protokoll = 11;
		}else if(!switchButton1.get() && switchButton2.get()){
			protokoll = 10;
		}else if(switchButton1.get() && !switchButton2.get()){
			protokoll = 1;
		}else if(!switchButton1.get() && !switchButton2.get()){
			protokoll = 0;
		}
		if (lastprotokoll != protokoll){
			lastprotokoll = protokoll;
			if (wifi.connected()) {
				wifi.cmd.writeCmd(protokoll);
			}
			
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
			WifiTransmitter task = new WifiTransmitter();
			task.period = 500;
			Task.install(task);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
