package br.com.bluesburguer.order.configuration;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class H2Config {

	@Value("${h2.web-server.web-port:8174}")
	private String webPort;
	
    @Value("${h2.pg-server.pg-port:8574}")
    private String pgPort;
	
    @Value("${h2.tcp-server.tcp-port:8774}")
    private String tcpPort;
	
    @Profile("dev")
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2WebServer() throws SQLException {
        return Server.createWebServer("-web", "-webAllowOthers", "-webPort", webPort,
        		"-properties", "~/.h2/" + webPort // Utilizando diretório customizado para salvar a URL de conexão no console web.
        );
    }

    @Profile("dev")
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server tcpServer() throws SQLException {
    	return Server.createTcpServer("-tcpPort", tcpPort, "-tcpAllowOthers");
    }
    
    @Profile("dev")
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2PgServer() throws SQLException {
        var pgParams = new String[] { "-pgAllowOthers", "-pgPort", pgPort };
        return Server.createPgServer(pgParams);
    }
}
