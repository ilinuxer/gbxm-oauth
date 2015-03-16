package zx.soft.oauth.dao.domain;

/**
 * Created by jimbo on 15-3-16.
 */
public class GplusAPP {

    private String appName;
    private String clientId;
    private String clientSecret;

    public GplusAPP(String appName, String clientId, String clientSecret) {
        this.appName = appName;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
