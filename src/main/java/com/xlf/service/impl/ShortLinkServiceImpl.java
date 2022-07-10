package com.xlf.service.impl;

import com.xlf.constants.LinkStatus;
import com.xlf.entity.LinkInfoEntity;
import com.xlf.exception.ErrorCodeEnum;
import com.xlf.repository.ShortLinkRepository;
import com.xlf.service.ShortLinkService;
import com.xlf.util.Base62Util;
import com.xlf.util.JsonUtil;
import com.xlf.util.SnowflakeUtil;
import com.xlf.vo.response.AddShortLinkRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class ShortLinkServiceImpl implements ShortLinkService {
    @Resource
    private ShortLinkRepository shortLinkRepository;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String SHORT_LINK_CACHE_PREFIX = "SHORT_LINK_CACHE_PREFIX_";
    /**
     * default expire after 5 day
     */
    @Value("${shortLink.expireDateInterval}")
    private Long defaultExpireDateInterval;
    @Value("${shortLink.shortLinkDomain}")
    private String shortLinkDomain;

    @Override
    public AddShortLinkRsp addShortLink(String url, Long expire) {
        LinkInfoEntity entity = new LinkInfoEntity();
        entity.setId(SnowflakeUtil.nextId());
        entity.setShortLink(Base62Util.encode(entity.getId()));
        entity.setOriginLink(url);
        entity.setExpireTime(getExpireTime(expire));
        entity.setStatus(LinkStatus.ENABLE);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        shortLinkRepository.save(entity);
        String cacheKey = SHORT_LINK_CACHE_PREFIX + entity.getShortLink();
        stringRedisTemplate.opsForValue().set(cacheKey, JsonUtil.toJsonString(entity));
        stringRedisTemplate.expireAt(cacheKey, entity.getExpireTime());
        return new AddShortLinkRsp(entity.getShortLink());
    }

    private Date getExpireTime(Long expire) {
        if (Objects.isNull(expire)) {
            expire = defaultExpireDateInterval;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, expire.intValue());
        return calendar.getTime();
    }

    public String findOriginLink(String shortLink) {
        LinkInfoEntity entity = findLink(shortLink);
        if (Objects.isNull(entity)) {
            throw ErrorCodeEnum.SHORT_LINK_NOT_EXIST.newException();
        }
        if (new Date().after(entity.getExpireTime())) {
            shortLinkRepository.updateStatus(entity.getId(), LinkStatus.DISABLE);
            throw ErrorCodeEnum.SHORT_LINK_NOT_EXIST.newException();
        }
        return entity.getOriginLink();
    }

    @Override
    public void delete(String shortLink) {
        LinkInfoEntity entity = findLink(shortLink);
        if (Objects.isNull(entity)) {
            throw ErrorCodeEnum.SHORT_LINK_NOT_EXIST.newException();
        }

        String cacheKey = SHORT_LINK_CACHE_PREFIX + shortLink;
        stringRedisTemplate.delete(cacheKey);
        shortLinkRepository.updateStatus(entity.getId(), LinkStatus.DISABLE);
    }

    private LinkInfoEntity findLink(String shortLink) {
        String cacheKey = SHORT_LINK_CACHE_PREFIX + shortLink;
        String entity = stringRedisTemplate.opsForValue().get(cacheKey);
        log.info("get LinkInfoEntity from redis , shortLink:{},result:{}", shortLink, entity);
        if (Objects.nonNull(entity)) {
            return JsonUtil.fromJson(entity, LinkInfoEntity.class);
        }
        return null;
    }
}
