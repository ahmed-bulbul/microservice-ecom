package com.ms.order_service.config;

import com.ms.order_service.client.InventoryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    @Bean
    public InventoryClient inventoryClient() {
        // Create and configure SimpleClientHttpRequestFactory
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000); // 3 seconds
        factory.setReadTimeout(3000);    // 3 seconds

        // Build RestClient
        RestClient restClient = RestClient.builder()
                .baseUrl(inventoryServiceUrl)
                .requestFactory(factory) // Pass the factory directly (NOT a lambda!)
                .build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(InventoryClient.class);
    }
}
