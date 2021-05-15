package pw.cdmi.aws.edu.region.rs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class RegionResourceTest {

//    @Autowired
//    private MockMvc mockMvc;// 定义一个 MockMvc
//
//
//    @Test
//    void listCity() throws Exception {
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders.get("/edu/v1/region/cities")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON_VALUE)) // perform 结束
//                .andExpect(MockMvcResultMatchers.status().isOk()) // 添加断言
//                .andDo(MockMvcResultHandlers.print()) // 添加执行
//                .andReturn();// 添加返回
//    }
//
//    @Test
//    void listDistrict() throws Exception {
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders.get("/edu/v1/region/districts")
//                        .param("city", "Chengdu")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON_VALUE)) // perform 结束
//                .andExpect(MockMvcResultMatchers.status().isOk()) // 添加断言
//                .andDo(MockMvcResultHandlers.print()) // 添加执行
//                .andReturn();// 添加返回
//    }
}