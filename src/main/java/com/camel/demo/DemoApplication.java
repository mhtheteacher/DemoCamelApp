package com.camel.demo;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication extends RouteBuilder {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void configure() throws Exception {
	
		System.out.println("Application Started....");
		moveAllFilesFromOneLocToOther();
		multiFileProcessor("OrderStatus.txt");
		System.out.println("Application Ended...");
	}

	//To Move All Files From One Folder to Another
	public void moveAllFilesFromOneLocToOther(){
		from("file:source2?noop=true").to("file:destination")
		.log(LoggingLevel.INFO,"myCustomisedLog","moveAllFilesFromOneLocToOther Method Invoked");

	}

	
	// Convert A Specific File into 3 Files Based on the content 
	public void multiFileProcessor(String filename){
		from("file:source?noop=true").filter(header(Exchange.FILE_NAME).startsWith(filename)).unmarshal().csv()
		.split(body().tokenize(",")).choice()
		.when(body().contains("Completed")).to("file:destination?fileName=completed.csv")
		.when(body().contains("PendingConfirmation")).to("file:destination?fileName=pending.csv")
		.when(body().contains("OutForDelivery")).to("file:destination?fileName=progress.csv")
		.log(LoggingLevel.INFO,"myCustomisedLog","multiFileProcessor Method Invoked");
		
	}


}
