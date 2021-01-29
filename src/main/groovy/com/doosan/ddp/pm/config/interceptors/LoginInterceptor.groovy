package com.doosan.ddp.pm.config.interceptors
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import com.doosan.ddp.pm.comm.results.ServerResultObject
import com.doosan.ddp.pm.comm.utils.JsonUtils
class LoginInterceptor implements HandlerInterceptor {
	/**
	 * 在Controller执行前调用
	 */
	@Override
	boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		System.out.println("系统验证是否登录URL : " + request.getRequestURI())
		HttpSession session = request.getSession()
		Object user = session.getAttribute("currentUser")
		if(user != null){
			System.out.println("用户已登录系统 ...")
			return true
		}else{
			System.out.println("用户未登录系统,跳转值登录页面 ...")
			response.sendRedirect("/pm/web/login")
			return false
		}
	}
	/**
	 * 在Controller执行之后,视图渲染前调用
	 */
	@Override
	void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView mav)
			throws Exception {
		// TODO Auto-generated method stub
		if(mav != null){
			System.out.println("ViewName : " + mav.getViewName() + "------------------" + request.getRequestURI())
		}
	}
	/**
	 * 在Controller执行完成,视图渲染完毕后调用
	 */
	@Override
	void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception e)
			throws Exception {
		System.out.println("Finished the request ......" + request.getRequestURI())
	}
	/**
	 * 拦截后的错误输出
	 * @param response
	 * @param result
	 * @throws Exception
	 */
	void errorOutput(HttpServletResponse response, ServerResultObject<Object> result) throws Exception{
		OutputStream output = null
		try{
			response.setContentType("text/json")
			response.setCharacterEncoding("utf-8")
			output = response.getOutputStream()
			output.write(JsonUtils.objectToJson(result).getBytes("utf-8"))
			output.flush()
		}finally{
			if(output != null)
				output.close()
		}
	}
}