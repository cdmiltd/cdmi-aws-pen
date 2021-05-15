package pw.cdmi.aws.edu.obs.test;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import pw.cdmi.aws.edu.idcard.entity.IdCardStudentEntity;

public class IDCardTest {

	public static void main(String[] args) {
		
		List<IdCardStudentEntity> list = new ArrayList<IdCardStudentEntity>();
		for (int i = 1; i <= 64; i++) {
			IdCardStudentEntity e = new IdCardStudentEntity();
			e.setIndexNum(i);
			e.setStudentSn(i);
			e.setStudentId("");
			e.caclRangePoint();
			list.add(e);
		}
		
		System.out.println(JSON.toJSONString(list));
		
		list.forEach(e->{
			String s = "update edu_id_card_student set upleftX=%s,upleftY=%s,lowRightX=%s,lowRightY=%s where indexNum=%s ;";
			System.out.println(String.format(s, e.getUpleftX(),e.getUpleftY(),e.getLowRightX(),e.getLowRightY(),e.getIndexNum()));
		});
	}
	
	
	
}
