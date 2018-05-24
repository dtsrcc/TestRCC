package wifi;


public enum CmdState {init(0), setServo90(2), setDC90(4), setServo45(5),
		dc90Reached(191), servo45Reached(46), servo90(90), dc90(190), servo45(45), dcGo(200), dcStop(210);
	private int cmd;
	CmdState(int val){cmd = val;}
	public int getCmd(){return cmd;}
	public void setCmd(int val){cmd = val;}
}
