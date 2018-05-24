package Motors;

import java.io.PrintStream;

import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
//import ch.ntb.inf.deep.runtime.mpc555.*;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_PWM;


public class ServoMotor {
	final boolean useTPUA = true;
	final int pwmPeriode = 20000000 / TPU_PWM.tpuTimeBase;
	final int minHighTime = 1200000 / TPU_PWM.tpuTimeBase;
	final int maxHighTime = 2200000 / TPU_PWM.tpuTimeBase;
	int lastPos = 0;
	int defaultPosServo = 0;
	int speedValue = 10;
	int minPosServo;
	int maxPosServo;
	int delayRounds = 0;
	
	int highTime = 0;
	
	private TPU_PWM pwm;
	
	public ServoMotor(int pwmChannel, int defaultPos ,int minPos, int maxPos ) {
		pwm = new TPU_PWM(useTPUA, pwmChannel, pwmPeriode, minHighTime);
		int highTime = (1200000 + (defaultPos * 11111)) / TPU_PWM.tpuTimeBase;
		pwm.update(highTime);
		defaultPosServo = defaultPos;
		lastPos = defaultPos;
		minPosServo = minPos;
		maxPosServo = maxPos;
		
		/*SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(19200, SCI.NO_PARITY, (short)8);
		System.out = new PrintStream(sci1.out);*/
	}
	
	public void setPositionSlow(int degree)				// Buggy meeds to be fixed
	{
		delayRounds++;
		//System.out.print(delayRounds);
		//System.out.print("   ");
		//System.out.println(speedValue);
		if (delayRounds >= speedValue ) {
			//System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
			delayRounds = 0;
			if ((degree>= minPosServo) && (degree <= maxPosServo)) {
				if (degree>lastPos) {
					int nextPos = lastPos+1;
					highTime = (1200000 + (nextPos * 11111)) / TPU_PWM.tpuTimeBase;
					lastPos = nextPos;
					//System.out.print("traget > last ");
					//System.out.println(highTime);
					pwm.update(highTime);
				}
				else if (degree<lastPos) {
					int nextPos = lastPos-1;
					highTime = (1200000 + (nextPos * 11111)) / TPU_PWM.tpuTimeBase;
					lastPos = nextPos;
					//System.out.print("traget < last ");
					//System.out.println(highTime);
					pwm.update(highTime);
				}
				//pwm.update(highTime);
			} else if ((degree <= minPosServo) && (degree < maxPosServo)) {
				highTime = (1200000 + (minPosServo * 11111)) / TPU_PWM.tpuTimeBase;
				pwm.update(highTime);
				//System.out.print("min ");
				//System.out.println(highTime);
			} else if ((degree > minPosServo) && (degree >= maxPosServo)) {
				highTime = (1200000 + (maxPosServo * 11111)) / TPU_PWM.tpuTimeBase;
				pwm.update(highTime);
				//System.out.print("max ");
				//System.out.println(highTime);
			}
		}
	}
	
	/*public void setPositionSlow(int degree)					// Working with bugs please fix!
	{
		if((lastMovement+speedValue)<System.currentTimeMillis()) { // 50000 equals 50ms
			int targetPos = (1200000 + (degree * 11111));
			int nextPos;
			if (targetPos>lastPos) {
				nextPos = lastPos+11111;
			}
			else if (targetPos<lastPos) {
				nextPos = lastPos-11111;
			}
			else {
				nextPos = lastPos;
			}
			
			int highTime = nextPos / TPU_PWM.tpuTimeBase;
			
			if (highTime > minHighTime && highTime < maxHighTime) {		// tried to constrain hightime, but no effect...
				pwm.update(highTime);
				lastPos = nextPos;
			} else if (highTime < minHighTime) {
				pwm.update(minHighTime);
				lastPos = minHighTime*TPU_PWM.tpuTimeBase;
			} else {
				pwm.update(maxHighTime);
				lastPos = maxHighTime*TPU_PWM.tpuTimeBase;
			}
			
			lastMovement = System.currentTimeMillis();
		}	
	}*/
	
	
	public void setPosition(int degree)
	{
		int highTime = 0;
		if ((degree>= minPosServo) && (degree <= maxPosServo)) {
			highTime = (1200000 + (degree * 11111)) / TPU_PWM.tpuTimeBase;
		} else if ((degree < minPosServo) && (degree < maxPosServo)) {
			highTime = (1200000 + (minPosServo * 11111)) / TPU_PWM.tpuTimeBase;
		} else if ((degree > minPosServo) && (degree > maxPosServo)) {
			highTime = (1200000 + (maxPosServo * 11111)) / TPU_PWM.tpuTimeBase;
		} else {
			highTime = defaultPosServo;
		}
		
		pwm.update(highTime);
	}
	
}
