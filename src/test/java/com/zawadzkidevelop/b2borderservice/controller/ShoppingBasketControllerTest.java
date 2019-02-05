package com.zawadzkidevelop.b2borderservice.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ShoppingBasketControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void saveNewProductIntoBasket() throws Exception {
        //GIVEN
        String requestUrl = "/api/basket/1/add?productId=testProductId&quantity=10";

        //WHEN
        ResultActions perform = mockMvc.perform(put(requestUrl).contentType(contentType));

        //THEN
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].quantity", is(any(Integer.class))))
                .andExpect(jsonPath("$.[0].productId", is(any(String.class))));
    }

    @Test
    public void saveProductIntoBasketWhenQuantityLessThanZero() throws Exception {
        //GIVEN
        String requestUrl = "/api/basket/1/add?productId=abc&quantity=-10";
        String expectedCause = "Product quantity below 0";

        //WHEN
        ResultActions perform = mockMvc.perform(put(requestUrl).contentType(contentType));

        //THEN
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cause", is(expectedCause)));
    }

    @Test
    public void saveProductIntoBasketWhenQuantityEqualsZero() throws Exception {
        //GIVEN
        String requestUrl = "/api/basket/1/add?productId=def&quantity=0";

        //WHEN
        ResultActions perform = mockMvc.perform(put(requestUrl).contentType(contentType));

        //THEN
        perform.andExpect(status().isOk());
    }
}
