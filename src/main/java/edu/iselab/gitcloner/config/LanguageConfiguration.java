package edu.iselab.gitcloner.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * This class is a Spring's configuration class. It is responsible for managing
 * the language of the system
 * 
 * @author Thiago Ferreira
 * @version 0.0.1
 * @since 2020-12-09
 */
@Configuration
public class LanguageConfiguration implements WebMvcConfigurer {

    @Bean
    public MessageSource messageSource() {
        
        ReloadableResourceBundleMessageSource  source = new ReloadableResourceBundleMessageSource ();
        
        source.setBasename("classpath:i18n/messages");
        source.setDefaultEncoding("UTF-8");
        
        return source;
    }

    @Override
    public Validator getValidator() {
       
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        
        validator.setValidationMessageSource(messageSource());
        
        return validator;
    }

    @Bean
    public LocaleResolver localeResolver() {

        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        
        localeResolver.setCookieName("localeInfo");
        localeResolver.setCookieMaxAge(24 * 60 * 60);
        localeResolver.setDefaultLocale(Locale.US);
        
        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        
        localeChangeInterceptor.setParamName("lang");
        
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        
        registry.addInterceptor(localeChangeInterceptor());
    }
}
