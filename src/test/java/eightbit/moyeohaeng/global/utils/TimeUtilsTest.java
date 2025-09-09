package eightbit.moyeohaeng.global.utils;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TimeUtilsTest {

    @Nested
    @DisplayName("여행 일차 계산 테스트")
    class CalculateTravelDaysTest {

        @Test
        @DisplayName("정상적인 여행 일차 계산")
        void calculateTravelDays_Success() {
            // given
            LocalDate startDate = LocalDate.of(2025, 10, 1);
            LocalDate endDate = LocalDate.of(2025, 10, 7);

            // when
            Integer travelDays = TimeUtils.calculateTravelDays(startDate, endDate);

            // then
            assertThat(travelDays).isEqualTo(7); // 시작일과 종료일 포함
        }

        @Test
        @DisplayName("시작일과 종료일이 같은 경우")
        void calculateTravelDays_SameDay() {
            // given
            LocalDate date = LocalDate.of(2025, 10, 1);

            // when
            Integer travelDays = TimeUtils.calculateTravelDays(date, date);

            // then
            assertThat(travelDays).isEqualTo(1); // 하루 여행
        }

        @Test
        @DisplayName("시작일이 null인 경우")
        void calculateTravelDays_NullStartDate() {
            // given
            LocalDate endDate = LocalDate.of(2025, 10, 7);

            // when
            Integer travelDays = TimeUtils.calculateTravelDays(null, endDate);

            // then
            assertThat(travelDays).isNull();
        }

        @Test
        @DisplayName("종료일이 null인 경우")
        void calculateTravelDays_NullEndDate() {
            // given
            LocalDate startDate = LocalDate.of(2025, 10, 1);

            // when
            Integer travelDays = TimeUtils.calculateTravelDays(startDate, null);

            // then
            assertThat(travelDays).isNull();
        }
    }

    @Nested
    @DisplayName("날짜 범위 검증 테스트")
    class ValidateDateRangeTest {

        @Test
        @DisplayName("정상적인 날짜 범위")
        void validateDateRange_Success() {
            // given
            LocalDate startDate = LocalDate.of(2025, 10, 1);
            LocalDate endDate = LocalDate.of(2025, 10, 7);

            // when & then
            assertDoesNotThrow(() -> TimeUtils.validateDateRange(startDate, endDate));
        }

        @Test
        @DisplayName("종료일이 시작일보다 이전인 경우")
        void validateDateRange_EndBeforeStart() {
            // given
            LocalDate startDate = LocalDate.of(2025, 10, 7);
            LocalDate endDate = LocalDate.of(2025, 10, 1);

            // when & then
            assertThatThrownBy(() -> TimeUtils.validateDateRange(startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("종료 날짜는 시작 날짜보다 이후여야 합니다.");
        }

        @Test
        @DisplayName("시작일이 null인 경우")
        void validateDateRange_NullStartDate() {
            // given
            LocalDate endDate = LocalDate.of(2025, 10, 7);

            // when & then
            assertDoesNotThrow(() -> TimeUtils.validateDateRange(null, endDate));
        }

        @Test
        @DisplayName("종료일이 null인 경우")
        void validateDateRange_NullEndDate() {
            // given
            LocalDate startDate = LocalDate.of(2025, 10, 1);

            // when & then
            assertDoesNotThrow(() -> TimeUtils.validateDateRange(startDate, null));
        }
    }
}
