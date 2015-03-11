package zx.soft.google.oauth.code;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
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
import zx.soft.utils.threads.ApplyThreadPool;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by jimbo on 15-3-11.
 */
public class AuthorizationUrl {

    private static HttpTransport httpTransport;

    private ThreadPoolExecutor pool = ApplyThreadPool.getThreadPoolExector();

    public String getAuthorizationUrl(String appName, String clientId, String clientSecret) throws IOException {

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
                JSON_FACTORY, clientId, clientSecret, Collections.singleton(PlusScopes.PLUS_ME)).setDataStoreFactory(dataStoreFactory).build();

        LocalServerReceiver.Builder builder = new LocalServerReceiver.Builder().setHost("localhost").setPort(8088);

        VerificationCodeReceiver receiver = builder.build();


        Credential credential = flow.loadCredential("user");
        if (credential == null || credential.getRefreshToken() == null && credential.getExpiresInSeconds() <= 60L) {
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
                System.out.println("test THREAD");
                String code = receiver.waitForCode();
                TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri1).execute();
                Credential temp = flow.createAndStoreCredential(response, "user");
                System.out.println(temp);
                System.out.println("test ended");
            } catch (IOException e) {
                System.out.println("test CThread error");
                e.printStackTrace();
            } finally {
                try {
                    receiver.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
