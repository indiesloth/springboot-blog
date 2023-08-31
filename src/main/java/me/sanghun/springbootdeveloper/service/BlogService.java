package me.sanghun.springbootdeveloper.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.sanghun.springbootdeveloper.domain.Article;
import me.sanghun.springbootdeveloper.dto.*;
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

  public Article findById(Long id) {
    return blogRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("not fount" + id));
  }

  public void delete(Long id) {
    blogRepository.deleteById(id);
  }

  @Transactional
  public Article update(Long id, UpdateArticleRequest request) {
    Article article = blogRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

    article.update(request.getTitle(), request.getContent());

    return article;
  }
}
