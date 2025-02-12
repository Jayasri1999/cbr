package com.example.cbr.processor;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class ShoeProcessFlow extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("activemq:shoeProcessFlow.in")
            .log("Processing Shoe Order")
            .process(exchange -> {
            	String jsonPayload = exchange.getIn().getBody(String.class);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode order = mapper.readTree(jsonPayload);

                double amount = order.at("/order/amount").asDouble();
                if (amount > 100) {
                    double discountedAmount = amount * 0.9; // 10% discount
                    ((ObjectNode) order.at("/order")).put("amount", discountedAmount);
                    log.info("++++++++Applied 10% discount for shoe order. New amount: " + discountedAmount);
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
