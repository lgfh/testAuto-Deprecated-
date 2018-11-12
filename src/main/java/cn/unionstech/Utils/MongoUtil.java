package cn.unionstech.Utils;

import com.mongodb.MongoClient;

/**
 * @author WXY
 * @version 创建时间：2018/11/1
 */
public class MongoUtil {
    public static String getMongoConnectionResult(String ip) {

        String mongoConnectionPoint = null;
        try {
            MongoClient mongoClient = new MongoClient(ip, 27017);
            mongoConnectionPoint = mongoClient.getConnectPoint();


        } catch (Exception e) {
            e.getMessage();
        }
        if (mongoConnectionPoint != null) {
            return mongoConnectionPoint;
        }
        return null;
    }

}
