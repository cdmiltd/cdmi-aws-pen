package pw.cdmi.aws.edu.idcard.service.impl;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.RandomUtil;
import pw.cdmi.aws.edu.common.cache.RedisUtil;
import pw.cdmi.aws.edu.idcard.service.IdCardService;
import pw.cdmi.aws.edu.school.repo.StudentRepository;

@Service
public class RedisRandomService {
	
	@Autowired
	private StudentRepository studentRepo;
	
	@Autowired
	private IdCardService idCardService;
	

	@Autowired
	private RedisUtil redis;

	private static final String SN_REDIS_KEY = "id_card_sn_key";

	private static final String STUDENT_ID_REDIS_KEY = "student_id_sn_key";
	
	
	@Value("${idcard.pdf.local.path}")
	private String pdfPath;

	
	/**
	 * 获取下一个批改版的序列号
	 * 
	 * @return
	 */
	public Long getNextSn() {
		return redis.incr(SN_REDIS_KEY, 1);
	}

	/**
	 * 获取下一个身份码
	 */

	public Long getNextStudentCardId() {
		return redis.incr(STUDENT_ID_REDIS_KEY, RandomUtil.randomInt(1, 10));
	}

	@PostConstruct
	public void init() {
		Long max = studentRepo.selectMaxCardSn();
		if (max == null) {
			max = 10000000l;
		}
		Object v = redis.get(STUDENT_ID_REDIS_KEY);
		if (v == null) {
			redis.set(STUDENT_ID_REDIS_KEY, max);
		} else {
			Long vl = Long.valueOf(v.toString());
			if (vl < max) {
				redis.set(STUDENT_ID_REDIS_KEY, max);
			}
		}

		Long max2 = idCardService.selectMaxSn();
		if (max2 == null) {
			max2 = 200000l;
		}

		Object v2 = redis.get(SN_REDIS_KEY);
		if (v2 == null) {
			redis.set(SN_REDIS_KEY, max2);
		} else {
			long vl = Long.valueOf(v2.toString());
			if (vl < max2) {
				redis.set(SN_REDIS_KEY, max2);
			}
		}

		File path = new File(pdfPath);
		if (!path.exists()) {
			path.mkdirs();
		}

	}

}
