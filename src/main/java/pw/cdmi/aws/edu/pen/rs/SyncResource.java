package pw.cdmi.aws.edu.pen.rs;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import pw.cdmi.aws.edu.common.cache.RedisUtil;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.pen.modules.PenPoint;
import pw.cdmi.aws.edu.pen.rs.request.PenTrackRequest;
import pw.cdmi.core.exception.HttpClientException;

@RestController
@RequestMapping("/edu/v1/pen/track")
public class SyncResource {
	
	
	 private static final Logger log = LoggerFactory.getLogger(SyncResource.class);
	@Autowired
	private RedisUtil redis;
	
	/**
	 * 实时笔迹同步
	 * @param userId
	 * @param req
	 */
	@PostMapping("/online")
	public void online(@RequestBody PenTrackRequest req) {
		if(StringUtils.isBlank(req.getMac()) || req.getPoints() == null || req.getPoints().length == 0)
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
//		log.info("在线同步数据 mac =>[{}], points=>[{}]",req.getMac(),JSON.toJSONString(req.getPoints()));
		
		redis.lpushAll(String.format(PenPoint.redis_prefix, req.getMac()), req.getPoints());
		
	}
	
	/**
	 * 离线数据同步
	 * @param userId
	 * @param req
	 */
	
	@PostMapping("/offline")
	public void offline(@RequestBody PenTrackRequest req) {
		if(StringUtils.isBlank(req.getMac()) || req.getPoints() == null || req.getPoints().length == 0)
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
//		log.info("离线同步数据 mac =>[{}], points=>[{}]",req.getMac(),JSON.toJSONString(req.getPoints()));
		
		redis.lpushAll(String.format(PenPoint.redis_prefix, req.getMac()), req.getPoints());
		
	}
	
	
	
	
	
}
