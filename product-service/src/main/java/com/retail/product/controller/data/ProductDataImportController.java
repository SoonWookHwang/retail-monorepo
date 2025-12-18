package com.retail.product.controller.data;

import com.retail.product.dto.csv.ProductCsvDto;
import com.retail.product.service.productimport.BrandImportService;
import com.retail.product.service.productimport.CategoryImportService;
import com.retail.product.csvparser.CsvParserService;
import com.retail.product.service.productimport.ProductImportService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/import")
public class ProductDataImportController {

  private final CsvParserService csvParserService;
  private final BrandImportService brandImportService;
  private final ProductImportService productImportService;

  private final CategoryImportService categoryImportService;

  @PostMapping("/product-csv")
  public String importProductCsv(@RequestParam("file") MultipartFile file) {

    List<ProductCsvDto> dtos = csvParserService.parseProductCsv(file);
    // 브랜드 저장
    List<String> brandNames = dtos.stream()
        .map(ProductCsvDto::getBrandName)
        .toList();

    brandImportService.importBrands(brandNames);

    // 상품 저장
    productImportService.importProducts(dtos);

    return "업로드 완료: " + dtos.size() + "개의 상품이 저장되었습니다.";
  }

  @PostMapping("/products/update")
  public String updateAllProducts() {
    productImportService.allProductsUpdate();
    return "상품데이터 업데이트 완료";
  }

  @PostMapping("/categories")
  public String initCategories() {
    categoryImportService.initCategories();
    return "카테고리 초기 데이터 생성 완료";
  }
}