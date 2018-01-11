
import java.io.PrintStream;
import ch.ntb.inf.deep.runtime.ppc32.Task;
import ch.ntb.inf.deep.runtime.mpc555.driver.SCI;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_FQD;
import ch.ntb.inf.deep.runtime.mpc555.driver.TPU_PWM;
// comment
public class DecoderBlub extends Task {
	final byte tpuPin4 = 4;
	final boolean useTPUA = true;
	private TPU_FQD fqd;
	private TPU_PWM pwm1;
	private TPU_PWM pwm2;

	public DecoderBlub(){
		pwm1 = new TPU_PWM(true, 0, 100000 / TPU_PWM.tpuTimeBase, 0);
		pwm2 = new TPU_PWM(true, 1, 100000 / TPU_PWM.tpuTimeBase, 0);
		fqd = new TPU_FQD(useTPUA, tpuPin4);
		period = 500;
		Task.install(this);
	}

	public void action() {
		pwm2.update(10000/ TPU_PWM.tpuTimeBase);
		pwm1.update(0/ TPU_PWM.tpuTimeBase);
		int pos = fqd.getPosition();
		System.out.println(pos);
	}

	static {
		// 1) Initialize SCI1 (9600 8N1)
		SCI sci1 = SCI.getInstance(SCI.pSCI1);
		sci1.start(9600, SCI.NO_PARITY, (short)8);
		// 2) Use SCI1 for stdout
		System.out = new PrintStream(sci1.out);
		System.out.println("Hallo");
		new DecoderBlub();

	}
}

