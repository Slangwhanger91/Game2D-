import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

@SuppressWarnings("serial")
class Settings extends Properties {
    Settings(String infile) {
        try {
            InputStream in = new FileInputStream(infile);
            load(in);
        }
        catch (IOException ioe) {
            System.out.println("Could not read configuration file. God help us all! ABORT ABORT ABORT");
            System.exit(1);
        }
    }

    /* Use these to get values out of the file. key=value pairs. */
    String get(String key) { return getProperty(key); } 
    String get(String key, String def) { return getProperty(key, def); }

    /*
     * Alternative to get where if there is no key for `key` it will throw an
     * exception. Useful to indicate config values that *has* to be included,
     * and not let the program run when they are not set.
     */
    String getRequired(String key) throws BadConfigException {
        if (containsKey(key)) {
            return getProperty(key);
        } else {
            throw new BadConfigException("required property not found: " + key);
        }
    }
}

@SuppressWarnings("serial")
class BadConfigException extends Exception {
    public BadConfigException(String message) {
        super(message);
    }
}
