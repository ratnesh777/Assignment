package com.walmart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger1.annotations.EnableSwagger;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableSwagger
public class SwaggerConfiguration{ 
                                  
	  @Bean
	    public Docket swagger2Api() {
	        return new Docket(DocumentationType.SWAGGER_2)
	                .apiInfo(apiInfo())
	                .select()
	                //.apis(RequestHandlerSelectors.any())
	                .apis(RequestHandlerSelectors.basePackage("com.walmart.controller")) 
	                .paths(PathSelectors.any())
	                .build();
	    }
	    
	  /*  @Bean
	    public Docket swagger1Api() {
	        return new Docket(DocumentationType.SWAGGER_12)
	              //  .groupName("v12")
	                .apiInfo(apiInfo())
	                .select()
	                .apis(RequestHandlerSelectors.any())
	                //.apis(RequestHandlerSelectors.basePackage("com.walmart")) 
	                .paths(PathSelectors.any())
	                .build();
	    }
	    */

	    private ApiInfo apiInfo() {
	        return new ApiInfoBuilder()
	                .title("Spring REST Sample with Swagger")
	                .description("Spring REST Sample with Swagger API")
	                .termsOfServiceUrl("http://www-03.ibm.com/software/sla/sladb.nsf/sla/bm?Open")
	                .contact("Ratnesh Kumar Srivastava")
	                .license("Apache License Version 2.0")
	                .licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE")
	                .version("2.0")
	                .build();
	    }
	
}