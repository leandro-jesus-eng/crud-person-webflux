package br.com.leandro.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class CrudPersonWebfluxApplication {

	/*static {
		BlockHound.install(
				builder -> builder.allowBlockingCallsInside("java.util.UUID", "randomUUID")
						.allowBlockingCallsInside("java.io.FilterInputStream", "read")
						.allowBlockingCallsInside("java.io.InputStream", "readNBytes")
		);
	}*/

	public static void main(String[] args) {
		SpringApplication.run(CrudPersonWebfluxApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// dependendo da aplicação, é melhor configurar na infra
		TimeZone.setDefault(TimeZone.getTimeZone("UTC-3"));
	}
}
