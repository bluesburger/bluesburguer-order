package br.com.bluesburguer.order.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

@Configuration
public class SQSConfig {

	@Bean
	AmazonSQS amazonSqs() {
		return AmazonSQSClientBuilder.defaultClient();
	}
}
