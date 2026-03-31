package org.ganjp.api.core.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PaginatedResponseTest {

    @Test
    void should_createPaginatedResponse_withCorrectMetadata() {
        // Given
        List<String> items = List.of("a", "b", "c");
        
        // When
        PaginatedResponse<String> response = PaginatedResponse.of(items, 1, 20, 100);

        // Then
        assertThat(response.getContent()).hasSize(3);
        assertThat(response.getPage()).isEqualTo(1);
        assertThat(response.getSize()).isEqualTo(20);
        assertThat(response.getTotalElements()).isEqualTo(100);
        assertThat(response.getTotalPages()).isEqualTo(5); // 100 / 20
    }

    @Test
    void should_calculateTotalPages_when_itemsMissingOne() {
        // Given
        List<String> items = List.of("a");
        
        // When
        PaginatedResponse<String> response = PaginatedResponse.of(items, 0, 20, 21);

        // Then
        assertThat(response.getTotalPages()).isEqualTo(2);
    }

    @Test
    void should_returnZeroPages_when_totalElementsZero() {
        // When
        PaginatedResponse<String> response = PaginatedResponse.of(List.of(), 0, 20, 0);

        // Then
        assertThat(response.getTotalPages()).isZero();
    }
}
