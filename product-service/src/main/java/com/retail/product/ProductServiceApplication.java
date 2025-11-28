package com.retail.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * product-service 애플리케이션 진입점
 * jwt.enabled=false 이므로 common.security 패키지 내 JWT 관련 Bean은 등록되지 않음
 */
@SpringBootApplication(scanBasePackages = {
		"com.retail.product",
		"com.retail.common"
})
public class ProductServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
}
