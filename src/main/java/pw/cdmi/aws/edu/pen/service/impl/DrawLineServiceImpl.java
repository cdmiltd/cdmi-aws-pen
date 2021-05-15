package pw.cdmi.aws.edu.pen.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.pen.modules.entities.DrawLineEntities;
import pw.cdmi.aws.edu.pen.repo.DrawLineRepository;
import pw.cdmi.aws.edu.pen.service.DrawLineService;

import java.util.List;

@Service
public class DrawLineServiceImpl extends BaseServiceImpl<DrawLineEntities, String> implements DrawLineService {

    @Autowired
    private DrawLineRepository drawLineRepository;

    @Override
    public DrawLineEntities getByPageNoAndStudentId(Long pageNo, String studentId) {
        return drawLineRepository.getByPageNoAndStudentId(pageNo, studentId);
    }

    @Override
    public List<DrawLineEntities> findListByState(Boolean state) {
        return drawLineRepository.findByState(state);
    }

    @Override
    public List<DrawLineEntities> findListByStudentIdAndState(String studentId, Boolean state) {
        return drawLineRepository.findByStudentIdAndState(studentId, state);
    }
}
