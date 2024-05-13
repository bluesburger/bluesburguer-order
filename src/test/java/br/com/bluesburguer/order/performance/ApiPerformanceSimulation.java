package br.com.bluesburguer.order.performance;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import java.time.Duration;

import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class ApiPerformanceSimulation extends Simulation {

	private final HttpProtocolBuilder httpProtocol = 
			http.baseUrl("http://localhost:8000")
				.header("Content-Type", "application/json");
	
	ActionBuilder addOrderRequest = http("adicionar pedido")
			.post("/api/order")
			.body(StringBody("""
				{
				    "user": {
				        "cpf": "321.647.728-22",
				        "email": "email.usuario@server.com"
				    },
				    "items": [
				        {
				            "id": 1,
				            "quantity": 1
				        }
				    ]
				}
			"""))
			.check(status().is(201));
	
	ActionBuilder updateOrderFaseRequest = http("atualizar fase do pedido")
			.put("/api/order/1/IN_PROGRESS");
	
	ActionBuilder updateOrderStepAndFaseRequest = http("atualizar passo e fase do pedido")
			.put("/api/order/2/KITCHEN/IN_PROGRESS");
	
	ScenarioBuilder cenarioAddOrder = scenario("adicionar pedido")
				.exec(addOrderRequest);
	
	ScenarioBuilder updateOrderFase = scenario("atualizar fase do pedido")
			.exec(updateOrderFaseRequest);
	
	ScenarioBuilder updateOrderStepAndFase = scenario("atualizar passo e fase do pedido")
			.exec(updateOrderStepAndFaseRequest);
	
	{
        setUp(
        		cenarioAddOrder.injectOpen(
                        rampUsersPerSec(1)
                                .to(10)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(60)),
                        rampUsersPerSec(10)
                                .to(1)
                                .during(Duration.ofSeconds(10))),
        		updateOrderFase.injectOpen(
                        rampUsersPerSec(1)
                        .to(10)
                        .during(Duration.ofSeconds(10)),
                constantUsersPerSec(10)
                        .during(Duration.ofSeconds(60)),
                rampUsersPerSec(10)
                        .to(1)
                        .during(Duration.ofSeconds(10))),
        		updateOrderStepAndFase.injectOpen(
                        rampUsersPerSec(1)
                        .to(10)
                        .during(Duration.ofSeconds(10)),
                constantUsersPerSec(10)
                        .during(Duration.ofSeconds(60)),
                rampUsersPerSec(10)
                        .to(1)
                        .during(Duration.ofSeconds(10)))
        )
                .protocols(httpProtocol)
                .assertions(
                		// 300 milisegundos é aceitável
                        global().responseTime().max().lt(300),
                        global().failedRequests().count().is(0L));
    }
}
