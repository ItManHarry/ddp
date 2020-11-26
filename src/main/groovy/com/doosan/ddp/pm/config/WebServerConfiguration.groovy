package com.doosan.ddp.pm.config
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
@Configuration
class WebServerConfiguration extends WebMvcConfigurationSupport {
	/**
	 * 添加系统拦截器	
	 */
	void addInterceptors(InterceptorRegistry registry) {
		
	}
	/**
	 * 添加资源管理员
	 */
	void addResourceHandlers(ResourceHandlerRegistry registry) {
		//将templates目录下的CSS、JS文件映射为静态资源，防止Spring把这些资源识别成thymeleaf模版
		registry.addResourceHandler("/templates/*/**.js").addResourceLocations("classpath:/templates/")
		registry.addResourceHandler("/templates/*/**.css").addResourceLocations("classpath:/templates/")
		//其他的静态资源
		registry.addResourceHandler("/static/*/**").addResourceLocations("classpath:/static/")
		//registry.addResourceHandler("/images/*/**").addResourceLocations("file:"+imagePath)
		super.addResourceHandlers(registry)
	}
	
}