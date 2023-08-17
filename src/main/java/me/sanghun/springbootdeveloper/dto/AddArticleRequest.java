package me.sanghun.springbootdeveloper.dto;

import lombok.*;
import me.sanghun.springbootdeveloper.domain.Article;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {

  private String title;
  private String content;

  public Article toEntity() {
    return Article.builder()
        .title(title)
        .content(content)
        .build();
  }
}
