package owl_home.reactive_chat_client.config;


import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;


@Configuration
public class Config {
    @Bean
    public Queue anonQueue(){
        return new AnonymousQueue();
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("r_chat");
    }

    @Bean
    public Binding binding(FanoutExchange fanoutExchange, Queue anonQueue){
        return BindingBuilder.bind(anonQueue).to(fanoutExchange);
    }

    @Bean
    public RSocketRequester tcpRSocketRequester(RSocketStrategies strategies){
        return RSocketRequester.builder().rsocketStrategies(strategies()).tcp("localhost", 8090);
    }

    @Bean
    public RSocketStrategies strategies(){        
        return RSocketStrategies
                            .builder()
                            .decoders(decoders -> decoders.add(new Jackson2CborDecoder()))
                            .encoders(encoders -> encoders.add(new Jackson2CborEncoder()))
                            .build();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
