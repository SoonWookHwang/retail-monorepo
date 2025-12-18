package com.retail.product.service.productimport;

import com.retail.product.entity.Category;
import com.retail.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryImportService {

  private final CategoryRepository categoryRepository;

  public void initCategories() {
    // 1) 가전제품
    Category electronic = createCategory("가전제품", null);
    createCategory("TV", electronic);
    createCategory("핸드폰", electronic);
    createCategory("컴퓨터", electronic);
    createCategory("모니터", electronic);

    // 2) 패션
    Category fashion = createCategory("패션", null);

    // 패션 - 남성의류
    Category men = createCategory("남성의류", fashion);
    createCategory("티셔츠", men);
    createCategory("맨투맨/후드", men);
    createCategory("셔츠/남방", men);
    createCategory("니트웨어/가디건", men);
    createCategory("자켓", men);

    // 패션 - 여성의류
    Category women = createCategory("여성의류", fashion);
    createCategory("티셔츠", women);
    createCategory("맨투맨/후드", women);
    createCategory("블라우스/셔츠", women);
    createCategory("니트웨어/가디건", women);

    // 3) 가구/인테리어
    Category furniture = createCategory("가구/인테리어", null);
    createCategory("침실가구", furniture);
    createCategory("거실가구", furniture);
  }

  private Category createCategory(String name, Category parent) {
    return categoryRepository.findByNameAndParent(name, parent)
        .orElseGet(() -> {
          Category newCategory = Category.builder()
              .name(name)
              .parent(parent)
              .build();
          if (parent != null) {
            parent.getChildren().add(newCategory);
          }
          return categoryRepository.save(newCategory);
        });
  }
}