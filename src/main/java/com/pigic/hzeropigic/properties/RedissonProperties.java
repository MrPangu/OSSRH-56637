package com.pigic.hzeropigic.properties;

/**
 * @author guchang.pan@hand-china.com
 *
 */
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "pigic.lock")
public class RedissonProperties {

    private int timeout = 3000;

    private String address;

    private String password;

    private int connectionPoolSize = 64;

    private int connectionMinimumIdleSize=10;

    private int slaveConnectionPoolSize = 250;

    private int masterConnectionPoolSize = 250;

    private String[] sentinelAddresses;

    private String masterName;

    private String mode;

    private int database=0;

    private RedissonProperties.Pool pool;

    private RedissonProperties.Cluster cluster;

    @Data
    public static class Pool {
        private int maxIdle;

        private int minIdle;

        private int maxActive;

        private int maxWait;

        private int connTimeout;

        private int soTimeout;

        /**
         * 池大小
         */
        private  int size;

    }
    @Data
    public static class Cluster {
        /**
         * 集群状态扫描间隔时间，单位是毫秒
         */
        private int scanInterval;

        /**
         * 集群节点
         */
        private String nodes;

        /**
         * 默认值： SLAVE（只在从服务节点里读取）设置读取操作选择节点的模式。 可用值为： SLAVE - 只在从服务节点里读取。
         * MASTER - 只在主服务节点里读取。 MASTER_SLAVE - 在主从服务节点里都可以读取
         */
        private String readMode;
        /**
         * （从节点连接池大小） 默认值：64
         */
        private int slaveConnectionPoolSize;
        /**
         * 主节点连接池大小）默认值：64
         */
        private int masterConnectionPoolSize;

        /**
         * （命令失败重试次数） 默认值：3
         */
        private int retryAttempts;

        /**
         *命令重试发送时间间隔，单位：毫秒 默认值：1500
         */
        private int retryInterval;

        /**
         * 执行失败最大次数默认值：3
         */
        private int failedAttempts;
    }
}
