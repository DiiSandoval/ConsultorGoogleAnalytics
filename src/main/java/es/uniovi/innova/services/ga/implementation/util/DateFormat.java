package main.java.es.uniovi.innova.services.ga.implementation.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

	public static boolean isFechaActual(int day_before, int month_before,
			int year_before, int day_after, int month_after, int year_after) {
		String startDate = year_before + "-" + getStringNumber(month_before)
				+ "-" + getStringNumber(day_before);
		String endDate = year_after + "-" + getStringNumber(month_after) + "-"
				+ getStringNumber(day_after);
		Date fechaActual = new Date();
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		String fechaNow = formater.format(fechaActual);

		try {
			fechaActual = formater.parse(fechaNow);
			Date fecha1 = formater.parse(startDate);
			Date fecha2 = formater.parse(endDate);

			if (fechaActual.before(fecha2) && fechaActual.after(fecha1)) {
				return true;
			}
			return false;

		} catch (ParseException e) {
			e.printStackTrace();
		}


		return false;

	}
	
	/**
	 * If number less than 10, it will return: 0'number' If number more than 9,
	 * the response is the number
	 * 
	 * @param number
	 *            for checking
	 * @return number checked
	 */
	public static String getStringNumber(int number) {
		return (String) (number < 10 ? "0" + number : "" + number);
	}

	
	
}
