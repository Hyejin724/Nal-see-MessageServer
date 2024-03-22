package everycoding.NalseeFlux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@EnableWebSocketMessageBroker
//@EnableMongoRepositories(basePackages = "everycoding.NalseeFlux.chat")
public class NalSeeFluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(NalSeeFluxApplication.class, args);
	}

}
