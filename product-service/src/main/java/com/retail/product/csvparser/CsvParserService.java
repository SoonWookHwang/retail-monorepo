package com.retail.product.csvparser;

import com.opencsv.CSVReader;
import com.retail.product.dto.csv.ProductCsvDto;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CsvParserService {

  public List<ProductCsvDto> parseProductCsv(MultipartFile file) {
    List<ProductCsvDto> list = new ArrayList<>();
    try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(),
        StandardCharsets.UTF_8))) {
      String[] row;
      reader.readNext();

      while ((row = reader.readNext()) != null) {
        String brand = row[0];
        String product = row[1];
        Integer price = Integer.valueOf(row[2]);
        String imageUrl = row[3];
        String categoryName = row[4];
        String parentCategoryName = row[5];
        list.add(new ProductCsvDto(brand, product, price, imageUrl,categoryName,parentCategoryName));
      }
    } catch (Exception e) {
      throw new RuntimeException("CSV 파싱 실패", e);
    }

    return list;
  }
}