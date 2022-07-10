package com.xlf.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.xlf.BaseTest;
import com.xlf.constants.ApiResult;
import com.xlf.service.ShortLinkService;
import com.xlf.util.JsonUtil;
import com.xlf.vo.request.AddShortLinkReq;
import com.xlf.vo.response.AddShortLinkRsp;
import org.junit.Test;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;

public class ShortLinkControllerTest extends BaseTest {
    @Resource
    private ShortLinkService shortLinkService;

    @Test
    public void testAddShortLink() throws Exception {
        addShortLinkRsp();
    }

    private AddShortLinkRsp addShortLinkRsp() throws Exception {
        String url = "/shor-link/add";
        AddShortLinkReq req = new AddShortLinkReq();
        req.setUrl("https://www.bing.com");
        req.setExpireDate(10L);

        String json = postJson(url, JsonUtil.toJsonString(req));

        ApiResult<AddShortLinkRsp> rsp = JsonUtil.fromJson(json, new TypeReference<ApiResult<AddShortLinkRsp>>() {
        });
        Assert.notNull(rsp, "addShortLinkRsp can't be null");
        Assert.notNull(rsp.getData(), "addShortLinkRsp.data can't be null");
        Assert.hasText(rsp.getData().getShortLink(), "addShortLinkRsp.data.shortLink can't be empty");

        return rsp.getData();
    }

    @Test
    public void testDeleteShortLink() throws Exception {
        AddShortLinkRsp addShortLinkRsp = addShortLinkRsp();
        String url = "/shor-link/delete";
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("shortLink", addShortLinkRsp.getShortLink());
        String json = post(url, params);

        ApiResult<Object> rsp = JsonUtil.fromJson(json, new TypeReference<ApiResult<Object>>() {
        });
        Assert.notNull(rsp, "rsp can't be null");
    }

    @Test
    public void testUpdateShortLink() throws Exception {
        AddShortLinkRsp addShortLinkRsp = addShortLinkRsp();
        String url = "/shor-link/update";
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("shortLink", addShortLinkRsp.getShortLink());
        params.add("originLink", "https://www.google.com");
        String json = post(url, params);

        ApiResult<Object> rsp = JsonUtil.fromJson(json, new TypeReference<ApiResult<Object>>() {
        });
        Assert.notNull(rsp, "rsp can't be null");
    }

}
