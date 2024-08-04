package br.com.bluesburguer.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/** 
 * 
*/
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = { "br.com.bluesburguer.order" })
@EnableSqs
public class BluesBurguerOrderApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(BluesBurguerOrderApplication.class, args);
	}
}
