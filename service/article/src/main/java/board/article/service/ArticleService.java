package board.article.service;

import board.article.dto.request.ArticleCreateRequest;
import board.article.dto.request.ArticleUpdateRequest;
import board.article.dto.response.ArticleResponse;
import board.article.entity.Article;
import board.article.repository.ArticleRepository;
import board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final Snowflake snowFlake = new Snowflake();
    private final ArticleRepository articleRepository;

    public ArticleResponse create(ArticleCreateRequest request) {
        Article article = articleRepository.save(
                Article.of(snowFlake.nextId(),
                        request.getTitle(),
                        request.getContent(),
                        request.getBoardId(),
                        request.getWriterId())
        );

        return ArticleResponse.fromArticle(article);
    }

    public ArticleResponse update(Long articleId, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(()->new RuntimeException("글을 찾을 수 없습니다."));
        article.update(request.getTitle(), request.getContent());
        return ArticleResponse.fromArticle(article);
    }

    public ArticleResponse read(Long articleId) {
        return ArticleResponse.fromArticle(articleRepository.findById(articleId).orElseThrow(()->new RuntimeException("글을 찾을 수 없습니다.")));
    }

    public void delete(Long articleId) {
        articleRepository.deleteById(articleId);
    }
}
