package com.paper.config;

import com.mongodb.ConnectionString;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;

@Configuration
public class MongoDbConfig {

    public @Bean MongoClientFactoryBean mongo() {
        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
        mongo.setConnectionString(new ConnectionString("mongodb+srv://makcimkakakakaka:komunist2023@cluster0.euyznd3.mongodb.net/?retryWrites=true&w=majority&appName=AtlasApp&authMechanism=SCRAM-SHA-1"));
        return mongo;
    }

}
