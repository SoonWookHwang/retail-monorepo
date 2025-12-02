package com.retail.product.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.FieldValueFactorModifier;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.retail.product.document.ProductDocument;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

  private final ElasticsearchClient client;

  public void index(ProductDocument doc) {
    try {
      client.index(i -> i
          .index("products")
          .id(String.valueOf(doc.getId()))
          .document(doc)
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void delete(Long id) {
    try {
      client.delete(d -> d.index("products").id(String.valueOf(id)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<ProductDocument> search(String keyword) {
    try {
      SearchResponse<ProductDocument> response = client.search(s -> s
          .index("products")
          .query(q -> q
              .multiMatch(m -> m
                  .query(keyword)
                  .fields("name", "description")
              )
          ), ProductDocument.class);

      return response.hits().hits().stream()
          .map(Hit::source)
          .filter(Objects::nonNull)
          .toList();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<ProductDocument> searchWithBoost(String keyword) {
    try {
      SearchResponse<ProductDocument> response = client.search(s -> s
              .index("products")
              .query(q -> q
                  .functionScore(fs -> fs
                      .query(q2 -> q2
                          .multiMatch(m -> m
                              .query(keyword)
                              .fields("name", "description")
                          )
                      )
                      .boostMode(FunctionBoostMode.Multiply)
                      .functions(f -> f
                          .fieldValueFactor(ff -> ff
                              .field("likeCount")
                              .factor(1.5)
                              .modifier(FieldValueFactorModifier.Log1p)
                          )
                      )
                  )
              ),
          ProductDocument.class
      );

      return response.hits().hits().stream()
          .map(Hit::source)
          .filter(Objects::nonNull)
          .toList();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


}
