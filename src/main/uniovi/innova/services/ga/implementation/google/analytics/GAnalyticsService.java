package main.uniovi.innova.services.ga.implementation.google.analytics;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import main.uniovi.innova.services.ga.IGAService;
import main.uniovi.innova.services.ga.implementation.util.DateFormat;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.services.analytics.model.Accounts;
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.GaData.ColumnHeaders;
import com.google.api.services.analytics.model.Profiles;
import com.google.api.services.analytics.model.Webproperties;
import com.google.api.services.analytics.model.Webproperty;

/**
 * Class for getting information provided for Google Analytics
 * 
 * @author luisrodrigar - DiiSandoval
 *
 */
public abstract class GAnalyticsService implements IGAService {

	private static HttpTransport TRANSPORT;
	private static final JacksonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();

	private static final java.io.File DATA_STORE_DIR = new java.io.File(
			System.getProperty("user.home"), ".store/analytics_sample");

	private static FileDataStoreFactory dataStoreFactory;

	private static GoogleClientSecrets clientSecrets;

	private static final Collection<String> SCOPE = Collections
			.singleton(AnalyticsScopes.ANALYTICS_READONLY);
	private static final String APPLICATION_NAME = "Visits";

	// UA - the identificador was created by Google Analytics
	private String UA;

	public GAnalyticsService() {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			Reader reader = new FileReader(classLoader.getResource(
					"client_secrets.json").getFile());
			clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, reader);
		} catch (IOException e) {
			throw new Error("No client_secres.json found\n", e);
		}
	}

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
	public int numOfVisitsByDay(String id, int day, int month, int year) {
		String startDate = year + "-" + DateFormat.getStringNumber(month) + "-"
				+ DateFormat.getStringNumber(day);
		String endDate = startDate;
		return calculateVisits(startDate, endDate);
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
	public int numOfVisitsByMonth(String id, int month, int year) {
		String startDate = year + "-" + DateFormat.getStringNumber(month)
				+ "-01";
		String endDate = year
				+ "-"
				+ DateFormat.getStringNumber(month)
				+ "-"
				+ DateFormat.getStringNumber(Calendar.getInstance()
						.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calculateVisits(startDate, endDate);
	}

	/**
	 * Number of visits during a specific year
	 * 
	 * @param year
	 *            - the year to obtain the total visits
	 */
	@Override
	public int numOfVisitsByYear(String id, int year) {
		String startDate = year + "-01-01";
		String endDate = year + "-12-31";
		return calculateVisits(startDate, endDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * main.java.es.uniovi.innova.services.ga.IGAService#numOfVisitsBetweenTwoDates
	 * (java.lang.String, int, int, int, int, int, int)
	 */
	@Override
	public int numOfVisitsBetweenTwoDates(String id, int day_before,
			int month_before, int year_before, int day_after, int month_after,
			int year_after) {

		String startDate = year_before + "-"
				+ DateFormat.getStringNumber(month_before) + "-"
				+ DateFormat.getStringNumber(day_before);
		String endDate = year_after + "-"
				+ DateFormat.getStringNumber(month_after) + "-"
				+ DateFormat.getStringNumber(day_after);

		return calculateVisits(startDate, endDate);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * main.java.es.uniovi.innova.services.ga.IGAService#getVisitsByCountry(
	 * java.lang.String, int, int, int, int, int, int)
	 */
	@Override
	public Map<String, String> getVisitsByCountry(String id, int day_before,
			int month_before, int year_before, int day_after, int month_after,
			int year_after) {

		String startDate = year_before + "-"
				+ DateFormat.getStringNumber(month_before) + "-"
				+ DateFormat.getStringNumber(day_before);
		String endDate = year_after + "-"
				+ DateFormat.getStringNumber(month_after) + "-"
				+ DateFormat.getStringNumber(day_after);

		return calculateCountry(startDate, endDate);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * main.java.es.uniovi.innova.services.ga.IGAService#getVisitsBySSOO(java
	 * .lang.String, int, int, int, int, int, int)
	 */
	@Override
	public Map<String, String> getVisitsBySSOO(String id, int day_before,
			int month_before, int year_before, int day_after, int month_after,
			int year_after) {

		String startDate = year_before + "-"
				+ DateFormat.getStringNumber(month_before) + "-"
				+ DateFormat.getStringNumber(day_before);
		String endDate = year_after + "-"
				+ DateFormat.getStringNumber(month_after) + "-"
				+ DateFormat.getStringNumber(day_after);

		return calculateSSOO(startDate, endDate);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * main.java.es.uniovi.innova.services.ga.IGAService#getPageVisits(java.
	 * lang.String, int, int, int, int, int, int)
	 */
	@Override
	public Map<String, String> getPageVisits(String id, int day_before,
			int month_before, int year_before, int day_after, int month_after,
			int year_after) {

		String startDate = year_before + "-"
				+ DateFormat.getStringNumber(month_before) + "-"
				+ DateFormat.getStringNumber(day_before);
		String endDate = year_after + "-"
				+ DateFormat.getStringNumber(month_after) + "-"
				+ DateFormat.getStringNumber(day_after);

		return calculateGetPageVisits(startDate, endDate);

	}

	/**
	 * Obtain the visits of a website between start and end date
	 * 
	 * @param startDate
	 * @param endDate
	 * @return visits
	 */
	private int calculateVisits(String startDate, String endDate) {
		int visits = 0;
		try {
			TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			Analytics analytics = initializeAnalytics();
			String profileId = getProfileIdByUA(analytics, UA);
			if (profileId == null) {
				System.err.println("No profiles found.");
			} else {
				GaData gaData = executeVisitsQuery(analytics, profileId,
						startDate, endDate);
				visits = Integer.valueOf(gaData.getRows().get(0).get(0));
			}
		} catch (GoogleJsonResponseException e) {
			System.err.println("There was a service error: "
					+ e.getDetails().getCode() + " : "
					+ e.getDetails().getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return visits;
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return number of visits depends of countries
	 */
	private Map<String, String> calculateCountry(String startDate,
			String endDate) {
		Map<String, String> mapOS = new TreeMap<String, String>();
		try {
			TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			Analytics analytics = initializeAnalytics();
			String profileId = getProfileIdByUA(analytics, UA);
			if (profileId == null) {
				System.err.println("No profiles found.");
			} else {
				GaData gaData = executeCountryQuery(analytics, profileId,
						startDate, endDate);
				try {
					for (List<String> row : gaData.getRows()) {
						List<String> data = new ArrayList<String>();
						for (String colum : row) {
							data.add(colum);
						}
						mapOS.put(data.get(0), data.get(1));
					}

				} catch (NullPointerException ne) {
					System.out.println("No visits to pages");
					;
				}
			}
		} catch (GoogleJsonResponseException e) {
			System.err.println("There was a service error: "
					+ e.getDetails().getCode() + " : "
					+ e.getDetails().getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return mapOS;
	}

	/**
	 * @param startDate
	 * @param endDate
	 * @return popular pages on the site.
	 */
	private Map<String, String> calculateGetPageVisits(String startDate,
			String endDate) {
		Map<String, String> mapOS = new TreeMap<String, String>();
		try {
			TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			Analytics analytics = initializeAnalytics();
			String profileId = getProfileIdByUA(analytics, UA);
			if (profileId == null) {
				System.err.println("No profiles found.");
			} else {
				GaData gaData = executeGetPagesVisits(analytics, profileId,
						startDate, endDate);
				try {
					for (List<String> row : gaData.getRows()) {
						List<String> data = new ArrayList<String>();
						for (String colum : row) {
							data.add(colum);
						}
						mapOS.put(data.get(0), data.get(1));
					}

				} catch (NullPointerException ne) {
					System.out.println("No visits to pages");
					;
				}
			}
		} catch (GoogleJsonResponseException e) {
			System.err.println("There was a service error: "
					+ e.getDetails().getCode() + " : "
					+ e.getDetails().getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return mapOS;
	}

	/**
	 * @param startDate
	 * @param endDate
	 * @return operative systems more used
	 */
	private Map<String, String> calculateSSOO(String startDate, String endDate) {
		Map<String, String> mapOS = new TreeMap<String, String>();
		try {
			TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			Analytics analytics = initializeAnalytics();
			String profileId = getProfileIdByUA(analytics, UA);
			if (profileId == null) {
				System.err.println("No profiles found.");
			} else {
				GaData gaData = executeSSOOQuery(analytics, profileId,
						startDate, endDate);
				try {
					for (List<String> row : gaData.getRows()) {
						List<String> data = new ArrayList<String>();
						for (String colum : row) {
							data.add(colum);
						}
						mapOS.put(data.get(0), data.get(1));
					}

				} catch (NullPointerException ne) {
					System.out.println("No visits to pages");
					;
				}
			}
		} catch (GoogleJsonResponseException e) {
			System.err.println("There was a service error: "
					+ e.getDetails().getCode() + " : "
					+ e.getDetails().getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return mapOS;
	}

	/**
	 * Task about authorization on Google Analytics
	 * 
	 * @return Credentials
	 * @throws IOException
	 */
	private static Credential authorize() throws IOException {
		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				TRANSPORT, JSON_FACTORY, clientSecrets, SCOPE)
				.setDataStoreFactory(dataStoreFactory).build();
		// authorize
		return new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver()).authorize("user");
	}

	/**
	 * Authorizacition and connect to Google Analytics API
	 * 
	 * @return Analytics object
	 * @throws Exception
	 */
	private static Analytics initializeAnalytics() throws Exception {
		// Authorization.
		Credential credential = authorize();

		// Set up and return Google Analytics API client.
		return new Analytics.Builder(TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}

	/**
	 * Obtain the profile of the google analytics account
	 * 
	 * @param Analytics
	 * @param Google
	 *            Analytics id e.i. UA-XXXXX-Y
	 * @return Profile
	 * @throws IOException
	 */
	private static String getProfileIdByUA(Analytics analytics, String ua)
			throws IOException {
		String profileId = null;

		// Query accounts collection.
		Accounts accounts = analytics.management().accounts().list().execute();

		if (accounts.getItems().isEmpty()) {
			System.err.println("No accounts found");
		} else {
			String firstAccountId = accounts.getItems().get(0).getId();

			// Query webproperties collection.
			Webproperties webproperties = analytics.management()
					.webproperties().list(firstAccountId).execute();

			if (webproperties.getItems().isEmpty()) {
				System.err.println("No Webproperties found");
			} else {
				String webpropertyId = "";
				for (Webproperty each : webproperties.getItems())
					if (each.getId().equals(ua))
						webpropertyId = each.getId();

				// Query profiles collection.
				Profiles profiles = analytics.management().profiles()
						.list(firstAccountId, webpropertyId).execute();

				if (profiles.getItems().isEmpty()) {
					System.err.println("No profiles found");
				} else {
					profileId = profiles.getItems().get(0).getId();
				}
			}
		}
		return profileId;
	}

	/**
	 * Query to execute for getting the info about the website
	 * 
	 * @param analytics
	 *            - authorization and credentials
	 * @param profileId
	 *            - profile about the website to obtain the visits
	 * @param startDate
	 *            - initial date for starting to count the visits
	 * @param endDate
	 *            - end date for finishing to count the visits
	 * @return the info about the visits of the profile
	 * @throws IOException
	 */
	private static GaData executeVisitsQuery(Analytics analytics,
			String profileId, String startDate, String endDate)
			throws IOException {
		return analytics.data().ga().get("ga:" + profileId, // Table Id. ga: +
															// profile id.
				startDate, // Start date.
				endDate, // End date.
				"ga:visits") // Metrics.
				.execute();
	}

	/**
	 * Query to execute for getting the info about the website
	 * 
	 * @param analytics
	 *            - authorization and credentials
	 * @param profileId
	 *            - profile about the website to obtain the visits
	 * @param startDate
	 *            - initial date for starting to count the visits
	 * @param endDate
	 *            - end date for finishing to count the visits
	 * @return the info about the visits of the profile
	 * @throws IOException
	 */
	private static GaData executeCountryQuery(Analytics analytics,
			String profileId, String startDate, String endDate)
			throws IOException {
		return analytics.data().ga().get("ga:" + profileId, // Table Id. ga: +
				// profile id.
				startDate, endDate, "ga:visits").setDimensions("ga:country")
				.setSort("ga:visits").execute(); // Metrics.

	}

	/**
	 * @param analytics
	 * @param profileId
	 * @param startDate
	 * @param endDate
	 * @return Execute operative system query
	 * @throws IOException
	 */
	private static GaData executeSSOOQuery(Analytics analytics,
			String profileId, String startDate, String endDate)
			throws IOException {
		return analytics.data().ga().get("ga:" + profileId, // Table Id. ga: +
				// profile id.
				startDate, endDate, "ga:visits")
				.setDimensions("ga:operatingSystemVersion")
				.setSort("ga:visits").execute(); // Metrics.

	}

	/**
	 * @param analytics
	 * @param profileId
	 * @param startDate
	 * @param endDate
	 * @return Execute popular sites query
	 * @throws IOException
	 */
	private static GaData executeGetPagesVisits(Analytics analytics,
			String profileId, String startDate, String endDate)
			throws IOException {
		return analytics.data().ga().get("ga:" + profileId, // Table Id. ga: +
															// profile id.
				startDate, // Start date.
				endDate, // End date.
				"ga:visits") // Metrics.
				.setDimensions("ga:pagePath").setSort("ga:visits").execute();
	}

	/**
	 * Show the data about the name profile and the visits associated
	 * 
	 * @param data
	 *            about Google Analytics profile
	 */
	@SuppressWarnings("unused")
	private static void printGaData(GaData results) {
		// Print info about the Google Analytics profile
		System.out.println("printing results for profile: "
				+ results.getProfileInfo().getProfileName());
		System.out.println("printing results for web profile propery: "
				+ results.getProfileInfo().getWebPropertyId());
		System.out.println("printing results for account id: "
				+ results.getProfileInfo().getAccountId());

		if (results.getRows() == null || results.getRows().isEmpty()) {
			System.out.println("No results Found.");
		} else {

			// Print column headers, in this case, the visits
			for (ColumnHeaders header : results.getColumnHeaders()) {
				System.out.printf("%30s", header.getName());
			}
			System.out.println();

			// Print the visits of the website
			for (List<String> row : results.getRows()) {
				for (String column : row) {
					System.out.printf("%30s", column);
				}
				System.out.println();
			}

			System.out.println();
		}
	}

	/**
	 * The Google Analytics ID for querying the visits of the website which has
	 * this ID
	 * 
	 * @return Google
	 */
	public String getUA() {
		return UA;
	}

	/**
	 * Allow to change the Google Analytics ID for querying visits of other web
	 * site.
	 * 
	 * @param uA
	 */
	public void setUA(String uA) {
		UA = uA;
	}

}
