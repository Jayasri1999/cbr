-- create schema
create schema messageProcessor;

-- create table
CREATE TABLE messageProcessor.process_flow (
    scenario VARCHAR(10),
    country VARCHAR(10),
    instance VARCHAR(10),
    entry_process VARCHAR(255),
    transform_process VARCHAR(255),
    exit_process VARCHAR(255),
    xslt_content TEXT NULL,
    PRIMARY KEY (scenario, country, instance)
);

CREATE TABLE messageProcessor.category_routing (
    category_name VARCHAR(100),
    subcategory_name VARCHAR(100),
    cat_process_flow VARCHAR(255),
    xslt_content TEXT NULL,
    PRIMARY KEY (category_name, subcategory_name)
);