package pw.cdmi.aws.edu.school.rs;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.BeforeEach;
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
import pw.cdmi.aws.edu.MainApplition;
import pw.cdmi.aws.edu.school.modules.StudyStage;
import pw.cdmi.aws.edu.school.rs.requests.SchoolRequest;
import pw.cdmi.aws.edu.school.rs.requests.TeacherRequest;
import pw.cdmi.module.people.Sex;

@SpringBootTest(classes = {MainApplition.class})
@AutoConfigureMockMvc
class SchoolResourceTest {

//    @Autowired
//    private MockMvc mockMvc;// 定义一个 MockMvc
//    private String SCHOOL_ID = null;
//
//    @BeforeEach
//    public void setUp() throws Exception {
//
//    }
//
//    /**
//     * 新建一个学校信息
//     * @throws Exception
//     */
//    @Test
//    void postSchool() throws Exception {
//        SchoolRequest request = new SchoolRequest();
//        request.setName("棠湖小学");
//        request.setBrief("天府名校排名11");
//        request.setStages(new String[]{StudyStage.PrimarySchool.name(),StudyStage.JuniorHighSchool.name()});
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders.post("/edu/v1/school")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(JSON.toJSONString(request))
//                        .accept(MediaType.APPLICATION_JSON_VALUE)) // perform 结束
//                .andExpect(MockMvcResultMatchers.status().isOk()) // 添加断言
//                .andDo(MockMvcResultHandlers.print()) // 添加执行
//                .andReturn();// 添加返回
//        SCHOOL_ID = mvcResult.getResponse().getContentAsString();
//    }
//
//    /**
//     * 获取系统内的学校列表
//     * @throws Exception
//     */
//    @Test
//    void listSchool() throws Exception {
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders.get("/edu/v1/school")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON_VALUE)) // perform 结束
//                .andExpect(MockMvcResultMatchers.status().isOk()) // 添加断言
//                .andDo(MockMvcResultHandlers.print()) // 添加执行
//                .andReturn();// 添加返回
//    }
//
//    /**
//     * 新增一个学校老师
//     * @throws Exception
//     */
//    @Test
//    void postTeacher() throws Exception {
//        TeacherRequest teacher = new TeacherRequest();
//        teacher.setName("王静");
//        teacher.setSex(Sex.FEMALE);
//        teacher.setJobName("语文");
//        teacher.setPhoneNumbers(new String[]{"18615703273"});
//        String school_id = ""; //TODO 替换
//        if(SCHOOL_ID != null) {
//            school_id = SCHOOL_ID;
//        }
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders.post("/edu/v1/school/"+school_id+"/teacher")
//                        .content(JSON.toJSONString(teacher))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON_VALUE)) // perform 结束
//                .andExpect(MockMvcResultMatchers.status().isOk()) // 添加断言
//                .andDo(MockMvcResultHandlers.print()) // 添加执行
//                .andReturn();// 添加返回
//    }
//
//    /**
//     * 新增一个班级学生信息
//     * @throws Exception
//     */
//    @Test
//    void postStudent() throws Exception {
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders.get("/")
//                        .param("", "")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON_VALUE)) // perform 结束
//                .andExpect(MockMvcResultMatchers.status().isOk()) // 添加断言
//                .andDo(MockMvcResultHandlers.print()) // 添加执行
//                .andReturn();// 添加返回
//    }
//
//    /**
//     * 获取学校的年级
//     */
//
//    /**
//     * 获取学校指定年级下的班级
//     */
//
//    /**
//     * 获取学校指定班级下的学生
//     */
//    @Test
//    void listStudent() throws Exception {
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders.get("/")
//                        .param("", "")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON_VALUE)) // perform 结束
//                .andExpect(MockMvcResultMatchers.status().isOk()) // 添加断言
//                .andDo(MockMvcResultHandlers.print()) // 添加执行
//                .andReturn();// 添加返回
//    }
//
//    /**
//     * 获取学校的学科以及老师
//     */
//    @Test
//    void listTeacher() throws Exception {
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders.get("/")
//                        .param("", "")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON_VALUE)) // perform 结束
//                .andExpect(MockMvcResultMatchers.status().isOk()) // 添加断言
//                .andDo(MockMvcResultHandlers.print()) // 添加执行
//                .andReturn();// 添加返回
//    }
//
//    /**
//     * 为学校分配学科教材
//     */
//
//    /**
//     * 获取学校的学科以及学科教材
//     */
//    @Test
//    void listSchoolBook() throws Exception {
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders.get("/")
//                        .param("", "")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON_VALUE)) // perform 结束
//                .andExpect(MockMvcResultMatchers.status().isOk()) // 添加断言
//                .andDo(MockMvcResultHandlers.print()) // 添加执行
//                .andReturn();// 添加返回
//    }
//
//    @Test
//    void listStage() throws Exception {
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders.get("/edu/v1/stage")
//                        .param("", "")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON_VALUE)) // perform 结束
//                .andExpect(MockMvcResultMatchers.status().isOk()) // 添加断言
//                .andDo(MockMvcResultHandlers.print()) // 添加执行
//                .andReturn();// 添加返回
//    }
//
//    @Test
//    void listGrade() throws Exception {
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders.get("/edu/v1/grade")
//                        .param("stage", "PrimarySchool")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON_VALUE)) // perform 结束
//                .andExpect(MockMvcResultMatchers.status().isOk()) // 添加断言
//                .andDo(MockMvcResultHandlers.print()) // 添加执行
//                .andReturn();// 添加返回
//    }
}