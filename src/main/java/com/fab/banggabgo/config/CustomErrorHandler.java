package com.fab.banggabgo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CustomErrorHandler implements ResponseErrorHandler {
    private String responseBody;

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        // Handle specific error statuses here
        return response.getStatusCode() != HttpStatus.OK;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            log.info(response.getBody().toString());
        } else {
            // Handle other error statuses as needed
            log.info(response.getBody().toString());

            this.responseBody = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
        }
    }

    public String getResponseBody() {
        return responseBody;
    }
}
