import ch.ntb.inf.deep.runtime.mpc555.driver.HD44780U;
import ch.ntb.inf.deep.runtime.ppc32.Task;

public class DisplayTest {
	//private static final char Helloidiot = 0;
	//Test made by Dimitri
	
	private HD44780U DisplayObj;
	
	public DisplayTest() {
		
		DisplayObj = HD44780U.getInstance();
		
		DisplayObj.init(2);
		
		DisplayObj.onOff(true, true, true);
		
		DisplayObj.clearDisplay();
		
		DisplayObj.setCursor(0,0);
		
		DisplayObj.writeChar('H');
		
		DisplayObj.setCursor(1,1);
		
		DisplayObj.writeInt(1,1);
	}
	
	
	public void action() {
		
		//DisplayObj.writeChar(Helloidiot);
		
		//DisplayObj.writeChar('H');
		
	}
	


}
