import ch.ntb.inf.deep.runtime.mpc555.driver.HD44780U;
import ch.ntb.inf.deep.runtime.ppc32.Task;

public class DisplayTest {
	private static final char Helloidiot = 0;
	//Test made by Pascal
	
	private HD44780U DisplayObj;
	
	public DisplayTest() {
		
		DisplayObj = HD44780U.getInstance();
		
		DisplayObj.init(2);
		
		DisplayObj.onOff(true, true, true);
		
		DisplayObj.setCursor(1,1);
	}
	
	public void clear_Display() {
		
		DisplayObj.clearDisplay();
		
	}
	
	public void action() {
		
		DisplayObj.writeChar(Helloidiot);
		
		// DisplayObj.writeInt(7, 2);
		
	}
	


}
