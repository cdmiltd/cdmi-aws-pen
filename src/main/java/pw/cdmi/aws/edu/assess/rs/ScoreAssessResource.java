package pw.cdmi.aws.edu.assess.rs;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pw.cdmi.aws.edu.assess.rs.responses.ClasshourStatisticResponse;
import pw.cdmi.aws.edu.assess.services.ScoreAssessService;
import pw.cdmi.aws.edu.book.modules.entities.BookClassHourEntity;
import pw.cdmi.aws.edu.book.modules.entities.BookKnowledgeEntity;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.core.exception.HttpClientException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/edu/v1")
public class ScoreAssessResource {

    @Autowired
    private ScoreAssessService scoreAssessService;

    /**
     * 统计一个班级下已批改试卷
     * @param classteamId
     * @return
     */
    @GetMapping("/test/classTeam")
    public List<Map<String, Object>> findTestByClassteamId(@RequestParam("classteamId") String classteamId) {
        if(StringUtils.isBlank(classteamId)) throw new HttpClientException(ErrorMessages.MissRequiredParameter, classteamId);
        return scoreAssessService.findTestByClassteamId(classteamId);
    }

    /**
     * 统计某一张试卷，最高分数、最低分数、平均分数
     * @param classteamId
     * @param id    试卷编号
     * @return
     */
    @GetMapping("/test/classteam/static")
    public List<Map<String, Object>> staticByClassteamIdAndTextBookId(@RequestParam("classteamId") String classteamId, @RequestParam("id") String id){
        List<Map<String, Object>> data = scoreAssessService.staticByClassteamIdAndTextBookId(classteamId, id);
        return data;
    }

    /**
     * 统计某一张试卷，最高分数、最低分数、平均分数
     * @param classteamId
     * @param id    试卷编号
     * @return
     */
    @GetMapping("/test/classteam/rank")
    public List<Map<String, Object>> rankByClassteamIdAndTextBookId(@RequestParam("classteamId") String classteamId, @RequestParam("id") String id){
        List<Map<String, Object>> data = scoreAssessService.rankByClassteamIdAndTextBookId(classteamId, id);
        return data;
    }
}
