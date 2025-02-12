package com.example.cbr.processor;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class ActionFigureToysProcessFlow extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("activemq:actionFigureToysProcessFlow.in")
            .log("Processing Action Figure Toys Order")
            .process(exchange -> {
            	String jsonPayload = exchange.getIn().getBody(String.class);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode order = mapper.readTree(jsonPayload);

                ((ObjectNode) order.at("/order")).put("specialBadge", "Limited Edition");
                log.info("+++++++Added limited edition badge for action figure order++++++++++");

                exchange.getIn().setBody(mapper.writeValueAsString(order));
                if (exchange.getIn().getHeader("exitProcess") != null) {
                    exchange.getIn().setHeader("nextHop", "exit");
                } else {
                	exchange.getIn().setHeader("nextHop", "outBound");
        		}
            })
            .choice()
            .when(header("nextHop").isEqualTo("exit"))
                .to("activemq:exit.in")
    		.when(header("nextHop").isEqualTo("outbound"))
    			.to("activemq:outboud.in");
    }
}
