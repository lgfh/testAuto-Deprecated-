package Utils;

import redis.clients.jedis.Jedis;

/**
 * @author WXY
 * @version 创建时间：2018/11/1
 */
public class RedisUtil {
    public static String getRedisConnectionResult(String ip, String passwd, int port) {
        Jedis jedis = new Jedis(ip, port);
        jedis.auth(passwd);
        String pingResult = jedis.ping();
        return pingResult;
    }

    public static String getRedisConnectionResult(String ip, String passwd) {
        Jedis jedis = new Jedis(ip, 6379);
        jedis.auth(passwd);
        String pingResult = jedis.ping();
        return pingResult;
    }
}
