import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

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

    String get(String key) { return getProperty(key); } 
    String get(String key, String def) { return getProperty(key, def); }

    String getRequiredProperty(String key) throws BadConfigException {
        if (containsKey(key)) {
            return getProperty(key);
        } else {
            throw new BadConfigException("required property not found: " + key);
        }
    }
}
