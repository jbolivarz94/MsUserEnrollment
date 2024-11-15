package co.com.bancolombia.events;
import co.com.bancolombia.events.handlers.EventsHandler;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerRegistryConfiguration {

    // see more at: https://reactivecommons.org/reactive-commons-java/#_handlerregistry_2
    @Bean
    public HandlerRegistry handlerRegistry(EventsHandler events) {
        return HandlerRegistry.register()
                .listenEvent("business.aliasidentity.register.svp.channelMicroservice.registerDone", events::handleEventA, Object.class)
                .listenEvent("business.aliasidentity.register.app.channelMicroservice.registerDone", events::handleEventA, Object.class);
    }
}
