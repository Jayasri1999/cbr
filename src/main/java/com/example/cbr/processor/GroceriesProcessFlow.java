package com.example.cbr.processor;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class GroceriesProcessFlow extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("activemq:groceriesProcessFlow.in")
            .log("Processing Groceries Order")
            .process(exchange -> {
            	String jsonPayload = exchange.getIn().getBody(String.class);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode order = mapper.readTree(jsonPayload);

                double amount = order.at("/order/amount").asDouble();
                if (amount > 30) {
                    ((ObjectNode) order.at("/order")).put("loyaltyPoints", "50 Bonus Points");
                    System.out.println("++++++++++Added 50 loyalty points for groceries order++++++++++");
                }

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
