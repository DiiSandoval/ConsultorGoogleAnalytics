package main.java.es.uniovi.innova.services.ga;

import java.util.Map;

public interface IGAService {
	int numOfVisitsByDay(String id, int day, int month, int year);

	int numOfVisitsByMonth(String id, int month, int year);

	int numOfVisitsByYear(String id, int year);

	int numOfVisitsBetweenTwoDates(String id, int day_before, int month_before,
			int year_before, int day_after, int month_after, int year_after);

	void setUA(String uA);
	
	Map<String, String> getVisitsByCountry(String id, int day_before, int month_before,
			int year_before, int day_after, int month_after, int year_after);
	
	Map<String, String> getVisitsBySSOO(String id, int day_before, int month_before,
			int year_before, int day_after, int month_after, int year_after);

	String getUA();

	Map<String, String> getPageVisits(String id, int day_before, int month_before,
			int year_before, int day_after, int month_after, int year_after);
}
