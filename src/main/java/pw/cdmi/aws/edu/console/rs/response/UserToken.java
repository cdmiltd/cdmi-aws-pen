package pw.cdmi.aws.edu.console.rs.response;

import lombok.Data;
import pw.cdmi.aws.edu.console.modules.UserRole;

import java.util.List;
import java.util.Map;

@Data
public class UserToken {
    private String token;       //token信息
    private String name;        //显示名称
    private String phone;       //手机号
    private String userId;      //系统用户ID
    private String avatarUrl;    //用户头像
    private String role;        //用户角色
    private List<String> roles;   //用户角色
    private UserRole currentRole;   //当前角色
    private Map<String,String> extension;
}
