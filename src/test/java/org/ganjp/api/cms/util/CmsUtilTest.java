package org.ganjp.api.cms.util;

import org.ganjp.api.cms.article.Article;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CmsUtilTest {

    @Test
    void should_parseLanguage_when_validString() {
        // When
        Article.Language lang = CmsUtil.parseLanguage("EN", Article.Language.class);

        // Then
        assertThat(lang).isEqualTo(Article.Language.EN);
    }

    @Test
    void should_returnNull_when_invalidLanguageString() {
        // When
        Article.Language lang = CmsUtil.parseLanguage("INVALID", Article.Language.class);

        // Then
        assertThat(lang).isNull();
    }

    @Test
    void should_returnNull_when_blankLanguageString() {
        // When
        assertThat(CmsUtil.parseLanguage(null, Article.Language.class)).isNull();
        assertThat(CmsUtil.parseLanguage("", Article.Language.class)).isNull();
    }

    @Test
    void should_throwException_when_filenameIsInvalid() {
        // Then
        assertThatThrownBy(() -> CmsUtil.validateFilename("../etc/passwd"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CmsUtil.validateFilename("dir/file.txt"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void should_notThrow_when_filenameIsValid() {
        // When & Then
        CmsUtil.validateFilename("image.png");
        CmsUtil.validateFilename("document-2024.pdf");
    }

    @Test
    void should_buildPageable_withCorrectSort() {
        // When
        Pageable pageable = CmsUtil.buildPageable(1, 10, "title", "desc");

        // Then
        assertThat(pageable.getPageNumber()).isEqualTo(1);
        assertThat(pageable.getPageSize()).isEqualTo(10);
        Sort.Order order = pageable.getSort().getOrderFor("title");
        assertThat(order).isNotNull();
        assertThat(order.getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void should_joinBaseAndPath_correctly() {
        // Case 1: Base and path both have no slash
        assertThat(CmsUtil.joinBaseAndPath("http://base", "file.png")).isEqualTo("http://base/file.png");
        
        // Case 2: Base ends with slash
        assertThat(CmsUtil.joinBaseAndPath("http://base/", "file.png")).isEqualTo("http://base/file.png");
        
        // Case 3: Path starts with slash
        assertThat(CmsUtil.joinBaseAndPath("http://base", "/file.png")).isEqualTo("http://base/file.png");
        
        // Case 4: Both have slash
        assertThat(CmsUtil.joinBaseAndPath("http://base/", "/file.png")).isEqualTo("http://base/file.png");
    }

    @Test
    void should_determineContentType_correctly() {
        assertThat(CmsUtil.determineContentType("t.png")).isEqualTo("image/png");
        assertThat(CmsUtil.determineContentType("t.pdf")).isEqualTo("application/pdf");
        assertThat(CmsUtil.determineContentType("t.mp4")).isEqualTo("video/mp4");
        assertThat(CmsUtil.determineContentType("t.mp3")).isEqualTo("audio/mpeg");
        assertThat(CmsUtil.determineContentType("unknown")).isEqualTo("application/octet-stream");
    }
}
