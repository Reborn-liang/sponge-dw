package cn.nearf.ggz.redis.redis;

import java.util.ArrayList;
import java.util.List;

import org.jeecgframework.core.util.PropertiesUtil;

import cn.nearf.ggz.config.ConfigUtils;
import cn.nearf.ggz.utils.ObjectUtils;
import cn.nearf.ggz.utils.StringUtils;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;


public class RedisService {
	private static PropertiesUtil redisPro = new PropertiesUtil("dbconfig.properties");
	
	private static ShardedJedisPool pool = null;

	/**
	 * 构建redis连接池
	 * 
	 * @param ip
	 * @param port
	 * @return JedisPool
	 */
	private synchronized static ShardedJedisPool getPool() {
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			
			//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
			config.setBlockWhenExhausted(ConfigUtils.getBooleanProperty(redisPro, "redis.blockWhenExhausted"));
			config.setTimeBetweenEvictionRunsMillis(ConfigUtils.getLongProperty(redisPro, "redis.timeBetweenEvictionRunsMillis"));
			config.setMinEvictableIdleTimeMillis(ConfigUtils.getLongProperty(redisPro, "redis.minEvictableIdleTimeMillis"));
			
//			// 设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
//			config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
//			// 是否启用pool的jmx管理功能, 默认true
//			config.setJmxEnabled(true);
			
			// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
			// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
			config.setMaxTotal(ConfigUtils.getIntProperty(redisPro, "redis.maxTotal"));
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(ConfigUtils.getIntProperty(redisPro, "redis.maxIdle"));
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(ConfigUtils.getIntProperty(redisPro, "redis.maxWaitMillis"));
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(ConfigUtils.getBooleanProperty(redisPro, "redis.testOnBorrow"));
			config.setTestOnReturn(ConfigUtils.getBooleanProperty(redisPro, "redis.testOnReturn"));

//			String servers = ConfigUtils.getStringProperty(redisPro, "ggz.ccp58.com:5379");
			String servers = "ggz.ccp58.com:5379";
			if (servers == null) {
				System.err.println("未读取到redis配置");
				return null;
			}

			String pwd = "hjut6hktEkhk";

			List<JedisShardInfo> jdsInfoList = new ArrayList<JedisShardInfo>();
			String[] serverPorts = servers.split(";");
			for (String serverPort : serverPorts) {
				if (StringUtils.isEmpty(serverPort)) {
					continue;
				}

				String[] spStrs = serverPort.split(":");
				String name = spStrs[0];
				int port = ObjectUtils.getIntValue(spStrs[1]);

				JedisShardInfo info = new JedisShardInfo(name, port);

				if (StringUtils.isNotEmpty(pwd)) {
					info.setPassword(pwd);
				}

				jdsInfoList.add(info);
			}

			pool = new ShardedJedisPool(config, jdsInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
		}
		return pool;
	}
	
    

	public static ShardedJedis getJedis() {
		return getPool().getResource();
	}

	public static String getString(String key) {
		try {
			ShardedJedis redis = getJedis();
			String value = redis.get(key);
			redis.close();
			return value;
		} catch (Exception e) {
			System.err.println("RedisService::getString:" + e);
		}
		return null;
	}

	public static void setString(String key, String value) {
		try {
			ShardedJedis redis = getJedis();
			redis.set(key, value);
			redis.close();
		} catch (Exception e) {
			System.err.println("RedisService::setString:" + e);
		}
	}
	
	public static void deleteString(String key) {
		ShardedJedis redis = getJedis();
		redis.del(key);
		redis.close();
	}

	public static void main(String[] args) {
		setString("admin", "chris");
		System.err.println(getString("admin"));
	}

}
