package com.devteam.digital.common.aspect;

import com.devteam.digital.common.annotation.Limit;
import com.devteam.digital.common.exception.BadRequestException;
import com.devteam.digital.common.util.RequestHolder;
import com.devteam.digital.common.util.StringUtil;
import com.google.common.collect.ImmutableList;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


@Aspect
@Component
public class LimitAspect {

    private final RedisTemplate<Object,Object> redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(LimitAspect.class);

    public LimitAspect(RedisTemplate<Object,Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Pointcut("@annotation(com.devteam.digital.common.annotation.Limit)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        Limit limit = signatureMethod.getAnnotation(Limit.class);
        LimitType limitType = limit.limitType();
        String key = limit.key();
        if (StringUtil.isEmpty(key)) {
            if (limitType == LimitType.IP) {
                key = StringUtil.getIp(request);
            } else {
                key = signatureMethod.getName();
            }
        }

        ImmutableList<Object> keys = ImmutableList.of(StringUtil.join(limit.prefix(), "_", key, "_", request.getRequestURI().replaceAll("/","_")));

        String luaScript = buildLuaScript();
        RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
        Number count = redisTemplate.execute(redisScript, keys, limit.count(), limit.period());
        if (null != count && count.intValue() <= limit.count()) {
            logger.info("The {}th access key is {}, and the description is the interface of [{}] ", count, keys, limit.name());
            return joinPoint.proceed();
        } else {
            throw new BadRequestException("Limited access ");
        }
    }

    private String buildLuaScript() {
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }
}

