package zx.soft.oauth.dao.domain;

/**
 * Created by jimbo on 15-3-12.
 */
public class TwitterToken {

    private String accessToken;
    private String tokenSecret;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public TwitterToken(String accessToken, String tokenSecret) {
        this.accessToken = accessToken;
        this.tokenSecret = tokenSecret;
    }
}
