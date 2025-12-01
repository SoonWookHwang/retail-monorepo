package com.retail.product.controller;

import com.retail.common.dto.ApiResponse;
import com.retail.common.resolver.CurrentUser;
import com.retail.product.service.ProductLikeService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductLikeController {

  private final ProductLikeService productLikeService;

  @PostMapping("/{id}/like")
  public ApiResponse<Map<String, Boolean>> toggleLike(
      @PathVariable Long id,
      @CurrentUser Long userId
  ) {
    boolean liked = productLikeService.toggleLike(userId, id);
    return ApiResponse.ok(Map.of("liked", liked));
  }

}
