package me.sanghun.springbootdeveloper.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.sanghun.springbootdeveloper.domain.Article;
import me.sanghun.springbootdeveloper.dto.AddArticleRequest;
import me.sanghun.springbootdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlogService {

  private final BlogRepository blogRepository;

  public Article save(AddArticleRequest request) {
    return blogRepository.save(request.toEntity());
  }

  public List<Article> findAll() {
    return blogRepository.findAll();
  }
}
