package pw.cdmi.aws.edu.pen.service;

import pw.cdmi.aws.edu.common.service.BaseService;
import pw.cdmi.aws.edu.pen.modules.PenPoint;
import pw.cdmi.aws.edu.pen.modules.entities.DrawLineEntities;

import java.util.List;

public interface DrawLineService extends BaseService<DrawLineEntities, String> {

    /**
     * 获取生成轨迹记录
     * @param pageNo
     * @param studentId
     * @return
     */
    public DrawLineEntities getByPageNoAndStudentId(Long pageNo, String studentId);

    /**
     * 查询是否统计坐标点
     * @param state
     * @return
     */
    public List<DrawLineEntities> findListByState(Boolean state);

    /**
     * 查询是否统计坐标点
     * @Param studentId
     * @param state
     * @return
     */
    public List<DrawLineEntities> findListByStudentIdAndState(String studentId, Boolean state);
}
