package owl_home.reactive_chat.configuration;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

@Configuration
public class Config {
    
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
	public RSocketMessageHandler rsocketMessageHandler() {
		RSocketMessageHandler handler = new RSocketMessageHandler();
		handler.setRSocketStrategies(rsocketStrategies());
		return handler;
	}

	@Bean
	public RSocketStrategies rsocketStrategies() {
		return RSocketStrategies.builder()
			.encoders(encoders -> encoders.add(new Jackson2CborEncoder()))
			.decoders(decoders -> decoders.add(new Jackson2CborDecoder()))
			.routeMatcher(new PathPatternRouteMatcher())
			.build();
	}
}
