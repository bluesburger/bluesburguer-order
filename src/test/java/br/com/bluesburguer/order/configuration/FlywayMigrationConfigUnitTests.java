package br.com.bluesburguer.order.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;

@ExtendWith(MockitoExtension.class)
class FlywayMigrationConfigUnitTests {
	
	@Mock
	Flyway flyWay;

	@Test
	void shouldInstance() {
		assertThat(new FlywayMigrationConfig()).isNotNull();
	}
	
	@Test
	void givenFlywayConfig_WhenDefineConfiguration_ThenShouldDelayedFlyway() {
		var config = new FlywayMigrationConfig();
		assertThat(config.delayedFlywayInitializer(flyWay))
			.isInstanceOf(FlywayMigrationInitializer.class);
	}
}
