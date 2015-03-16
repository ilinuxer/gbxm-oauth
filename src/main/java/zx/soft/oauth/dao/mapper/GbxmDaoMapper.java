package zx.soft.oauth.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import zx.soft.oauth.dao.domain.FacebookToken;
import zx.soft.oauth.dao.domain.GplusAPP;

/**
 * Created by jimbo on 15-3-9.
 */
public interface GbxmDaoMapper {

    @Insert("INSERT INTO `fb_token` (`token`,`expire_at`) VALUES (#{accessToken},#{expiresAt})")
    public void insertFBToken(FacebookToken token);

    @Insert("INSERT INTO `gplus_app` (`app_name`,`client_id`,`client_secret`) VALUES (#{appName},#{clientId},#{clientSecret})")
    public void insertGplusApp(GplusAPP app);
}
