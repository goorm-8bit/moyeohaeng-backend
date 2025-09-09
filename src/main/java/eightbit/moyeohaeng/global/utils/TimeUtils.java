package eightbit.moyeohaeng.global.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

	public static Integer calculateTravelDays(LocalDate startDate, LocalDate endDate) {
		if (startDate == null || endDate == null) {
			return null;
		}
		long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
		if (days < 1) {
			throw new IllegalArgumentException("종료 날짜는 시작 날짜보다 같거나 이후여야 합니다.");
		}
		return (int)days;
	}

	public static void validateTimeRange(LocalTime startTime, LocalTime endTime) {
		if (startTime == null || endTime == null) {
			return;
		}
		if (!startTime.isBefore(endTime)) {
			throw new IllegalArgumentException("종료 시간은 시작 시간보다 이후여야 합니다.");
		}
	}

	public static void validateDateRange(LocalDate startDate, LocalDate endDate) {
		if (startDate == null || endDate == null) {
			return;
		}
		if (!startDate.isBefore(endDate)) {
			throw new IllegalArgumentException("종료 날짜는 시작 날짜보다 이후여야 합니다.");
		}
	}
}
