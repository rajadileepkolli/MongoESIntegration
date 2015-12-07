package com.digitalbridge.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
public class XMLConfig extends WebMvcConfigurationSupport 
{

    /*
     * Configure to plugin JSON and XML as request and response in method
     * handler
     */
    @Bean
    @Override
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter()
    {
        RequestMappingHandlerAdapter mappingHandlerAdapter = super.requestMappingHandlerAdapter();
        mappingHandlerAdapter.getMessageConverters().addAll(
                Arrays.asList(jsonMessageConverter(), xmlMessageConverter()));
        return mappingHandlerAdapter;
    }

    /* Configure bean to convert JSON to POJO and vice versa */
    @Bean
    public MappingJackson2HttpMessageConverter jsonMessageConverter()
    {
        return new MappingJackson2HttpMessageConverter();
    }

    /* Configure bean to convert XML to POJO and vice versa */
    @Bean
    public Jaxb2RootElementHttpMessageConverter xmlMessageConverter()
    {
        return new Jaxb2RootElementHttpMessageConverter();
    }

}