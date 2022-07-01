package com.triple.mileage.point.service.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

class PointAdditionCommandTest {

    @DisplayName("텍스트와 첨부사진이 모두 없는 경우 예외를 던진다.")
    @ParameterizedTest(name = "content: {0}")
    @NullAndEmptySource
    void checkValidReview(String content) {
        List<UUID> photoIds = Collections.emptyList();
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> new PointAdditionCommand(randomUUID(), photoIds, randomUUID(), content, randomUUID()));
    }

    private static UUID randomUUID() {
        return UUID.randomUUID();
    }

}
