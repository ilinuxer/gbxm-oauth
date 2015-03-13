package zx.soft.twitter.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import zx.soft.twitter.code.TwitterConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by jimbo on 15-3-12.
 */
public class TwitterOAuthServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(TwitterOAuthServlet.class);

    protected static Twitter twitter = new TwitterFactory().getInstance();
    protected static RequestToken requestToken = null;

    AccessToken twitterToken = null;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.info(twitter.toString());
        String pin = request.getParameter("pin");
        if(pin.length()>0){
            try {
                twitterToken = twitter.getOAuthAccessToken(requestToken,pin);
                //此处进行数据库操作
                System.out.println("token:" + twitterToken.getToken());
                System.out.println("secret:" + twitterToken.getTokenSecret());
                response.getWriter().println("ok");
            } catch (TwitterException e) {
                logger.error("获取token出错。");
                throw new RuntimeException(e);
            }
        }else{
            response.getWriter().println("wrong");
        }



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String result;
        setConsumer();
        logger.info(twitter.toString());
        try {
            System.out.println(twitter.toString());
            requestToken = twitter.getOAuthRequestToken();
            result = requestToken.getAuthorizationURL();
        } catch (TwitterException e) {
            logger.error("获取requestToken的时候出错。");
            throw new RuntimeException(e);
        }
        response.getWriter().println(result);
    }

    protected void setConsumer(){

        Properties config = TwitterConfig.getPropConfig("twitterapp.properties");
        String consumerKey = config.getProperty("consumer.key");
        String consumerSecret = config.getProperty("consumer.sercert");

        twitter.setOAuthConsumer(consumerKey,consumerSecret);
    }
}
