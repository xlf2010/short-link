package com.xlf.vo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddShortLinkReq {
    @NotBlank(message = "url can't be blank")
    private String url;
    private Long expireDate;
}
