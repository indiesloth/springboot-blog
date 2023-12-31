package me.sanghun.springbootdeveloper.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.sanghun.springbootdeveloper.domain.Article;
import me.sanghun.springbootdeveloper.dto.*;
import me.sanghun.springbootdeveloper.service.BlogService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController //클래스에 붙이면 객체 데이터를 Json 형식으로 반환
public class BlogApiController {

  private final BlogService blogService;

  // HTTP 메서드가 POST일 때 전달받은 URL과 동일하면 메서드로 매핑
  @PostMapping("/api/articles")
  // @RequestBody로 요청 본문 값 매핑
  public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
    Article savedArticle = blogService.save(request);
    // 요청한 자원이 성공적으로 생성되었으 저장된 블로그 글 정보를 응답 객체에 담아 전송
    return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    /*
    · 200 OK : 요청이 성공적으로 수행되었음
    · 201 Created : 요청이 성공적으로 수행되었고, 새로운 리소스가 생성되었음
    · 400 Bad Request : 요청 값이 잘못되어 요청에 실패했음
    · 403 Forbidden : 권한이 없어 요청에 실패했음
    · 404 Not Found : 요청 값으로 찾은 리소스가 없어 요청에 실패했음
    · 500 Internal Server Error : 서버 상에 문제가 있어 요청에 실패했음
     */
  }

  @GetMapping("/api/articles")
  public ResponseEntity<List<ArticleResponse>> findAllArticles() {
    List<ArticleResponse> articles = blogService.findAll().stream().map(ArticleResponse::new)
        .toList();

    return ResponseEntity.ok().body(articles);
  }

  @GetMapping("/api/articles/{id}") //URL 경로에서 값 추출
  public ResponseEntity<ArticleResponse> findArticle(@PathVariable Long id) {
    Article article = blogService.findById(id);

    return ResponseEntity.ok().body(new ArticleResponse(article));
  }

  @DeleteMapping("/api/articles/{id}")
  public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
    System.out.println("삭제 메서드");
    blogService.delete(id);

    return ResponseEntity.ok().build();
  }

  @PutMapping("/api/articles/{id}")
  public ResponseEntity<Article> updateArticle(@PathVariable Long id,
      @RequestBody UpdateArticleRequest request) {
    Article updatedArticle = blogService.update(id, request);

    return ResponseEntity.ok().body(updatedArticle);
  }
}
