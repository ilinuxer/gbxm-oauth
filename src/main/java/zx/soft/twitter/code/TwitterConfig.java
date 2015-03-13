package zx.soft.twitter.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by jimbo on 15-3-12.
 */
public class TwitterConfig {
    private static Logger logger = LoggerFactory.getLogger(TwitterConfig.class);

    public static Properties getPropConfig(String fileName){
        Properties result = new Properties();
        try(InputStream inputStream = TwitterConfig.class.getClassLoader().getResourceAsStream(fileName)){
            result.load(inputStream);
            return result;
        } catch (IOException e) {
            logger.error("Error during reading "+fileName);
            throw new RuntimeException(e);
        }
    }
}
