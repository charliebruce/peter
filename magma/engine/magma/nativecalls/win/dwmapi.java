package magma.nativecalls.win;

import com.sun.jna.*;

public interface dwmapi extends Library {
  dwmapi INSTANCE = (dwmapi)Native.loadLibrary("dwmapi",dwmapi.class);
  //http://msdn.microsoft.com/en-us/library/aa969510%28VS.85%29.aspx
  public int DwmEnableComposition(int uCompositionAction);
}