package com.xlf;

import com.xlf.exception.ErrorCodeEnum;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.Objects;

@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseTest {
    private MockMvc mockMvc;

    @Resource
    private WebApplicationContext context;

    @Before
    public void initMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    public String post(String url, MultiValueMap<String, String> params) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        if (Objects.nonNull(params) && params.size() > 0) {
            requestBuilder.params(params);
        }

        return executeRequestBuilder(requestBuilder, ErrorCodeEnum.SUCCESS.getCode());
    }

    public String postJson(String url, String json) throws Exception {
        return postJson(url, json, ErrorCodeEnum.SUCCESS.getCode());
    }

    public String postJson(String url, String json, int expectCode) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(json);
        return executeRequestBuilder(requestBuilder, expectCode);
    }

    private String executeRequestBuilder(RequestBuilder requestBuilder, int expectCode) throws Exception {
        return mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(expectCode))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
