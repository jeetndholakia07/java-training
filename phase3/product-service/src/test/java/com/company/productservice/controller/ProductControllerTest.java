package com.company.productservice.controller;

import com.company.productservice.dto.response.ProductResponse;
import com.company.productservice.service.GuidService;
import com.company.productservice.service.ProductService;
import com.company.productservice.util.StatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private GuidService guidService;

    @Test
    void shouldReturnProductByGuid() throws Exception {
        ProductResponse response = new ProductResponse();
        response.setProductName("Laptop");
        response.setStatus(StatusEnum.A);

        when(guidService.verifyUUID("guid-1"))
                .thenReturn(true);

        when(productService.getProductByGuid("guid-1"))
                .thenReturn(response);

        mockMvc.perform(get("/v1/products/guid-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName")
                        .value("Laptop"));
    }

    @Test
    void shouldReturnBadRequestForInvalidGuid()
            throws Exception {
        when(guidService.verifyUUID("bad-guid"))
                .thenReturn(false);

        mockMvc.perform(get("/v1/products/bad-guid"))
                .andExpect(status().isBadRequest());
    }
}