package br.com.bluesburguer.order.bdd;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import br.com.bluesburguer.order.adapters.in.order.dto.OrderDto;
import br.com.bluesburguer.order.adapters.in.order.dto.OrderRequest;
import br.com.bluesburguer.order.adapters.in.order.item.dto.OrderItemRequest;
import br.com.bluesburguer.order.adapters.in.user.dto.UserRequest;
import br.com.bluesburguer.order.core.domain.Cpf;
import br.com.bluesburguer.order.core.domain.Email;
import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

public class StepDefinition {
	
	private static final String CPF = "997.307.080-10";
	private static final String EMAIL = "email.usuario@server.com";
	
	private Response response;
	
	private UUID orderIdCreated;
	private OrderDto orderDto;

    private String ENDPOINT_ORDER = "http://localhost:8000/api/order";
    
    private OrderRequest defineOrderRequest() {
    	var items = List.of(new OrderItemRequest(1L, 1));
		var cpf = new Cpf(CPF);
		var email = new Email(EMAIL);
		var user = new UserRequest(null, cpf, email);
		return new OrderRequest(items, user);
    }
    
	@Quando("criar um novo pedido")
	public void criar_um_novo_pedido() {
		RestAssured.registerParser("Text", Parser.JSON);
		
		var orderRequest = defineOrderRequest();
        response = given()
		                .contentType(MediaType.APPLICATION_JSON_VALUE)
		                .body(orderRequest)
	                .when()
	                	.post(ENDPOINT_ORDER);
	}
	
	@Então("o pedido é criado com sucesso")
	public void o_pedido_é_criado_com_sucesso() {
		response.then()
	        .statusCode(HttpStatus.CREATED.value());
	}

	@Dado("que um pedido foi criado")
	public void que_um_pedido_foi_criado() {
		RestAssured.registerParser("Text", Parser.JSON);
		
		var orderRequest = defineOrderRequest();
        response = given()
		                .contentType(MediaType.APPLICATION_JSON_VALUE)
		                .body(orderRequest)
	                .when()
	                	.post(ENDPOINT_ORDER);
                	
        String orderIdCreatedStr = response.then()
        	.statusCode(HttpStatus.CREATED.value())
        	.extract()
        	.header("Location");
		
		orderIdCreated = UUID.fromString(orderIdCreatedStr);
	}
	
	@Quando("efetuar a busca de todos os pedidos")
	public void efetuar_a_busca_de_todos_os_pedidos() {
		response = given().when().get(ENDPOINT_ORDER);
	}

	@Então("o pedido deve ser apresentado na listagem")
	public void o_pedido_deve_ser_apresentado_na_listagem() {
	    var orderList = response.then()
	    	.statusCode(HttpStatus.OK.value())
	    	.extract()
	    	.body()
	    	.jsonPath().getList(".", OrderDto.class);
	    
	    assertThat(orderList)
	    	.isNotEmpty()
	    	.first()
	    	.hasFieldOrProperty("id")
	    	.hasFieldOrPropertyWithValue("step", OrderStep.ORDER)
	    	.hasFieldOrPropertyWithValue("fase", OrderFase.PENDING)
	    	.hasFieldOrProperty("user.id")
	    	.hasFieldOrProperty("user.cpf")
	    	.hasFieldOrProperty("user.email");
	}
	
	@Quando("efetuar a busca de pedido por id")
	public void efetuar_a_busca_de_pedido_por_id() {
		response = given()
				.when()
				.get(ENDPOINT_ORDER + "/{orderId}", orderIdCreated);
	} 

	@Então("o pedido deve ser apresentado")
	public void o_pedido_deve_ser_apresentado() {
		var orderRequest = defineOrderRequest();
		
	    orderDto = response.then()
	    	.statusCode(HttpStatus.OK.value())
	    	.extract()
	    	.body()
	    	.jsonPath().getObject(".", OrderDto.class);
	    
	    assertThat(orderDto)
	    	.hasFieldOrProperty("id")
	    	.hasFieldOrPropertyWithValue("step", OrderStep.ORDER)
	    	.hasFieldOrPropertyWithValue("fase", OrderFase.PENDING)
	    	.hasFieldOrProperty("user.id")
	    	.hasFieldOrPropertyWithValue("user.cpf", orderRequest.getUser().getCpf().getValue())
	    	.hasFieldOrPropertyWithValue("user.email", orderRequest.getUser().getEmail().getValue());
	}
	
	OrderFase newFase = OrderFase.PENDING;
	
	@Então("o pedido com a nova fase deve ser apresentado")
	public void o_pedido_com_a_nova_fase_deve_ser_apresentado() {
		var orderRequest = defineOrderRequest();
		
	    orderDto = response.then()
	    	.statusCode(HttpStatus.OK.value())
	    	.extract()
	    	.body()
	    	.jsonPath().getObject(".", OrderDto.class);
	    
	    assertThat(orderDto)
	    	.hasFieldOrProperty("id")
	    	.hasFieldOrPropertyWithValue("step", OrderStep.ORDER)
	    	.hasFieldOrPropertyWithValue("fase", newFase)
	    	.hasFieldOrProperty("user.id")
	    	.hasFieldOrPropertyWithValue("user.cpf", orderRequest.getUser().getCpf().getValue())
	    	.hasFieldOrPropertyWithValue("user.email", orderRequest.getUser().getEmail().getValue());
	}

	@Quando("efetuar a busca de pedido por step")
	public void efetuar_a_busca_de_pedido_por_step() {
		response = given().when().get(ENDPOINT_ORDER + "/step/{step}", OrderStep.ORDER);
	}

	@Quando("efetuar a atualização do step e da fase do pedido pelo id")
	public void efetuar_a_atualização_do_step_e_da_fase_do_pedido_pelo_id() {
		RestAssured.registerParser("Text", Parser.JSON);
		
		var orderItemRequest = new OrderItemRequest(1L, 1);
        response = given()
		                .contentType(MediaType.APPLICATION_JSON_VALUE)
		                .body(List.of(orderItemRequest))
	                .when()
	                	.put(ENDPOINT_ORDER + "/{orderId}", orderIdCreated);
	}
	
	@Quando("efetuar a atualização da fase do pedido pelo id")
	public void efetuar_a_atualização_da_fase_do_pedido_pelo_id() {
		RestAssured.registerParser("Text", Parser.JSON);
		
		var orderItemRequest = new OrderItemRequest(1L, 1);
        response = given()
		                .contentType(MediaType.APPLICATION_JSON_VALUE)
		                .body(List.of(orderItemRequest))
	                .when()
	                	.put(ENDPOINT_ORDER + "/{orderId}/{fase}", orderIdCreated, newFase);
	}
	
	@Quando("deletar o pedido pelo id")
	public void deletar_o_pedido_pelo_id() {
		response = given().when()
        	.delete(ENDPOINT_ORDER + "/{orderId}", orderIdCreated);
	}
	
	@Então("o pedido deletado nao deve mais ser encontrado")
	public void o_pedido_deletado_nao_deve_mais_ser_encontrado() {
		response.then()
		    	.statusCode(HttpStatus.NOT_FOUND.value());
	}
}
