package com.xlf.service;

import com.xlf.vo.response.AddShortLinkRsp;

public interface ShortLinkService {
    /**
     * add short link
     *
     * @param url
     * @param expire
     */
    AddShortLinkRsp addShortLink(String url, Long expire);

    String findOriginLink(String shortLink);

    void delete(String shortLink);

    void update(String shortLink, String originLink);
}
