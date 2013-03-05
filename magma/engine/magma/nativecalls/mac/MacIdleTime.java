package magma.nativecalls.mac;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class MacIdleTime {

	public interface ApplicationServices extends Library {

		ApplicationServices INSTANCE = (ApplicationServices) Native.loadLibrary("ApplicationServices", ApplicationServices.class);
		
		int kCGAnyInputEventType = ~0;
		int kCGEventSourceStatePrivate = -1;
		int kCGEventSourceStateCombinedSessionState =0;
		int kCGEventSourceStateHIDSystemState = 1;
		
		/**
		* @see http://developer.apple.com/mac/library/documentation/Carbon/Reference/QuartzEventServicesRef/Reference/reference.html#//apple_ref/c/func/CGEventSourceSecondsSinceLastEventType
		* @param sourceStateId
		* @param eventType
		* @return the elapsed seconds since the last input event
		*/
		public double CGEventSourceSecondsSinceLastEventType (int sourceStateId, int eventType);
	}

	public long getIdleTimeMilliseconds() {
		double idleTimeSeconds = ApplicationServices.INSTANCE.CGEventSourceSecondsSinceLastEventType(ApplicationServices.kCGEventSourceStateCombinedSessionState, ApplicationServices.kCGAnyInputEventType);
		return (long) (idleTimeSeconds * 1000);
	}
}	