package com.xlf.web;

import com.xlf.constants.ApiResult;
import com.xlf.service.ShortLinkService;
import com.xlf.vo.request.AddShortLinkReq;
import com.xlf.vo.response.AddShortLinkRsp;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class ShortLinkController {
    @Resource
    private ShortLinkService shortLinkService;

    @PostMapping(value = "/shor-link/add")
    @ResponseBody
    public ApiResult<AddShortLinkRsp> addShortLink(@RequestBody @Valid AddShortLinkReq req) {
        return ApiResult.success(shortLinkService.addShortLink(req.getUrl(), req.getExpireDate()));
    }

    @PostMapping(value = "/shor-link/delete")
    @ResponseBody
    public ApiResult<Object> deleteShortLink(@RequestParam("shortLink") String shortLink) {
        shortLinkService.delete(shortLink);
        return ApiResult.success();
    }


    @GetMapping("/{shortLink}")
    public void accessShortLink(@PathVariable("shortLink") String shortLink, HttpServletResponse response) {
        try {
            response.sendRedirect(shortLinkService.findOriginLink(shortLink));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
