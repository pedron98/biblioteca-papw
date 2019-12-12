package br.com.pedro.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.pedro.serialization.converter.YamlJackson2HttpMessageConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
private static final MediaType MEDIA_TYPE_YML = MediaType.valueOf("application/x-yaml");
	
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new YamlJackson2HttpMessageConverter());
	}
	
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
	}
	
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		//Via EXTENSION, localhost:8080/person/V1.xml
		/*
		 * configurer.favorParameter(false)
				  .ignoreAcceptHeader(false)
				  .defaultContentType(MediaType.APPLICATION_JSON)
				  .mediaType("json", MediaType.APPLICATION_JSON)
				  .mediaType("xml", MediaType.APPLICATION_XML);
				  .mediaType("x-yaml", MEDIA_TYPE_YML);*/
		
		//Via PATH PARAM, localhost:8080/person/V1?mediaType=xml
		/*
		 * configurer.favorPathExtension(false)
				  .favorParameter(true)
				  .parameterName("mediaType")
				  .ignoreAcceptHeader(true)
				  .useRegisteredExtensionsOnly(false)
				  .defaultContentType(MediaType.APPLICATION_JSON)
				  .mediaType("json", MediaType.APPLICATION_JSON)
				  .mediaType("xml", MediaType.APPLICATION_XML);
				  .mediaType("x-yaml", MEDIA_TYPE_YML);*/
		
		//VIA HEADER PARAM, Key = Accept, Value = application/xml  
		configurer.favorPathExtension(false)
				  .favorParameter(false)
				  .ignoreAcceptHeader(false)
				  .useRegisteredExtensionsOnly(false)
				  .defaultContentType(MediaType.APPLICATION_JSON)
				  .mediaType("json", MediaType.APPLICATION_JSON)
				  .mediaType("xml", MediaType.APPLICATION_XML)
				  .mediaType("x-yaml", MEDIA_TYPE_YML);		
	}
	
}
