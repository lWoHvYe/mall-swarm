package com.macro.mall.interceptor;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Objects;
import java.util.Properties;

/**
 * @author Hongyan Wang
 * @description mybatis插件，检查执行的 sql 是否带有 where 关键字，若不存在直接拦截。
 * @date 2021/1/10 19:56
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
@Slf4j
public class CheckWhereInterceptor implements Interceptor {

    private static final String WHERE = "WHERE";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //获取方法的第0个参数，也就是MappedStatement。@Signature注解中的args中的顺序
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        //获取sql命令操作类型
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        final Object[] queryArgs = invocation.getArgs();
        final Object parameter = queryArgs[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        String sql = boundSql.getSql();
        if (Objects.equals(SqlCommandType.DELETE, sqlCommandType)
            || Objects.equals(SqlCommandType.UPDATE, sqlCommandType)
            || Objects.equals(SqlCommandType.SELECT, sqlCommandType)) {
            //格式化sql
            sql = sql.replace("\n", "");
            if (!StringUtils.containsIgnoreCase(sql, WHERE)) {
                sql = sql.replace(" ", "");
                log.warn("SQL 语句没有where条件，禁止执行,sql为:{}", sql);
                throw new RuntimeException("SQL语句中没有where条件");
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        log.info("进入拦截器");
    }
}
