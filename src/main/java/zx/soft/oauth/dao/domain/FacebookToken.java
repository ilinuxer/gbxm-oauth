package zx.soft.oauth.dao.domain;

import java.util.Date;

/**
 * Created by jimbo on 15-3-9.
 */
public class FacebookToken {
    private String accessToken;
    private Date expiresAt;

    public FacebookToken(String accessToken, Date expiresAt) {
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}
