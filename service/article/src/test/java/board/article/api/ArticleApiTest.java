package board.article.api;

import board.article.dto.response.ArticlePageResponse;
import board.article.dto.response.ArticleResponse;
import board.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

@ActiveProfiles("test")
public class ArticleApiTest {

    RestClient restClient = RestClient.create("http://localhost:9000");

    @Test
    void createArticle() {
        ArticleResponse response = create(new ArticleCreateRequest(
                "h1", "my content", 1L, 1L
        ));
        System.out.println("response = " + response);
    }

    ArticleResponse create(ArticleCreateRequest request) {
        return restClient.post()
                .uri("/v1/articles")
                .body(request)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void readArticle() {
        ArticleResponse read = read(207739800453959680L);
        System.out.println("response = " + read);
    }

    ArticleResponse read(Long id) {
        return restClient.get()
                .uri("/v1/articles/{articleId}", id)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void updateArticle() {
        update(207739800453959680L);
        ArticleResponse response = read(207739800453959680L);
        System.out.println("response = " + response);
    }

    void update(Long id) {
        restClient.put()
                .uri("/v1/articles/{articleId}", id)
                .body(new ArticleUpdateRequest("h2", "my content 22"))
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void deleteArticle() {
        restClient.delete()
                .uri("/v1/articles/{articleId}", 207481357067378688L)
                .retrieve()
                .toBodilessEntity();
    }

    @Test
    void readAllTest() {
        ArticlePageResponse response = restClient.get()
                .uri("v1/articles?boardId=1&page=50000&pageSize=30")
                .retrieve()
                .body(ArticlePageResponse.class);

        System.out.println("response. = getArticleCount() = " + response.getArticleCount());
        for (ArticleResponse article : response.getArticles()) {
            System.out.println("articleId = " + article.getArticleId());
        }
    }

    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest {
        private String title;
        private String content;
        private Long writerId;
        private Long boardId;
    }

    @Getter
    @AllArgsConstructor
    static class ArticleUpdateRequest {
        private String title;
        private String content;
    }
}
