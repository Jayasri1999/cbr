package com.example.cbr.adapter;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.cbr.repository.CategoryRoutingRepository;

@Component
public class TransformAdapter extends RouteBuilder{
	@Autowired
	CategoryRoutingRepository categoryRoutingRepository;
	@Override
	public void configure() throws Exception{
		from("activemq:transform.in")
        .process(exchange -> {
            String transformProcessMethod = exchange.getIn().getHeader("transformProcess", String.class);
            invokeMethod(transformProcessMethod, exchange);
        })
		.choice()
		.when(header("nextHop").isEqualTo("categoryFlow"))
			.toD("activemq:${header.categoryProcessFlow}.in")
        .when(header("nextHop").isEqualTo("exit"))
            .to("activemq:exit.in")
		.when(header("nextHop").isEqualTo("outbound"))
			.to("activemq:outboud.in");
	}
	public void transformProcess1(Exchange exchange) {
		// transform xml to json using XSLT
        String xsltContent = exchange.getIn().getHeader("xsltContent", String.class);
        String body = exchange.getIn().getBody(String.class);
        try {
        String transformedBody = xmlToJsonTransformXSLT(body, xsltContent);
        exchange.getIn().setBody(transformedBody);
        } catch (TransformerException e) {
            log.error("Error during XSLT transformation", e);
            throw new RuntimeException("Transformation failed", e);
        }
        log.info("+++++++++++++during transformation+++++++++"+exchange.getIn().getHeader("categoryProcessFlow"));
        if(exchange.getIn().getHeader("categoryProcessFlow")!=null) {
        	exchange.getIn().setHeader("nextHop", "categoryFlow");
        }else if (exchange.getIn().getHeader("exitProcess") != null) {
            exchange.getIn().setHeader("nextHop", "exit");
        } else {
        	exchange.getIn().setHeader("nextHop", "outBound");
		}
	}
	
	public String xmlToJsonTransformXSLT(String body, String xsltContent) throws TransformerException  {
		TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(
            new StreamSource(new StringReader(xsltContent))
        );
        StringWriter writer = new StringWriter();
        transformer.transform(new StreamSource(new StringReader(body)), new StreamResult(writer));
        return writer.toString();
	}
	
	public void invokeMethod(String methodName, Object... params) {
	    try {
	        // Get method with exact name and parameter types
	        Class<?>[] paramTypes = new Class<?>[]{
	            Exchange.class
	        };	        
	        Method method = this.getClass().getMethod(methodName, paramTypes);
	        log.info("++++++++++invoke method++++++++++++++"+method);
	        method.invoke(this, params);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	

}
