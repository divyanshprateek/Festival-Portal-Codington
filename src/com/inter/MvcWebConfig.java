package com.inter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.ResourceBundleThemeSource;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.theme.CookieThemeResolver;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan("com.accenture.adf.businesstier.controller")
public class MvcWebConfig implements WebMvcConfigurer {

   @Bean(name="messageSource")
   public MessageSource messageSource() {
      ReloadableResourceBundleMessageSource messageSource=new ReloadableResourceBundleMessageSource();
      messageSource.setBasename("classpath:/messages");
      messageSource.setDefaultEncoding("UTF-8");
      messageSource.setUseCodeAsDefaultMessage(true);
      return messageSource;
   }

   @Bean
   public LocaleResolver localeResolver() {
      CookieLocaleResolver localeResolver = new CookieLocaleResolver();
      return localeResolver;
   }

   @Override
	public void addInterceptors(InterceptorRegistry registry) {
		ThemeChangeInterceptor themeChangeInterceptor = new ThemeChangeInterceptor();
		themeChangeInterceptor.setParamName("theme");
		registry.addInterceptor(themeChangeInterceptor);
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		registry.addInterceptor(localeChangeInterceptor);
   }


@Override
public void addArgumentResolvers(List<HandlerMethodArgumentResolver> arg0) {
	// TODO Auto-generated method stub
	
}


@Override
public void addFormatters(FormatterRegistry arg0) {
	// TODO Auto-generated method stub
	
}


@Override
public void addResourceHandlers(ResourceHandlerRegistry arg0) {
	// TODO Auto-generated method stub
	
}


@Override
public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> arg0) {
	// TODO Auto-generated method stub
	
}


@Override
public void addViewControllers(ViewControllerRegistry arg0) {
	// TODO Auto-generated method stub
	
}


@Override
public void configureAsyncSupport(AsyncSupportConfigurer arg0) {
	// TODO Auto-generated method stub
	
}


@Override
public void configureContentNegotiation(ContentNegotiationConfigurer arg0) {
	// TODO Auto-generated method stub
	
}


@Override
public void configureDefaultServletHandling(DefaultServletHandlerConfigurer arg0) {
	// TODO Auto-generated method stub
	
}


@Override
public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> arg0) {
	// TODO Auto-generated method stub
	
}


@Override
public void configureMessageConverters(List<HttpMessageConverter<?>> arg0) {
	// TODO Auto-generated method stub
	
}


@Override
public void configurePathMatch(PathMatchConfigurer arg0) {
	// TODO Auto-generated method stub
	
}


@Override
public MessageCodesResolver getMessageCodesResolver() {
	// TODO Auto-generated method stub
	return null;
}


@Override
public Validator getValidator() {
	// TODO Auto-generated method stub
	return null;
}

}