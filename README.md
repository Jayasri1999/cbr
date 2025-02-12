# Content Based Routing
Process:

->Store scenarios and category routing details in MySQL

->Send XML data to Apache Active Message Queue. The queue will be in the format of scenario.country.instance.in (eg:sc1.US.1.in).

->Inbound Adapter listens to the input queue and based on the name of the queue, it will fetch the corresponding process flow from cache or database.

->Stores the scenario, country, instance to headers. Also, stores entry process, transform process, category process flows, xslt content and exit process if they are not null.

->Based on the next hop, we will send the payload to next queue. If entry process exists for particular scenario, entry adapter will consume payload from entry.in.

->If transform process exists for particular scenario, transform adapter will consume payload from transform.in and will transform the XML data to JSON based on the XSLT content. Then, it sends the payload to next queue.

-> If category routing process exists for particular category and subcategory, adapter will consume payload from respective queue and will process the JSON data accordingly. Then, it sends the payload to next queue.

->If exit process exists, exit adapter will consume payload from exit.in and will send the payload to outbound.in

->Outbound Adapter consumes data from outbound.in queue and sends payload to output queue which is in the format of scenario.country.instance.out (eg:sc1.US.1.out)

Steps:

-> To run Active MQ: docker run -d --name activemq -p 8161:8161 -p 61616:61616 rmohr/activemq

-> To access Active MQ: http://localhost:8161/admin username:admin & password:admin

-> Clone the repository git clone https://github.com/Jayasri1999/cbr.git

-> To execute db related scripts: check cbr/database/tables.sql and data.sql

-> Build the Project mvn clean install

-> Run the application mvn spring-boot:run

-> Run the following sample command for Windows:

```
$creds = Get-Credential -Credential admin
Invoke-WebRequest -Uri "http://localhost:8161/api/message/sc1.US.1.in?type=queue"  -Method Post  -Body '<?xml version="1.0" encoding="UTF-8"?>
<order>
    <id>11223</id>
    <customer>Jayasri</customer>
    <amount>10.00</amount>
    <country>IN</country>
    <category>
        <name>kidsToys</name>
        <subcategories>
            <subcategory>
                <name>actionFigures</name>
                <items>
                    <item>
                        <name>Ironmane</name>
                        <price>70.00</price>
			<ageGroup>10</ageGroup>
                    </item>
                </items>
            </subcategory>
        </subcategories>
    </category>
</order>'  -ContentType "text/xml"  -Credential $creds
```

-> We can observe the results in ActiveMQ web console under "Queues" tab.
