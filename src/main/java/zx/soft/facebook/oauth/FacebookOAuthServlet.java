package zx.soft.facebook.oauth;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zx.soft.oauth.dao.FBDao;
import zx.soft.oauth.dao.common.DaoConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jimbo on 15-3-8.
 */
public class FacebookOAuthServlet extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(FacebookOAuthServlet.class);
    private FBDao dao = new FBDao(DaoConfig.Servers.GBXM);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accessToken = request.getParameter("access_token");
        String userId = request.getParameter("user_id");
        String expiresIn = request.getParameter("expires_in");


        FacebookClient.AccessToken newToken = new DefaultFacebookClient().obtainExtendedAccessToken("342572835951145", "ef8db461ff0067d0e83f8b8fe05ce736", accessToken);
        logger.info("get extend access token!");
        dao.insertFBToken(newToken.getAccessToken(),newToken.getExpires());

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
