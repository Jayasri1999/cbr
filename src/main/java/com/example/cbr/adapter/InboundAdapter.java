package com.example.cbr.adapter;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.cbr.cache.CategoryRoutingCache;
import com.example.cbr.cache.ProcessFlowCache;
import com.example.cbr.entity.CategoryRouting;
import com.example.cbr.entity.ProcessFlow;
import com.example.cbr.repository.CategoryRoutingRepository;
import com.example.cbr.repository.ProcessFlowRepository;


@Component
public class InboundAdapter extends RouteBuilder{
	@Autowired
	ProcessFlowRepository processFlowRepository;
	@Autowired
	ProcessFlowCache processFlowCache;
	@Autowired
	CategoryRoutingRepository categoryRoutingRepository;
	@Autowired
	CategoryRoutingCache categoryRoutingCache;
	@Override
	public void configure()  throws Exception{
	    configureInboundRoute("sc1.US.1.in");
	    configureInboundRoute("sc2.MX.2.in");
	}
	public void configureInboundRoute(String inputQueue){
		from("activemq:"+inputQueue)
		.process(exchange->{
			log.info("+++Before Inbound Process+++");
			inboundProcess(exchange);
			log.info("+++After Inbound Process+++");
		})
		.choice()
        .when(header("nextHop").isEqualTo("entry"))
            .to("activemq:entry.in")
        .when(header("nextHop").isEqualTo("transform"))
            .to("activemq:transform.in")
        .when(header("nextHop").isEqualTo("exit"))
            .to("activemq:exit.in")
		.when(header("nextHop").isEqualTo("outbound"))
			.to("activemq:outboud.in");
		
	}
	

	
	public void inboundProcess(Exchange exchange) {
		//Process data from queue name
		String inputQueue= exchange.getIn().getHeader("JMSDestination",String.class);
		String[] fields= inputQueue.split("\\.");
		String scenario= fields[0].substring(8);
		String country= fields[1];
		String instance= fields[2];
		log.info("scenario: "+scenario+" country: "+country+" instance: "+instance);
		// Extract category and subcategory using XPath
        String categoryName = exchange.getContext().resolveLanguage("xpath")
                .createExpression("/order/category/name/text()")
                .evaluate(exchange, String.class); 
        String subCategoryName = exchange.getContext().resolveLanguage("xpath")
                .createExpression("/order/category/subcategories/subcategory/name/text()")
                .evaluate(exchange, String.class); 
        log.info("categoryName: "+ categoryName+" subCategoryName: "+subCategoryName);
		//fetch from cache or db
		ProcessFlow processFlow= processFlowCache.getProcessFlow(scenario, country, instance);

		CategoryRouting categoryRouting = categoryRoutingCache.getCategoryRouting(categoryName, subCategoryName);
		
		log.info("+++++++get categoryRouting: "+categoryRouting);
		log.info("+++++++get processflow: "+processFlow);
		//set header
        exchange.getIn().setHeader("scenario", scenario);
        exchange.getIn().setHeader("country", country);
        exchange.getIn().setHeader("instance", instance);
        exchange.getIn().setHeader("entryProcess", processFlow.getEntry_process());
        exchange.getIn().setHeader("transformProcess", processFlow.getTransform_process());
        exchange.getIn().setHeader("exitProcess", processFlow.getExit_process());
        exchange.getIn().setHeader("xsltContent", categoryRouting.getXslt_content());
        exchange.getIn().setHeader("category", categoryName);
        exchange.getIn().setHeader("subCategoryName", subCategoryName);
        exchange.getIn().setHeader("categoryProcessFlow", categoryRouting.getCatProcessFlow());
        

        // Determine next hop
        if (processFlow.getEntry_process() != null) {
            exchange.getIn().setHeader("nextHop", "entry");
        } else if (processFlow.getTransform_process() != null) {
            exchange.getIn().setHeader("nextHop", "transform");
        } else if (processFlow.getExit_process() != null) {
            exchange.getIn().setHeader("nextHop", "exit");
        } else {
        	exchange.getIn().setHeader("nextHop", "outbound");
        }
	}
	
	public ProcessFlow fetchProcessFlow(String scenario, String country, String instance) {
		ProcessFlow processFlow = processFlowRepository.findByKeyCountryInstance(scenario, country, instance)
				.orElseThrow(() -> new IllegalArgumentException("ProcessFlow not found"));
		return processFlow;
	}
	public CategoryRouting fetchCategoryRouting(String category_name, String subcategory_name) {
		CategoryRouting categoryRouting = categoryRoutingRepository.findByCatSubCatCntry(category_name, subcategory_name)
				.orElseThrow(() -> new IllegalArgumentException("Category Routing not found"));
		log.info("+++inside fetchrepo+++"+categoryRouting.getCategory_name()+", catprocess"+categoryRouting.getCatProcessFlow());
		return categoryRouting;
	}

}
