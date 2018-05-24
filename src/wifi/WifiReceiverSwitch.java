package wifi;


import java.io.PrintStream;

import ch.ntb.inf.deep.runtime.ppc32.Task;
import ch.ntb.inf.deep.runtime.util.CmdInt;
import ch.ntb.inf.deep.runtime.mpc555.driver.MPIOSM_DIO;
import ch.ntb.inf.deep.runtime.mpc555.driver.RN131;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;

public class WifiReceiverSwitch extends Task{
	final int resetPin = 11;
		
	private RN131 wifi;
	final int pin5 = 5, pin6 = 6;
	private int protokoll;
	public MPIOSM_DIO led1, led2;
	
	public WifiReceiverSwitch() throws Exception {
		period = 500;
		
		SCI sci = SCI.getInstance(SCI.pSCI2);
		sci.start(115200, SCI.NO_PARITY, (short)8);

		wifi = new RN131(sci.in , sci.out, new MPIOSM_DIO(11, true));
		protokoll = 0;
		led1 = new MPIOSM_DIO(pin5, true);
		led2 = new MPIOSM_DIO(pin6, true);
		
	}
	
	public void action(){
		
		while (true) {
			CmdInt.Type type = wifi.cmd.readCmd();
			if (type == CmdInt.Type.None) break;
			if (type == CmdInt.Type.Cmd) {
				protokoll = wifi.cmd.getInt();
				switch(protokoll){
				case 0:
					led1.set(false);
					led2.set(false);
					break;
				case 10:
					led1.set(false);
					led2.set(true);
					break;
				case 11:
					led1.set(true);
					led2.set(true);
					break;
				case 1:
					led1.set(true);
					led2.set(false);
					break;
				default:
					led1.set(false);
					led2.set(false);
					break;
				}
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
			WifiReceiver task = new WifiReceiver();
			task.period = 500;
			Task.install(task);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
