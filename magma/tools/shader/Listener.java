package shader;

import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowListener;
import com.jogamp.newt.event.WindowUpdateEvent;

public class Listener implements WindowListener {

	@Override
	public void windowDestroyNotify(WindowEvent arg0) {
		System.exit(0);

	}

	@Override
	public void windowDestroyed(WindowEvent arg0) {
		System.exit(0);
	}

	@Override
	public void windowGainedFocus(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowLostFocus(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowMoved(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowRepaint(WindowUpdateEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowResized(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

}
