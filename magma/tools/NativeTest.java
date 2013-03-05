import magma.nativecalls.win.dwmapi;

public class NativeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		dwmapi.INSTANCE.DwmEnableComposition(0);
	}

}
