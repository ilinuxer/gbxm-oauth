package zx.soft.google.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zx.soft.google.oauth.code.AuthorizationUrl;
import zx.soft.utils.threads.ApplyThreadPool;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by jimbo on 15-3-8.
 */
public class GoogleOAuthServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(GoogleOAuthServlet.class);

    private AuthorizationUrl authorizationUrl = new AuthorizationUrl();

    private ThreadPoolExecutor pool = ApplyThreadPool.getThreadPoolExector();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String appName = request.getParameter("appName");
        String clientId = request.getParameter("clientId");
        String clientSecret = request.getParameter("clientSecret");
        logger.info("appName"+"="+appName);
        logger.info("clientId"+"="+clientId);
        logger.info("clientSecret"+"="+clientSecret);
        String url= authorizationUrl.getAuthorizationUrl(appName,clientId,clientSecret);
        System.out.println(url);
        response.getWriter().println(url);

//        pool.execute(new ThreadOne(response,url));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

//    public class ThreadOne implements Runnable{
//
//        private HttpServletResponse response;
//        private String url;
//
//        public ThreadOne(HttpServletResponse response,String url) {
//            this.response = response;
//            this.url = url;
//        }
//
//        @Override
//        public void run() {
//            try {
//                response.getWriter().println(url);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

}
