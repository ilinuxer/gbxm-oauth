package zx.soft.oauth.dao;

import zx.soft.oauth.dao.common.DaoConfig;

import java.util.Date;

/**
 * Created by jimbo on 15-3-9.
 */
public class FBDaoDemo {


    public static void main(String[] args) {
        FBDao dao = new FBDao(DaoConfig.Servers.GBXM);

        dao.insertFBToken("test",new Date());
    }

}
