package com.macro.mall.search.config;

import com.baomidou.dynamic.datasource.plugin.MasterSlaveAutoRoutingPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Hongyan Wang
 * @className DataSourceConfig
 * @description#自动读写分离
 * 场景：
 * 在纯的读写分离环境，写操作全部是master，读操作全部是slave。
 * 不想通过注解配置完成以上功能。
 * 答：在mybatis环境下可以基于mybatis插件结合本数据源完成以上功能。
 * 手动注入插件。
 * @Bean
 * public MasterSlaveAutoRoutingPlugin masterSlaveAutoRoutingPlugin(){
 *     return new MasterSlaveAutoRoutingPlugin();
 * }
 * 默认主库名称master,从库名称slave。
 * @date 2020/12/30 23:17
 */
@Component
public class RWAutoRouteConfig {
    @Bean
    public MasterSlaveAutoRoutingPlugin masterSlaveAutoRoutingPlugin(){
        return new MasterSlaveAutoRoutingPlugin();
    }
}
