package br.com.bluesburguer.order.support;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import br.com.bluesburguer.order.BluesBurguerOrderApplication;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(
		classes = { BluesBurguerOrderApplication.class },
		properties = { 
				"spring.main.allow-bean-definition-overriding=true",
				"spring.cloud.bus.enabled=false",
				"spring.cloud.consul.enabled=false", 
				"spring.cloud.consul.discovery.enabled=false"
		},
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles({ "test" })
@ContextConfiguration(classes = BluesBurguerOrderApplication.class)
public class ApplicationIntegrationSupport {

}
