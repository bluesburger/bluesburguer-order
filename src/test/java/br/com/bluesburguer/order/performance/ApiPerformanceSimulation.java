package br.com.bluesburguer.order.performance;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
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
				        "cpf": "807.542.590-13",
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
			.put("/api/order/12642ba8-66cf-4dd5-8e97-71bd852efcf7/CONFIRMED");
	
	ActionBuilder updateOrderStepAndFaseRequest = http("atualizar passo e fase do pedido")
			.put("/api/order/556f2b18-bda4-4d05-934f-7c0063d78f48/KITCHEN/CONFIRMED");
	
	ScenarioBuilder cenarioAddOrder = scenario("adicionar pedido")
				.exec(addOrderRequest);
	
	ScenarioBuilder updateOrderFase = scenario("atualizar fase do pedido")
			.exec(updateOrderFaseRequest);
	
	ScenarioBuilder updateOrderStepAndFase = scenario("atualizar passo e fase do pedido")
			.exec(updateOrderStepAndFaseRequest);
	
	int rampUsersPerSec = 1, rampUsersTo = 10, rampUsersDuringSeconds = 10;
	int constantUsersPerSec = 10, constantUsersDuringSeconds = 60;
	
	{
        setUp(
        		cenarioAddOrder.injectOpen(
        				rampUsersPerSec(rampUsersPerSec).to(rampUsersTo).during(Duration.ofSeconds(rampUsersDuringSeconds)),
        				constantUsersPerSec(constantUsersPerSec).during(Duration.ofSeconds(constantUsersDuringSeconds))
        		)
        		,
        		updateOrderFase.injectOpen(
                        rampUsersPerSec(rampUsersPerSec).to(rampUsersTo).during(Duration.ofSeconds(rampUsersDuringSeconds)),
                        constantUsersPerSec(constantUsersPerSec).during(Duration.ofSeconds(constantUsersDuringSeconds))
                )
        		,
        		updateOrderStepAndFase.injectOpen(
        				rampUsersPerSec(rampUsersPerSec).to(rampUsersTo).during(Duration.ofSeconds(rampUsersDuringSeconds)),
                        constantUsersPerSec(constantUsersPerSec).during(Duration.ofSeconds(constantUsersDuringSeconds))
                )
        )
                .protocols(httpProtocol)
                .assertions(
                		// l000 milisegundos é aceitável
//                        global().responseTime().max().lt(1500),
                        global().failedRequests().count().is(0L));
    }
}
