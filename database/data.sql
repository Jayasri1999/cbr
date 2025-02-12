--Insert data into process_flow table
INSERT INTO messageProcessor.process_flow (scenario, country, instance, entry_process, transform_process, exit_process,xslt_content)
VALUES 
    ('sc1', 'US', '1','entryProcess1', 'transformProcess1', 'exitProcess1', null),
    ('sc2', 'MX', '2', 'entryProcess1', null, 'exitProcess1',null);

--Insert data into category_routing table
--Insert data for cat: shoes subCat: sneakers
INSERT INTO messageProcessor.category_routing (category_name, subcategory_name, cat_process_flow,xslt_content)
VALUES ('shoes','sneakers','shoeProcessFlow', '<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" encoding="UTF-8"/>

    <xsl:template match="/">
        {
            "order": {
                "id": "<xsl:value-of select="/order/id"/>",
                "customer": "<xsl:value-of select="/order/customer"/>",
                "amount": "<xsl:value-of select="/order/amount"/>",
                "country": "<xsl:value-of select="/order/country"/>",
                "category": {
                    "name": "<xsl:value-of select="/order/category/name"/>",
                    "subcategories": [
                        <xsl:for-each select="/order/category/subcategories/subcategory">
                            {
                                "name": "<xsl:value-of select="name"/>",
                                "items": [
                                    <xsl:for-each select="items/item">
                                        {
                                            "name": "<xsl:value-of select="name"/>",
                                            "price": "<xsl:value-of select="price"/>",
					    "size": "<xsl:value-of select="size"/>"
                                        }<xsl:if test="position() != last()">,</xsl:if>
                                    </xsl:for-each>
                                ]
                            }<xsl:if test="position() != last()">,</xsl:if>
                        </xsl:for-each>
                    ]
                }
            }
        }
    </xsl:template>
</xsl:stylesheet>');

--Insert data for cat: groceries subCat: freshProduce
INSERT INTO messageProcessor.category_routing (category_name, subcategory_name, cat_process_flow, xslt_content)
VALUES ('groceries','freshProduce','groceriesProcessFlow', '<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" encoding="UTF-8"/>

    <xsl:template match="/">
        {
            "order": {
                "id": "<xsl:value-of select="/order/id"/>",
                "customer": "<xsl:value-of select="/order/customer"/>",
                "amount": "<xsl:value-of select="/order/amount"/>",
                "country": "<xsl:value-of select="/order/country"/>",
                "category": {
                    "name": "<xsl:value-of select="/order/category/name"/>",
                    "subcategories": [
                        <xsl:for-each select="/order/category/subcategories/subcategory">
                            {
                                "name": "<xsl:value-of select="name"/>",
                                "items": [
                                    <xsl:for-each select="items/item">
                                        {
                                            "name": "<xsl:value-of select="name"/>",
                                            "price": "<xsl:value-of select="price"/>",
					    "loyaltyPoints": "<xsl:value-of select="loyaltyPoints"/>"
                                        }<xsl:if test="position() != last()">,</xsl:if>
                                    </xsl:for-each>
                                ]
                            }<xsl:if test="position() != last()">,</xsl:if>
                        </xsl:for-each>
                    ]
                }
            }
        }
    </xsl:template>
</xsl:stylesheet>');

--Insert data for cat: jewelry subCat: diamonds
INSERT INTO messageProcessor.category_routing (category_name, subcategory_name, cat_process_flow, xslt_content)
VALUES ('jewelry','diamonds', 'jewelryProcessFlow','<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" encoding="UTF-8"/>

    <xsl:template match="/">
        {
            "order": {
                "id": "<xsl:value-of select="/order/id"/>",
                "customer": "<xsl:value-of select="/order/customer"/>",
                "amount": "<xsl:value-of select="/order/amount"/>",
                "country": "<xsl:value-of select="/order/country"/>",
                "category": {
                    "name": "<xsl:value-of select="/order/category/name"/>",
                    "subcategories": [
                        <xsl:for-each select="/order/category/subcategories/subcategory">
                            {
                                "name": "<xsl:value-of select="name"/>",
                                "items": [
                                    <xsl:for-each select="items/item">
                                        {
                                            "name": "<xsl:value-of select="name"/>",
                                            "price": "<xsl:value-of select="price"/>",
					    "appraisalValue": "<xsl:value-of select="appraisalValue"/>"
                                        }<xsl:if test="position() != last()">,</xsl:if>
                                    </xsl:for-each>
                                ]
                            }<xsl:if test="position() != last()">,</xsl:if>
                        </xsl:for-each>
                    ]
                }
            }
        }
    </xsl:template>
</xsl:stylesheet>');


--Insert data for cat: kidsToys subCat: generic
INSERT INTO messageProcessor.category_routing (category_name, subcategory_name, cat_process_flow, xslt_content)
VALUES ('kidsToys','generic','kidsToysProcessFlow','<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" encoding="UTF-8"/>

    <xsl:template match="/">
        {
            "order": {
                "id": "<xsl:value-of select="/order/id"/>",
                "customer": "<xsl:value-of select="/order/customer"/>",
                "amount": "<xsl:value-of select="/order/amount"/>",
                "country": "<xsl:value-of select="/order/country"/>",
                "category": {
                    "name": "<xsl:value-of select="/order/category/name"/>",
                    "subcategories": [
                        <xsl:for-each select="/order/category/subcategories/subcategory">
                            {
                                "name": "<xsl:value-of select="name"/>",
                                "items": [
                                    <xsl:for-each select="items/item">
                                        {
                                            "name": "<xsl:value-of select="name"/>",
                                            "price": "<xsl:value-of select="price"/>",
					    "ageGroup": "<xsl:value-of select="ageGroup"/>"
                                        }<xsl:if test="position() != last()">,</xsl:if>
                                    </xsl:for-each>
                                ]
                            }<xsl:if test="position() != last()">,</xsl:if>
                        </xsl:for-each>
                    ]
                }
            }
        }
    </xsl:template>
</xsl:stylesheet>');

--Insert data for cat: kidsToys subCat: actionFigures
INSERT INTO messageProcessor.category_routing (category_name, subcategory_name, cat_process_flow, xslt_content)
VALUES ('kidsToys','generic','kidsToysProcessFlow','<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" encoding="UTF-8"/>

    <xsl:template match="/">
        {
            "order": {
                "id": "<xsl:value-of select="/order/id"/>",
                "customer": "<xsl:value-of select="/order/customer"/>",
                "amount": "<xsl:value-of select="/order/amount"/>",
                "country": "<xsl:value-of select="/order/country"/>",
                "category": {
                    "name": "<xsl:value-of select="/order/category/name"/>",
                    "subcategories": [
                        <xsl:for-each select="/order/category/subcategories/subcategory">
                            {
                                "name": "<xsl:value-of select="name"/>",
                                "items": [
                                    <xsl:for-each select="items/item">
                                        {
                                            "name": "<xsl:value-of select="name"/>",
                                            "price": "<xsl:value-of select="price"/>",
					    "ageGroup": "<xsl:value-of select="ageGroup"/>"
                                        }<xsl:if test="position() != last()">,</xsl:if>
                                    </xsl:for-each>
                                ]
                            }<xsl:if test="position() != last()">,</xsl:if>
                        </xsl:for-each>
                    ]
                }
            }
        }
    </xsl:template>
</xsl:stylesheet>');