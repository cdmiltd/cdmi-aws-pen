package pw.cdmi.aws.edu.console.modules.entities;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import pw.cdmi.aws.edu.console.modules.UserRole;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "aws_app_user")
public class AppUserEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "Id", length = 32)
    private String id;                      //用户编号
    private String wxOpenId;                //对应的微信OpenId
    private String username;                //用户名
    @Column(unique = true,length = 32)
    private String phone;                   //用户的电话号码
    private String password;                //用户密码
    private String nickName;                //用户显示名
    private String trueName;                //用户真名
    private String roles;                   //用户所拥有的角色列表
    private Date createTime;                //创建时间
    private Date updateTime;                //更新时间
    private String avatarUrl;               //用户头像


    public List<String> getRoles(){
        if(StringUtils.isBlank(this.roles)){
            return new ArrayList<String>();
        }
        return JSONArray.parseArray(this.roles,String.class);
    }

    public boolean containsRole(UserRole role) {
    	return getRoles().contains(role.name());
    }

    public String getRolesStr(){
        List<String> roles = getRoles();
        StringBuilder rolesStr = new StringBuilder();
        for (String role: roles){
            rolesStr.append(role).append("/");
        }
        String rolesString = rolesStr.substring(0, rolesStr.length()-1);
        return rolesString;
    }
    
    public void removeRole(UserRole userRole){
        List<String> ls_roles = getRoles();
        ls_roles.remove(userRole.name());
        this.roles = JSON.toJSONString(ls_roles);
    }

    public void addRole(UserRole userRole){
        List<String> ls_roles = getRoles();
        if(ls_roles == null){
            ls_roles = new ArrayList<String>();
        }
        if(!ls_roles.contains(userRole.name())) {
        	 ls_roles.add(userRole.name());
             this.roles = JSON.toJSONString(ls_roles);
        }
       
    }
}
