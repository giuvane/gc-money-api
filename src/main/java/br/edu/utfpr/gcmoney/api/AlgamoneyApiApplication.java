package br.edu.utfpr.gcmoney.api;

import java.util.Calendar;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

import br.edu.utfpr.gcmoney.api.config.property.AlgamoneyApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(AlgamoneyApiProperty.class)
public class AlgamoneyApiApplication {
	
	private static ApplicationContext APPLICATION_CONTEXT;

	public static void main(String[] args) {
		APPLICATION_CONTEXT = SpringApplication.run(AlgamoneyApiApplication.class, args);
		Calendar hoje = Calendar.getInstance();
		System.out.println("Hora do servidor: " + hoje.getTime());
	}
	
	public static <T> T getBean(Class<T> type) {
		return APPLICATION_CONTEXT.getBean(type);
	}
	
	/*
	@PostConstruct
	 void started() {
	 TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	 } 
	*/
}
