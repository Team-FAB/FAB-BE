package com.fab.banggabgo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.fab.banggabgo.repository.mongo")
@EnableMongoAuditing
public class MongoAuditConfig {

}
