package zx.soft.oauth.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import zx.soft.oauth.dao.domain.FacebookToken;

/**
 * Created by jimbo on 15-3-9.
 */
public interface GbxmDao {

    @Insert("INSERT INTO `fb_token` (`token`,`expire_at`) VALUES (#{accessToken},#{expiresAt})")
    public void insertFBToken(FacebookToken token);
}
