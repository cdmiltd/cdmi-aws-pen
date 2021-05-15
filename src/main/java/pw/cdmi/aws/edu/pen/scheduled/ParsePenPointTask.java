package pw.cdmi.aws.edu.pen.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import pw.cdmi.aws.edu.common.cache.RedisUtil;
import pw.cdmi.aws.edu.common.utils.ListCopy;
import pw.cdmi.aws.edu.pen.modules.PenPoint;
import pw.cdmi.aws.edu.pen.service.PenSerivce;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class ParsePenPointTask {

	private static final Logger log = LoggerFactory.getLogger(ParsePenPointTask.class);
	
	@Autowired
	private RedisUtil redis;
	
	@Autowired
	private PenSerivce penService;
	/**
	 * @Scheduled(fixedRate=3000)：上一次开始执行时间点后3秒再次执行；
		@Scheduled(fixedDelay=3000)：上一次执行完毕时间点3秒再次执行；
		@Scheduled(initialDelay=1000, fixedDelay=3000)：第一次延迟1秒执行，然后在上一次执行完毕时间点3秒再次执行；
		@Scheduled(cron="* * * * * ?")：按cron规则执行；
	 */
    @Scheduled(initialDelay=1000, fixedDelay=2 * 1000)
    public void parsePenPointTasks() {
    	log.info("开始解析笔轨迹数据");
    	Set<String> syncKeys = redis.keys(String.format(PenPoint.redis_prefix, "*"));
    	for (String key : syncKeys) {
    		log.info("本地解析笔点位数据 mac :[{}]",key);
    		List<Object> list = redis.lGet(key, 0, -1);
    		log.info("redis data size =>{}",list.size());
    		List<PenPoint> pointData = ListCopy.copyListProperties(list, PenPoint::new);
    		if(pointData.isEmpty()) {
    			 log.info("mac:[{}] 未找到同步数据",key);
				 continue;
    		}
    		//lpush 添加的 这里需要翻转
	    	Collections.reverse(pointData);
	    	try {
	    		String mac = key.substring(9);
	    		int count = penService.parseSyncPoints(mac, pointData);
	    		
	    		if(count > 0) {
	    			//移除解析成功的值
	    			redis.ltrim(key, 0, (0 - count - 1));
	    		}
	    		
			} catch (Exception e) {
				log.error("mac:[{}] 解析发生异常 msg=>{}",key,e.getMessage());
				e.printStackTrace();
			}
    		
    		
    	
		}
    	
    	
       
    }

}
