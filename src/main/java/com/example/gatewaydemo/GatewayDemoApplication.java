package com.example.gatewaydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.net.URI;

@SpringBootApplication
public class GatewayDemoApplication {

	private static final URI URL = URI.create("http://httpbin.org:80");

	@Bean
	public RouteLocator myRoutes(final RouteLocatorBuilder builder) {
		return builder.routes()
			.route(r -> r.path("/get")
				.filters(f -> f.addRequestHeader("X-Test-Header", "Rewrite request"))
				.uri(URL))
			.route(r -> r.host("*.myhost.org")
				.filters(f -> f.addRequestHeader("X-Another-Test-Header", "Another rewrite request"))
				.uri(URL))
			.route(r -> r.host("*.rewrite.org")
				.filters(f -> f.rewritePath("/foo/(?<segment>.*)", "/${segment}"))
				.uri(URL))
			.build();

	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayDemoApplication.class, args);
	}
}
