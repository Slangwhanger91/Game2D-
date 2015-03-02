import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

class Settings extends Properties {
    Settings() {
        try {
            InputStream in = new FileInputStream("config.properties");
            load(in);
        }
        catch (IOException ioe) {
            System.out.println("Could not read configuration file. God help us all! ABORT ABORT ABORT");
            System.exit(1);
        }
    }
}
