package br.com.bluesburguer.orderingsystem.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import br.com.bluesburguer.orderingsystem.order.domain.service.OrderService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/** 
 * 
*/
@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
public class BluesBurguerOrderApplication {
	
	@Autowired
	private OrderService orderService;
	
	public static void main(String[] args) {
		SpringApplication.run(BluesBurguerOrderApplication.class, args);
	}

	@PostConstruct
	void init() throws InterruptedException {
		/*
		OrderItem item1 = new OrderItem();
		item1.setCurrentValue(104.50D);

		Order order = new Order(Fase.PENDING);
		order.add(item1);
		var savedOrder = orderService.save(order);
		TimeUnit.SECONDS.sleep(5L);
		orderService.updateFase(savedOrder.getId(), Fase.IN_PROGRESS);
		log.info("Updated order {} to {}", savedOrder.getId(), Fase.IN_PROGRESS);
		
		TimeUnit.SECONDS.sleep(5L);
		orderService.updateFase(savedOrder.getId(), Fase.DONE);
		log.info("Updated order {} to {}", savedOrder.getId(), Fase.DONE);
		*/
	}
}
