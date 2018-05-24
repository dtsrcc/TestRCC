package wifi;


import java.io.PrintStream;

import ch.ntb.inf.deep.runtime.ppc32.Task;
import ch.ntb.inf.deep.runtime.util.CmdInt;
import ch.ntb.inf.deep.runtime.mpc555.driver.MPIOSM_DIO;
import ch.ntb.inf.deep.runtime.mpc555.driver.RN131;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;

public class WifiReceiver extends Task {
	final int resetPin = 11;
	public MPIOSM_DIO led;
	boolean ledState;
	
	private RN131 wifi;
	
	public WifiReceiver() throws Exception {
		period = 500;
		
		SCI sci = SCI.getInstance(SCI.pSCI2);
		sci.start(115200, SCI.NO_PARITY, (short)8);

		wifi = new RN131(sci.in , sci.out, new MPIOSM_DIO(11, true));
		
		led = new MPIOSM_DIO(14, true);
		ledState = false;
		led.set(ledState);
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
		while (true) {
			CmdInt.Type type = wifi.cmd.readCmd();
			if (type == CmdInt.Type.None) break;
			if (type == CmdInt.Type.Cmd) {
				System.out.print("command=");
				System.out.print(wifi.cmd.getInt());
			}
			else if (type == CmdInt.Type.Code) {
				System.out.print("code=");
				System.out.print(wifi.cmd.getInt());
			}
			else if (type == CmdInt.Type.Unknown) {
				System.out.print("unknown(");
				System.out.print(wifi.cmd.getHeader());
				System.out.print(")=");
				System.out.print(wifi.cmd.getInt());
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
