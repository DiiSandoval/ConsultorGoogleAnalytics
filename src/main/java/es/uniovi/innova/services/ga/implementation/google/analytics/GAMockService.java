package main.java.es.uniovi.innova.services.ga.implementation.google.analytics;

import java.util.Map;

import main.java.es.uniovi.innova.services.ga.IGAService;

public class GAMockService implements IGAService {

	@Override
	public int numOfVisitsByDay(int day, int month, int year) {
		return (int) Math.random() * 250;
	}

	@Override
	public int numOfVisitsByMonth(int month, int year) {
		return (int) Math.random() * 10000;
	}

	@Override
	public int numOfVisitsByYear(int year) {
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
	public int numOfVisitsBetweenTwoDates(int day_before, int month_before,
			int year_before, int day_after, int month_after, int year_after) {
		return (int) Math.random() * 10000000;
	}

	@Override
	public Map<String, String> getVisitsByCountry(int day_before,
			int month_before, int year_before, int day_after, int month_after,
			int year_after) {
		return null;
	}
	@Override
	public Map<String, String> getVisitsBySSOO(int day_before,
			int month_before, int year_before, int day_after, int month_after,
			int year_after) {
		return null;
	}

	@Override
	public Map<String, String> getPageVisits(int day_before, int month_before,
			int year_before, int day_after, int month_after, int year_after) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
