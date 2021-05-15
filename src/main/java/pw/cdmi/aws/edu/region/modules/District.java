package pw.cdmi.aws.edu.region.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public enum District {
    QingYang(City.Chengdu,"0101","青羊区"),
    JingJiang(City.Chengdu,"0102","锦江区"),
    JingNiu(City.Chengdu,"0103","金牛区"),
    WuHou(City.Chengdu,"0104","武侯区"),
    ChengHua(City.Chengdu,"0105","成华区"),
    LongQuanYi(City.Chengdu,"0106","龙泉驿区"),
    QingBaiJiang(City.Chengdu,"0107","清白江区"),
    XinDou(City.Chengdu,"0108","新都区"),
    WenJiang(City.Chengdu,"0109","温江区"),
    GaoXing(City.Chengdu,"0110","高新区"),
    GaoXingXi(City.Chengdu,"0111","高新西区"),
    ShuangLiu(City.Chengdu,"0112","双流区"),
    DuJiangYan(City.Chengdu,"0113","都江堰市"),
    PengZhou(City.Chengdu,"0114","彭州市"),
    JingTang(City.Chengdu,"0115","金堂县"),
    PiDou(City.Chengdu,"0116","郫都区"),
    DaYi(City.Chengdu,"0117","大邑县"),
    PuJiang(City.Chengdu,"0118","浦江县"),
    XinJin(City.Chengdu,"0119","新津县"),
    JianYang(City.Chengdu,"0120","简阳市"),
    HeChuan(City.Chongqing,"0201","合川区"),
    JiangJin(City.Chongqing,"0202","江津区"),
    NanChuan(City.Chongqing,"0203","南川区"),
    YongChuan(City.Chongqing,"0204","永川区"),
    NanAn(City.Chongqing,"0205","南岸区"),
    YuBei(City.Chongqing,"0206","渝北区"),
    DaDuKou(City.Chongqing,"0207","大渡口区"),
    WanZhou(City.Chongqing,"0208","万州区"),
    BeiBei(City.Chongqing,"0209","北碚区"),
    ShaPingBa(City.Chongqing,"0210","沙坪坝区"),
    BaNan(City.Chongqing,"0211","巴南区"),
    FuLin(City.Chongqing,"0212","涪陵区"),
    JiangBei(City.Chongqing,"0213","江北区"),
    JiuLongPu(City.Chongqing,"0214","九龙坡区"),
    YuZhong(City.Chongqing,"0215","渝中区"),
    QianJiang(City.Chongqing,"0216","黔江区"),
    ChangShou(City.Chongqing,"0217","长寿区"),
    QiJiang(City.Chongqing,"0218","綦江区"),
    TongNan(City.Chongqing,"0219","潼南区"),
    DaZu(City.Chongqing,"0220","大足区"),
    RongChang(City.Chongqing,"0221","荣昌区"),
    BiShan(City.Chongqing,"0222","璧山区"),
    WuLong(City.Chongqing,"0223","武隆区"),
    LiangPing(City.Chongqing,"0224","梁平区"),
    KaiZhou(City.Chongqing,"0225","开州区"),
    ;
    private City city;
    private String code;
    private String title;
    District(City city,String code, String title){
        this.city = city;
        this.code = code;
        this.title = title;
    }
    
    private static Map<String,  List<District>> map = new HashMap<>();
    private static Map<String,  District> data = new HashMap<>();
    static {
    	for(District d:District.values()) {
			data.put(d.code, d);
		}
    	
    	
    	for(City c:City.values()) {
    		List<District> list = new ArrayList<>();
    		for(District d:District.values()) {
    			if(d.city == c) {
    				list.add(d);
    			}
    		}
    		map.put(c.getCode(), list);
    	}
    }
    
    public static List<District> forCityCode(String code){
    	return map.get(code);
    }
    
    public static District valuesOfCode(String code){
    	return data.get(code);
    }

    public String getCode(){
        return this.code;
    }
    public String getTitle(){
        return this.title;
    }
    public City getCity(){
        return this.city;
    }
}
