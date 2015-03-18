package zx.soft.google.oauth.code;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.plus.PlusScopes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zx.soft.utils.threads.ApplyThreadPool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by jimbo on 15-3-11.
 */
public class GplusAuthorizationUrl {
    private static Logger logger = LoggerFactory.getLogger(GplusAuthorizationUrl.class);

    private static HttpTransport httpTransport;

    private ThreadPoolExecutor pool = ApplyThreadPool.getThreadPoolExector();

    public String getAuthorizationUrl(String appName, String clientId, String clientSecret) throws IOException {

        Properties redirectProp = new Properties();

        try(InputStream inputStream = GplusAuthorizationUrl.class.getClassLoader().getResourceAsStream("gplus-oauth.properties")){
            redirectProp.load(inputStream);
        }catch (IOException e){
            logger.error("加载 gplus-oauth.properties 文件的时候出错！");
            throw new RuntimeException(e);
        }

        String redirectUrl = redirectProp.getProperty("gplus.redirecturl");
        int redirectPort = Integer.parseInt(redirectProp.getProperty("gplus.redirectport"));
        //授权参数准备
        File dataStoreDir = new File(System.getProperty("user.home"), ".gplus/credentials_" + appName);
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(dataStoreDir);
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
                JSON_FACTORY, clientId, clientSecret, Collections.singleton(PlusScopes.PLUS_ME)).setDataStoreFactory(dataStoreFactory)
                .addRefreshListener(new DataStoreCredentialRefreshListener("user", dataStoreFactory))
                .setAccessType("offline").setApprovalPrompt("force").build();

        LocalServerReceiver.Builder builder = new LocalServerReceiver.Builder().setHost(redirectUrl).setPort(redirectPort);

        VerificationCodeReceiver receiver = builder.build();

        Credential credential = flow.loadCredential("user");

        if (credential == null || credential.getRefreshToken() == null && credential.getExpiresInSeconds().longValue() <= 60L) {
            String redirectUri1 = receiver.getRedirectUri();
            pool.execute(new ThreadOne(flow, receiver, redirectUri1));
            AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri1);
            Preconditions.checkNotNull(authorizationUrl);

            return authorizationUrl.toString();
        } else {
            return "authorized";
        }
    }

    public class ThreadOne implements Runnable {
        private GoogleAuthorizationCodeFlow flow;
        private VerificationCodeReceiver receiver;
        private String redirectUri1;

        public ThreadOne(GoogleAuthorizationCodeFlow flow, VerificationCodeReceiver receiver, String redirectUri1) {
            this.flow = flow;
            this.receiver = receiver;
            this.redirectUri1 = redirectUri1;
        }

        @Override
        public void run() {
            try {
                String code = receiver.waitForCode();
                logger.error("返回码：：：" + code);
                TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri1).execute();
                Credential temp = flow.createAndStoreCredential(response, "user");
            } catch (IOException e) {
                logger.error("认证过程出错");
                throw new RuntimeException(e);
            } finally {
                try {
                    receiver.stop();
                } catch (IOException e) {
                    logger.error("认证过程出错");
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
