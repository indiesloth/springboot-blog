package me.sanghun.springbootdeveloper.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateArticleRequest {
  private String title;
  private String content;
}
