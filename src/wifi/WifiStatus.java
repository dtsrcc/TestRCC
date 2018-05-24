package wifi;


import ch.ntb.inf.deep.runtime.ppc32.Task;
import ch.ntb.inf.deep.runtime.mpc555.driver.MPIOSM_DIO;

public class WifiStatus extends Task{
	private static final int ledPin1 = 12;
	private static final int ledPin2 = 13;
	private static WifiStatus task;
	public static enum STATE {off, disconnected, connected, error, sending};
	public static STATE state;
	public MPIOSM_DIO led1, led2; 
	
	private WifiStatus(){
		led1 = new MPIOSM_DIO(ledPin1, true);
		led2 = new MPIOSM_DIO(ledPin2, true);
		state = STATE.off;
	}
	
	public static WifiStatus getInstance(){
		if (task == null){
			task = new WifiStatus();
			task.period = 1000;
			Task.install(task);
		}
		return task;
	}
	
	public void action(){
		switch(state){
		case disconnected:
			led1.set(false);
			led2.set(false);
			break;
		case connected:
			led1.set(true);
			led2.set(false);
			break;
		case error:
			led1.set(!led1.get());
			led2.set(false);
			break;
		case sending:
			led2.set(!led2.get());
			break;
		case off:
			led2.set(false);
		}
	}
	
	public void setState(STATE state){
		WifiStatus.state = state;
	}
	
	static {
		task = null;
	}
}
