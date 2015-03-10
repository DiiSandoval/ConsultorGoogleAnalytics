package main.java.es.uniovi.innova.services.ga;

import java.util.Map;

public interface IGAService {
	int numOfVisitsByDay(int day, int month, int year);

	int numOfVisitsByMonth(int month, int year);

	int numOfVisitsByYear(int year);

	int numOfVisitsBetweenTwoDates(int day_before, int month_before,
			int year_before, int day_after, int month_after, int year_after);

	void setUA(String uA);
	
	Map<String, String> getVisitsByCountry(int day_before, int month_before,
			int year_before, int day_after, int month_after, int year_after);
	
	Map<String, String> getVisitsBySSOO(int day_before, int month_before,
			int year_before, int day_after, int month_after, int year_after);

	String getUA();

	Map<String, String> getPageVisits(int day_before, int month_before,
			int year_before, int day_after, int month_after, int year_after);
}
