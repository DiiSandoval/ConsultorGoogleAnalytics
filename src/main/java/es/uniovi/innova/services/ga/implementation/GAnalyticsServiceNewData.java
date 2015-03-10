package main.java.es.uniovi.innova.services.ga.implementation;

import java.util.Map;

import main.java.es.uniovi.innova.services.ga.implementation.google.analytics.GAnalyticsService;

import org.springframework.cache.annotation.Cacheable;

/**
 * Class for getting information provided for Google Analytics
 * 
 * @author luisrodrigar - DiiSandoval
 *
 */
public class GAnalyticsServiceNewData extends GAnalyticsService {

	/**
	 * Number of visits in one specific day
	 * 
	 * @param day
	 *            - the day of a month
	 * @param month
	 *            - the month of a year
	 * @param year
	 *            - the concrete year
	 */
	@Override
	@Cacheable(value = "actualVisits", key = "#root.methodName.concat(#day.toString()).concat(#month.toString()).concat(#year.toString())")
	public int numOfVisitsByDay(int day, int month, int year) {
		System.out.println("> Caché variable - Numero de visitas en un día");
		return super.numOfVisitsByDay(day, month, year);
	}

	/**
	 * Number of visits during a specific month
	 * 
	 * @param month
	 *            - the mont of a year
	 * @param year
	 *            - the concrete year
	 */
	@Override
	@Cacheable(value = "actualVisits", key = "#root.methodName.concat(#month.toString()).concat(#year.toString())")
	public int numOfVisitsByMonth(int month, int year) {
		System.out.println("> Caché variable - Numero de visitas en un mes");
		return super.numOfVisitsByMonth(month, year);
	}
	/**
	 * Number of visits during a specific year
	 * 
	 * @param year
	 *            - the year to obtain the total visits
	 */
	@Override
	@Cacheable(value = "actualVisits", key = "#root.methodName.concat(#year.toString())")
	public int numOfVisitsByYear(int year) {
		System.out.println("> Caché variable - Numero de visitas en un año");
		return super.numOfVisitsByYear(year);
	}

	/* (non-Javadoc)
	 * @see main.java.es.uniovi.innova.services.ga.implementation.google.analytics.GAnalyticsService#numOfVisitsBetweenTwoDates(int, int, int, int, int, int)
	 */
	@Override
	@Cacheable(value = "actualVisits", key = "#root.methodName.concat(#day_before.toString()).concat(#month_before.toString())"
			+ ".concat(#year_before.toString()).concat(#day_after.toString()).concat(#month_after.toString()).concat(#year_after.toString())")
	public int numOfVisitsBetweenTwoDates(int day_before, int month_before,
			int year_before, int day_after, int month_after, int year_after) {
		System.out
				.println("> Caché variable - Numero de visitas entre dos fechas");
		return super.numOfVisitsBetweenTwoDates(day_before, month_before,
				year_before, day_after, month_after, year_after);

	}

	/* (non-Javadoc)
	 * @see main.java.es.uniovi.innova.services.ga.implementation.google.analytics.GAnalyticsService#getVisitsByCountry(int, int, int, int, int, int)
	 */
	@Override
	@Cacheable(value = "actualVisits", key = "#root.methodName.concat(#day_before.toString()).concat(#month_before.toString())"
			+ ".concat(#year_before.toString()).concat(#day_after.toString()).concat(#month_after.toString()).concat(#year_after.toString())")
	public Map<String, String> getVisitsByCountry(int day_before,
			int month_before, int year_before, int day_after, int month_after,
			int year_after) {
		System.out.println("> Caché variable - Numero de visitas por país");
		return super.getVisitsByCountry(day_before, month_before, year_before,
				day_after, month_after, year_after);

	}

	/* (non-Javadoc)
	 * @see main.java.es.uniovi.innova.services.ga.implementation.google.analytics.GAnalyticsService#getVisitsBySSOO(int, int, int, int, int, int)
	 */
	@Override
	@Cacheable(value = "actualVisits", key = "#root.methodName.concat(#day_before.toString()).concat(#month_before.toString())"
			+ ".concat(#year_before.toString()).concat(#day_after.toString()).concat(#month_after.toString()).concat(#year_after.toString())")
	public Map<String, String> getVisitsBySSOO(int day_before,
			int month_before, int year_before, int day_after, int month_after,
			int year_after) {
		System.out
				.println("> Caché variable - Numero de visitas por sistema operativo");
		return super.getVisitsBySSOO(day_before, month_before, year_before,
				day_after, month_after, year_after);

	}

	/* (non-Javadoc)
	 * @see main.java.es.uniovi.innova.services.ga.implementation.google.analytics.GAnalyticsService#getPageVisits(int, int, int, int, int, int)
	 */
	@Override
	@Cacheable(value = "actualVisits", key = "#root.methodName.concat(#day_before.toString()).concat(#month_before.toString())"
			+ ".concat(#year_before.toString()).concat(#day_after.toString()).concat(#month_after.toString()).concat(#year_after.toString())")
	public Map<String, String> getPageVisits(int day_before, int month_before,
			int year_before, int day_after, int month_after, int year_after) {
		System.out.println("> Caché variable - Numero de visitas por pagina");
		return super.getPageVisits(day_before, month_before, year_before,
				day_after, month_after, year_after);

	}

}
