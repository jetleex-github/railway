package com.eaosoft.JWT.config;

import com.eaosoft.JWT.handler.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * Token lanjieqi
 * @Author ZhouWenTao
 * @Date 2022/7/14 14:23
 * @Version 1.0
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**");    // Intercept all requests and decide if you need to sign in by determining if there is a @LoginRequired annotation
    }
    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> arg0) {

    }
    @Override
    public void addCorsMappings(CorsRegistry arg0) {
    }
    @Override
    public void addFormatters(FormatterRegistry arg0) {

    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry arg0) {

    }
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> arg0) {

    }
    @Override
    public void addViewControllers(ViewControllerRegistry arg0) {

    }
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer arg0) {

    }
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer arg0) {

    }
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer arg0) {

    }
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> arg0) {

    }
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> arg0) {

    }
    @Override
    public void configurePathMatch(PathMatchConfigurer arg0) {

    }
    @Override
    public void configureViewResolvers(ViewResolverRegistry arg0) {

    }
    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> arg0) {

    }
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> arg0) {

    }
    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        return null;
    }
    @Override
    public Validator getValidator() {
        return null;
    }

}

