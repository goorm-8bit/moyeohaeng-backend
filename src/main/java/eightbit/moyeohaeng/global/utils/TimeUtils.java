package eightbit.moyeohaeng.global.utils;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeUtils {

	static public void validateTimeRange(LocalTime startTime, LocalTime endTime) {
		if (startTime == null || endTime == null) {
			return;
		}
		if (!startTime.isBefore(endTime)) {
			throw new IllegalArgumentException("종료 시간은 시작 시간보다 이후여야 합니다.");
		}
	}

	static public void validateDateRange(LocalDate startDate, LocalDate endDate) {
		if (startDate == null || endDate == null) {
			return;
		}
		if (!startDate.isBefore(endDate)) {
			throw new IllegalArgumentException("종료 날짜는 시작 날짜보다 이후여야 합니다.");
		}
	}
}
