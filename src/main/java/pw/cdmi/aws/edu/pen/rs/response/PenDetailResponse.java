package pw.cdmi.aws.edu.pen.rs.response;

import lombok.Data;

@Data
public class PenDetailResponse {
    private String id;   //信息编号
    private String sn;   //序列号
    private String mac;  //Mac地址
    private String name;        //笔的名称
    private Double quantity;    //电量
    private Boolean activated;  //是否已激活
    private Owner owner;        //笔的拥有者
    private EndUser enduser;    //笔的使用者


    @Data
    public static class Owner{
        private String id;
        private String name;
        private String type = "SCHOOL";
    }

    @Data
    public static class EndUser{
        private String id;
        private String name;
        private String phone;
        private String type = "TEACHER";
    }
}
