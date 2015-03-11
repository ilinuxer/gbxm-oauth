package zx.soft.oauth.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zx.soft.oauth.dao.common.DaoConfig;
import zx.soft.oauth.dao.domain.FacebookToken;
import zx.soft.oauth.dao.mapper.GbxmDao;

import java.util.Date;

/**
 * Created by jimbo on 15-3-9.
 */
public class FBDao {
    private static Logger logger = LoggerFactory.getLogger(FBDao.class);

    private final SqlSessionFactory sqlSessionFactory;

    public FBDao(DaoConfig.Servers server) {
        try{
            sqlSessionFactory = DaoConfig.getSqlSessionFactory(server);

        }catch (RuntimeException e){
            logger.error("SpecoalQuery RuntimeException:" + e);
            throw new RuntimeException(e);
        }
    }

    public void insertFBToken(String accessToken,Date expiresAt){
        try(SqlSession sqlSession = sqlSessionFactory.openSession();){
            GbxmDao gbxmDao = sqlSession.getMapper(GbxmDao.class);
            gbxmDao.insertFBToken(new FacebookToken(accessToken,expiresAt));
        }
    }
}
