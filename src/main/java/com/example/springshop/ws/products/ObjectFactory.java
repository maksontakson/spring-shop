package com.example.springshop.ws.products;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.polozov.springeshop.ws.products
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetProductsRequest }
     *
     */
    public GetProductsRequest createGetProductsRequest() {
        return new GetProductsRequest();
    }

    /**
     * Create an instance of {@link GetProductsResponse }
     *
     */
    public GetProductsResponse createGetProductsResponse() {
        return new GetProductsResponse();
    }

    /**
     * Create an instance of {@link ProductsWS }
     *
     */
    public ProductsWS createProductsWS() {
        return new ProductsWS();
    }

}
