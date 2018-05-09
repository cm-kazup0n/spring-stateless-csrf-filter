package jp.classmethod.spring_stateless_csrf_filter.e2e;

import jp.classmethod.spring_stateless_csrf_filter.e2e.app.Application;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class IntegrateTest {

    @Autowired
    private MockMvc mvc;

    @Ignore("POSTパラメータへのトークンの埋め込みがまだできない")
    @Test
    public void exampleTest() throws Exception {
        final MvcResult result = mvc.perform(get("/secured"))
                .andExpect(status().isOk())
                .andExpect(content().string("GET secured/index"))
        .andExpect(cookie().exists("session")).andReturn();



        mvc.perform(post("/secured").cookie(result.getResponse().getCookies()))
                .andExpect(status().isOk())
                .andExpect(content().string("GET secured/index"));


    }





}
