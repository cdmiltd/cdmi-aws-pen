package pw.cdmi.aws.edu.common.aop;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;


@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {
	
	private final String TOKEN = "token";
	
	private String error;
	
	public TokenInterceptor() {
		Map<String, Object> map = new HashMap<>();
		map.put("timestamp", System.currentTimeMillis());
		map.put("status", 500);
		map.put("error", "role auth error");
		map.put("message", "权限验证失败");
		error = JSON.toJSONString(map);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
			
			
			
			// 如果类上注解 @Login 表示该类下面所有方法都必须登录
			Auth auth = ((HandlerMethod) handler).getBeanType().getAnnotation(Auth.class);
			if (auth != null) {
				String token = request.getHeader(TOKEN);
				if(!StringUtils.isBlank(token)) {
					
				}
				
				if (auth.login()) {
					
				}
	
			} else {
				// 方法上注解
				Auth LoginAnnotation = ((HandlerMethod) handler).getMethodAnnotation(Auth.class);
				if (LoginAnnotation != null) {
	
					if (LoginAnnotation.login()) {
						
					}
	
				}
			}
			
		}
		return true;

	}

	protected void responseOutWithJson(HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(500);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(error);
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}
