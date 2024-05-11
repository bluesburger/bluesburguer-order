package br.com.bluesburguer.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;

@ExtendWith(MockitoExtension.class)
class BluesBurguerOrderUnitTests {

	@Test
	void givenMainClass_WhenMainMethodIsCalled_ShouldRunSpringApplication() {
		try (MockedStatic<SpringApplication> utilities = Mockito.mockStatic(SpringApplication.class)) {
			BluesBurguerOrderApplication.main(new String[] {});
		}
	}
}
