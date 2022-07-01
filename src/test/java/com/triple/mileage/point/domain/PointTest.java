package com.triple.mileage.point.domain;

import com.triple.mileage.common.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PointTest {

    private static final UUID USER_ID = UUID.randomUUID();

    @DisplayName("포인트 합")
    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
            "1, 2, 3",
            "7, -3, 4"
    })
    void sum(int origin, int operand, int expected) {
        // given
        Point point = new Point(USER_ID, origin);

        // when
        point.sum(operand);

        // then
        assertThat(point.getValue()).isEqualTo(expected);
    }

    @DisplayName("포인트가 음수라면 예외를 던진다.")
    @ParameterizedTest(name = "value = {0}")
    @ValueSource(ints = {-100, -1})
    void pointIsZeroOrMore(int value) {
        Assertions.assertThatExceptionOfType(PointException.class)
                .isThrownBy(() -> new Point(USER_ID, value))
                .matches(e -> e.getErrorCode() == ErrorCode.NEGATIVE_ACCUMULATED_POINTS);
    }

}
