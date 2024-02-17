package com.chilun.apiopenspace.starter;

/**
 * @author 齿轮
 * @date 2024-02-17-14:38
 */
public class APIAccessClientFactory<T> {
    public APIAccessClient<T> produceClient(String gatewayAPIURI, String accesskey, String secretkey, Long expiration) {
        APIAccessClient<T> tAPIAccessClient = new APIAccessClient<>();
        tAPIAccessClient.setGatewayAPIURI(gatewayAPIURI);
        tAPIAccessClient.setAccesskey(accesskey);
        tAPIAccessClient.setSecretkey(secretkey);
        tAPIAccessClient.setExpiration(expiration);
        return tAPIAccessClient;
    }
}
