package br.com.bluesburguer.order.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties.Discovery;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.WebEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
class WebConfigUnitTests {

	@Mock
	WebEndpointsSupplier webEndpointsSupplier;
	
	@Mock
    ServletEndpointsSupplier servletEndpointsSupplier;
    
	@Mock
    ControllerEndpointsSupplier controllerEndpointsSupplier;
    
	@Mock
    EndpointMediaTypes endpointMediaTypes;
    
	@Mock
    CorsEndpointProperties corsProperties;
    
	@Mock
    WebEndpointProperties webEndpointProperties;
    
	@Mock
    Environment environment;
	
	@Mock
	Discovery discovery;
	
	@InjectMocks
	private WebConfig webConfig;
	
	@Test
	void givenDiscoveryEnabledAndTruthyBasePath_WhenRunCustomizedConfiguration_ThenShouldResolveEndpointLinks() {
		
		doReturn(true)
			.when(discovery).isEnabled();
		
		doReturn(discovery)
			.when(webEndpointProperties).getDiscovery();
		
		doReturn("/")
			.when(webEndpointProperties).getBasePath();
		
		var handlerMapping = webConfig.webEndpointServletHandlerMapping(
				webEndpointsSupplier, servletEndpointsSupplier, 
				controllerEndpointsSupplier, endpointMediaTypes, 
				corsProperties, webEndpointProperties, environment);
		
		assertThat(handlerMapping)
			.isNotNull();
	}
	
	@Test
	void givenDiscoveryEnabledAndNullBasePath_WhenRunCustomizedConfiguration_ThenShouldResolveEndpointLinks() {
		
		doReturn(true)
			.when(discovery).isEnabled();
		
		doReturn(discovery)
			.when(webEndpointProperties).getDiscovery();
		
		doReturn(null)
			.when(webEndpointProperties).getBasePath();
		
		var handlerMapping = webConfig.webEndpointServletHandlerMapping(
				webEndpointsSupplier, servletEndpointsSupplier, 
				controllerEndpointsSupplier, endpointMediaTypes, 
				corsProperties, webEndpointProperties, environment);
		
		assertThat(handlerMapping)
			.isNotNull();
	}
	
	@Test
	void givenDiscoveryEnabledAndDifferentManagementPort_WhenRunCustomizedConfiguration_ThenShouldResolveEndpointLinks() {
		
		doReturn(true)
			.when(discovery).isEnabled();
		
		doReturn(discovery)
			.when(webEndpointProperties).getDiscovery();
		
		doReturn(null)
			.when(webEndpointProperties).getBasePath();
		
		try (MockedStatic<ManagementPortType> utilities = Mockito.mockStatic(ManagementPortType.class)) {
			utilities
				.when(() -> ManagementPortType.get(environment))
				.thenReturn(ManagementPortType.DIFFERENT);
			
			var handlerMapping = webConfig.webEndpointServletHandlerMapping(
					webEndpointsSupplier, servletEndpointsSupplier, 
					controllerEndpointsSupplier, endpointMediaTypes, 
					corsProperties, webEndpointProperties, environment);
			
			assertThat(handlerMapping)
				.isNotNull();
		}
	}
	
	@Test
	void givenDiscoveryDisabled_WhenRunCustomizedConfiguration_ThenShouldResolveEndpointLinks() {
		
		doReturn(false)
			.when(discovery).isEnabled();
		
		doReturn(discovery)
			.when(webEndpointProperties).getDiscovery();
		
		var handlerMapping = webConfig.webEndpointServletHandlerMapping(
				webEndpointsSupplier, servletEndpointsSupplier, 
				controllerEndpointsSupplier, endpointMediaTypes, 
				corsProperties, webEndpointProperties, environment);
		
		assertThat(handlerMapping)
			.isNotNull();
	}
}
