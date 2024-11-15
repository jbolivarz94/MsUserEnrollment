package co.com.bancolombia.events.handlers;

import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.async.impl.config.annotations.EnableEventListeners;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@EnableEventListeners
public class EventsHandler {

    public Mono<Void> handleEventA(DomainEvent<Object> event) {
        System.out.println("event received: " + event.getName() + " ->" + event.getData()); // TODO: Remove this line
        return Mono.empty();
    }
}
