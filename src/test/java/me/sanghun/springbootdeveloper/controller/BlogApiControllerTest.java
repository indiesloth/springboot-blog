package me.sanghun.springbootdeveloper.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import me.sanghun.springbootdeveloper.domain.Article;
import me.sanghun.springbootdeveloper.dto.*;
import me.sanghun.springbootdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest // 테스트용 애플리케이션 컨텍스트
@AutoConfigureMockMvc //MockMvc 생성 및 자동 구성
class BlogApiControllerTest {

  @Autowired
  private MockMvc mockMvc;

  /*
    ObjectMapper 클래스로 만든 객체는 자바 객체를 JSON 데이터로 변환하는 직렬화serialization 또는
    반대로 JSON 데이터를 자바에서 사용하기 위해 자바 객체로 변환하는 역직렬화deserialization할 때 사용합니다.
    ObjectMapper 클래스는 Jackson 라이브러리에서 제공합니다.
   */
  @Autowired
  private ObjectMapper objectMapper;  // 직렬화, 역직렬화를 위한 클래스

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private BlogRepository blogRepository;

  @BeforeEach // 테스트 실행 전 실행하는 메서드
  public void mockMvcSetup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    blogRepository.deleteAll();
  }


  @DisplayName("AddArticle: 블로그 글 추가에 성공한다.")
  @Test
  public void addArticle() throws Exception {
    //given
    final String url = "/api/articles";
    final String title = "title";
    final String content = "content";
    final AddArticleRequest userRequest = new AddArticleRequest(title, content);

    // 객체 JSON으로 직렬화
    final String requestBody = objectMapper.writeValueAsString(userRequest);

    // when
    // 설정한 내용을 바탕으로 요청 전송
    // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
    ResultActions result = mockMvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(requestBody));

    // then
    // import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
    result.andExpect(status().isCreated());

    List<Article> articles = blogRepository.findAll();

    // import static org.assertj.core.api.Assertions.assertThat;
    assertThat(articles.size()).isEqualTo(1); // 크기가 1인지 검증
    assertThat(articles.get(0).getTitle()).isEqualTo(title);
    assertThat(articles.get(0).getContent()).isEqualTo(content);
  }

  @DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다.")
  @Test
  public void findAllArticles() throws Exception {
    // given
    final String url = "/api/articles";
    final String title = "title";
    final String content = "content";

    blogRepository.save(Article.builder()
        .title(title)
        .content(content)
        .build());

    // when
    final ResultActions resultActions = mockMvc.perform(
        get(url).accept(MediaType.APPLICATION_JSON));

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].content").value(content))
        .andExpect(jsonPath("$[0].title").value(title));
  }

  @DisplayName("findArticle: 블로그 글 조회에 성공한다.")
  @Test
  public void findArticle() throws Exception {
    // given
    final String url = "/api/articles/{id}";
    final String title = "title";
    final String content = "content";

    Article savedArticle = blogRepository.save(Article.builder()
        .title(title)
        .content(content)
        .build());

    // when
    final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value(content))
        .andExpect(jsonPath("$.title").value(title));
  }

  @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
  @Test
  public void deleteArticle() throws Exception {
    // given
    final String url = "/api/articles/{id}";
    final String title = "title";
    final String content = "content";

    Article savedArticle = blogRepository.save(Article.builder()
        .title(title)
        .content(content)
        .build());

    // when
    ResultActions resultActions = mockMvc.perform(delete(url, savedArticle.getId()))
        .andExpect(status().isOk());

    // then
    List<Article> articles = blogRepository.findAll();

    assertThat(articles).isEmpty();
  }

  @DisplayName("updateArticle: 블로그 글 수정에 성공한다.")
  @Test
  public void updateArticle() throws Exception {
    // given
    final String url = "/api/articles/{id}";
    final String title = "title";
    final String content = "content";

    Article savedArticle = blogRepository.save(Article.builder()
        .title(title)
        .content(content)
        .build());

    final String newTitle = "new Title";
    final String newContent = "new Content";

    UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

    // when
    ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)));

    // then
    result.andExpect(status().isOk());

    Article article = blogRepository.findById(savedArticle.getId()).get();

    assertThat(article.getTitle()).isEqualTo(newTitle);
    assertThat(article.getContent()).isEqualTo(newContent);
  }
}