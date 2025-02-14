package bot.getfromenvs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class GetFromEnvs {
    Properties properties = new Properties();
    FileInputStream input = null;

    public String getFromEnvsByName(String name){
        try {
            input = new FileInputStream("config.properties");
            properties.load(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty(name);
    }
}
