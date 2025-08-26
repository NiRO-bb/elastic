package com.example.elastic_api_service;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.time.Duration;

public abstract class AbstractContainer {

    static final ElasticsearchContainer container;

    static {
        container = new ElasticsearchContainer("elasticsearch:8.17.10")
                        .withExposedPorts(9200)
                        .withEnv("xpack.security.enabled", "false")
                        .withEnv("ES_JAVA_OPTS", "-Xms256m -Xmx256m")
                        .withStartupTimeout(Duration.ofSeconds(40))
                        .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                                new HostConfig()
                                        .withPortBindings(new PortBinding(
                                                Ports.Binding.bindPort(9200),
                                                new ExposedPort(9200)))
                        ));
        container.start();
        updateProperties();
    }

    private static void updateProperties() {
        System.setProperty("spring.elasticsearch.uris", container.getHttpHostAddress());
        System.setProperty("elastic-service.index.method", "method_index");
        System.setProperty("elastic-service.index.request", "request_index");
    }

}
