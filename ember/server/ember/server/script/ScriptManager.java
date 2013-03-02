package ember.server.script;

import ember.util.*;
/*import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;*/

import java.io.File;
import java.io.IOException;

/**
 * Manager responsible for executing Python or LUA scripts.
 * @author charlie
 *
 */

public class ScriptManager {

    private static ScriptManager instance = new ScriptManager();

    public static ScriptManager getInstance() {
        return instance;
    }

   // private PythonInterpreter interp;
    private Logger logger = Logger.getInstance();

    public ScriptManager() {
     //   interp = new PythonInterpreter();
    }

    /*public boolean invoke(String name, Object... bindings) {
        try {
            PyObject obj = interp.get(name);
            if (obj != null && obj instanceof PyFunction) {
                PyFunction func = (PyFunction) obj;
                PyObject[] objects = new PyObject[bindings.length];
                for (int i = 0; i < bindings.length; i++) {
                    Object bind = bindings[i];
                    objects[i] = Py.java2py(bind);
                }
                func.__call__(objects);
            } else {
                return false;
            }
            return true;
        } catch (PyException e) {
            System.out.println(e.type);
            System.out.println(e.value);
            System.out.println(e.traceback);
            Logger.getInstance().error("Problem while parsing: " + name + ":");
            Logger.getInstance().stackTrace(e);
            return false;
        }
    }*/

    public void reload() throws IOException {
        logger.info("Loading scripts...");
        //interp.cleanup();
        File scriptDir = new File("data/scripts/");
        int parsed = 0;
        if (scriptDir.isDirectory()) {
            File[] children = scriptDir.listFiles();
            for (File child : children) {
                if (child.isFile() && child.getName().endsWith(".py")) {
                    //interp.execfile(new FileInputStream(child));
                    parsed++;
                }
            }
        }
        File packageDir = new File("packages/");
        if (packageDir.isDirectory()) {
            File[] children = packageDir.listFiles();
            for (File child : children) {
                if (child.isFile() && child.getName().endsWith(".py")) {
                   // interp.execfile(new FileInputStream(child));
                    parsed++;
                }
            }
        }
        logger.info("Parsed " + parsed + " scripts.");
    }

}
