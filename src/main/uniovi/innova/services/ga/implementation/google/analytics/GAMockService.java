package main.uniovi.innova.services.ga.implementation.google.analytics;

import java.util.Map;

import main.uniovi.innova.services.ga.IGAService;

public class GAMockService implements IGAService {

	@Override
	public int numOfVisitsByDay(String id, int day, int month, int year) {
		return (int) Math.random() * 250;
	}

	@Override
	public int numOfVisitsByMonth(String id, int month, int year) {
		return (int) Math.random() * 10000;
	}

	@Override
	public int numOfVisitsByYear(String id, int year) {
		return (int) Math.random() * 10000000;
	}

	@Override
	public void setUA(String uA) {
	}

	@Override
	public String getUA() {
		return "";
	}

	@Override
	public int numOfVisitsBetweenTwoDates(String id, int day_before, int month_before,
			int year_before, int day_after, int month_after, int year_after) {
		return (int) Math.random() * 10000000;
	}

	@Override
	public Map<String, String> getVisitsByCountry(String id, int day_before,
			int month_before, int year_before, int day_after, int month_after,
			int year_after) {
		return null;
	}
	@Override
	public Map<String, String> getVisitsBySSOO(String id, int day_before,
			int month_before, int year_before, int day_after, int month_after,
			int year_after) {
		return null;
	}

	@Override
	public Map<String, String> getPageVisits(String id, int day_before, int month_before,
			int year_before, int day_after, int month_after, int year_after) {
		return null;
	}
	

}
