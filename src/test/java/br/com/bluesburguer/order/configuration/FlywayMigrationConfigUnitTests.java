package br.com.bluesburguer.order.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;

@ExtendWith(MockitoExtension.class)
class FlywayMigrationConfigUnitTests {
	
	@Mock
	Flyway flyWay;
	
	@InjectMocks
	FlywayMigrationConfig config;
	
	@Test
	void givenFlywayConfig_WhenDefineConfiguration_ThenShouldDelayedFlyway() {
		assertThat(config.delayedFlywayInitializer(flyWay))
			.isInstanceOf(FlywayMigrationInitializer.class);
	}
}
