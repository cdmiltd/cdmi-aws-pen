package pw.cdmi.aws.edu.assess.services;

import pw.cdmi.aws.edu.assess.modules.entities.ScoreAssess;
import pw.cdmi.aws.edu.common.service.BaseService;

import java.util.List;
import java.util.Map;

public interface ScoreAssessService extends BaseService<ScoreAssess, String> {
    /**
     * 获取学生试卷分数信息
     * @param textBookId
     * @param studentId
     */
    ScoreAssess getByTextBookIdAndStudentId(String textBookId, String studentId);

    /**
     * 查询当前班级的所有批改试卷
     * @param classteamId
     * @return
     */
    List<Map<String, Object>> findTestByClassteamId(String classteamId);

    /**
     * 统计某班级某一张试卷的最高分数、最低分数、平均分数
     * @param classteamId
     * @param textBookId
     * @return
     */
    List<Map<String, Object>> staticByClassteamIdAndTextBookId(String classteamId, String textBookId);

    /**
     * 对某班级某一张试卷，考试成绩排行
     * @param classteamId
     * @param textBookId
     * @return
     */
    List<Map<String, Object>> rankByClassteamIdAndTextBookId(String classteamId, String textBookId);

}
