package br.com.bluesburguer.order.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class H2ConfigUnitTests {

	@InjectMocks
	private H2Config h2Config;

	@Test
	void givenH2Config_WhenH2WebServer_ThenShouldProvideBean() throws SQLException {
		try (MockedStatic<Server> utilities = Mockito.mockStatic(Server.class)) {
			utilities
				.when(() -> Server.createWebServer(any()))
				.thenReturn(mock(Server.class));

			assertThat(h2Config.h2WebServer()).isNotNull();
		}
	}
	
	@Test
	void givenH2Config_WhenH2TcpServer_ThenShouldProvideBean() throws SQLException {
		try (MockedStatic<Server> utilities = Mockito.mockStatic(Server.class)) {
			utilities
				.when(() -> Server.createTcpServer(any()))
				.thenReturn(mock(Server.class));

			assertThat(h2Config.tcpServer()).isNotNull();
		}
	}
	
	@Test
	void givenH2Config_WhenH2PgServer_ThenShouldProvideBean() throws SQLException {
		try (MockedStatic<Server> utilities = Mockito.mockStatic(Server.class)) {
			utilities
				.when(() -> Server.createPgServer(any()))
				.thenReturn(mock(Server.class));

			assertThat(h2Config.h2PgServer()).isNotNull();
		}
	}
}
