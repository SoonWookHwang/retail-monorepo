package com.retail.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.product.dto.ProductRequest;
import com.retail.product.entity.Brand;
import com.retail.product.entity.Category;
import com.retail.product.repository.BrandRepository;
import com.retail.product.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private BrandRepository brandRepository;
  @Autowired private CategoryRepository categoryRepository;

  private Long brandId;
  private Long categoryId;

  @BeforeEach
  void setup() {
    brandId = brandRepository.save(Brand.builder().name("LG").build()).getId();
    categoryId = categoryRepository.save(Category.builder().name("Monitor").build()).getId();
  }

  @Test
  @DisplayName("POST /api/products 요청 시 상품이 성공적으로 등록된다.")
  void createProduct() throws Exception {
    ProductRequest req = ProductRequest.builder()
        .name("UltraFine 5K")
        .price(1800000)
        .description("5K Retina Display Monitor")
        .brandId(brandId)
        .categoryId(categoryId)
        .stockQuantity(20)
        .imageUrls(List.of("https://example.com/monitor.jpg"))
        .build();

    mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.name").value("UltraFine 5K"))
        .andExpect(jsonPath("$.data.brandName").value("LG"));
  }

  @Test
  @DisplayName("GET /api/products 요청 시 상품 목록이 반환된다.")
  void getAllProducts() throws Exception {
    ProductRequest req = ProductRequest.builder()
        .name("Gram Laptop")
        .price(1700000)
        .description("Lightweight LG laptop")
        .brandId(brandId)
        .categoryId(categoryId)
        .stockQuantity(5)
        .imageUrls(List.of("https://example.com/gram.jpg"))
        .build();

    // 등록
    mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk());

    // 전체 조회
    mockMvc.perform(get("/api/products"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data", hasSize(greaterThan(0))))
        .andExpect(jsonPath("$.data[0].brandName").value("LG"));
  }

  @Test
  @DisplayName("GET /api/products/{id} 요청 시 단일 상품 정보가 반환된다.")
  void getProductById() throws Exception {
    ProductRequest req = ProductRequest.builder()
        .name("UltraGear Monitor")
        .price(1200000)
        .description("Gaming monitor 240Hz")
        .brandId(brandId)
        .categoryId(categoryId)
        .stockQuantity(10)
        .imageUrls(List.of("https://example.com/ultragear.jpg"))
        .build();

    // 등록 후 ID 추출
    String content = mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    // JSON에서 ID 추출
    Long productId = objectMapper.readTree(content).path("data").path("id").asLong();

    // 단건 조회
    mockMvc.perform(get("/api/products/" + productId))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.name").value("UltraGear Monitor"))
        .andExpect(jsonPath("$.data.brandName").value("LG"));
  }

  @Test
  @DisplayName("DELETE /api/products/{id} 요청 시 상품이 삭제된다.")
  void deleteProduct() throws Exception {
    ProductRequest req = ProductRequest.builder()
        .name("LG OLED TV")
        .price(3300000)
        .description("77 inch OLED")
        .brandId(brandId)
        .categoryId(categoryId)
        .stockQuantity(3)
        .imageUrls(List.of("https://example.com/oled.jpg"))
        .build();

    String content = mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Long productId = objectMapper.readTree(content).path("data").path("id").asLong();

    mockMvc.perform(delete("/api/products/" + productId))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }
}
