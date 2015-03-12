package main.uniovi.innova;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.uniovi.innova.services.ga.IGAService;
import main.uniovi.innova.services.ga.IPortalesService;
import main.uniovi.innova.services.ga.implementation.portales.APILiferayPortalesDAO;
import main.uniovi.innova.services.ga.implementation.util.DateFormat;

import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;

public class VisitsScopePortlet extends VisitsPortlet {

	private Map<String, String> mapPortal = new HashMap<String, String>();
	
	final static Logger log = LoggerFactory
			.getLogger(APILiferayPortalesDAO.class);

	/* (non-Javadoc)
	 * @see main.java.es.uniovi.innova.VisitsPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	@Override
	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {

		ThemeDisplay themeDisplay = (ThemeDisplay) request
				.getAttribute(WebKeys.THEME_DISPLAY);
		mapPortal = portalService.getPortalesScope(themeDisplay);
		request.setAttribute("mapPortal", mapPortal);
		
		String id = mapPortal.get("idGoogleAnalytics");
		gaServiceNewData.setUA(id);
		request.setAttribute("namePortal", mapPortal.get("name"));
		include("/html/viewScope.jsp", request, response);

	}

	/* (non-Javadoc)
	 * @see main.java.es.uniovi.innova.VisitsPortlet#processAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse)
	 */
	@Override
	public void processAction(ActionRequest actionrequest,
			ActionResponse actionResponse) {

		int monthStart = Integer.parseInt(actionrequest
				.getParameter("month_start"));
		int dayStart = Integer
				.parseInt(actionrequest.getParameter("day_start"));
		int yearStart = Integer.parseInt(actionrequest
				.getParameter("year_start"));
		int monthEnd = Integer
				.parseInt(actionrequest.getParameter("month_end"));
		int dayEnd = Integer.parseInt(actionrequest.getParameter("day_end"));
		int yearEnd = Integer.parseInt(actionrequest.getParameter("year_end"));

		IGAService gaService = defineService(monthStart, dayStart, yearStart,
				monthEnd, dayEnd, yearEnd);

		String id = mapPortal.get("idGoogleAnalytics");
		gaService.setUA(mapPortal.get("idGoogleAnalytics"));

		actionrequest.setAttribute("numVisitasIntervalo", gaService
				.numOfVisitsBetweenTwoDates(id, dayStart, monthStart,
						yearStart, dayEnd, monthEnd, yearEnd));
		actionrequest
				.setAttribute("mapCountry", gaService.getVisitsByCountry(id,
						dayStart, monthStart, yearStart, dayEnd, monthEnd,
						yearEnd));
		actionrequest.setAttribute("mapSO", gaService.getVisitsBySSOO(id,
				dayStart, monthStart, yearStart, dayEnd, monthEnd, yearEnd));
		actionrequest.setAttribute("mapPages", gaService.getPageVisits(id,
				dayStart, monthStart, yearStart, dayEnd, monthEnd, yearEnd));

	}

	/**
	 * @param monthStart
	 * @param dayStart
	 * @param yearStart
	 * @param monthEnd
	 * @param dayEnd
	 * @param yearEnd
	 * @return the service to use depends of the kind of cache you want to use
	 */
	private IGAService defineService(int monthStart, int dayStart,
			int yearStart, int monthEnd, int dayEnd, int yearEnd) {
		IGAService gaService;
		if (DateFormat.isFechaActual(dayStart, monthStart, yearStart, dayEnd,
				monthEnd, yearEnd))
			gaService = gaServiceNewData;
		else
			gaService = gaServiceOldData;
		
		return gaService;
	}

	/* (non-Javadoc)
	 * @see main.java.es.uniovi.innova.VisitsPortlet#include(java.lang.String, javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	protected void include(String path, RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {

		PortletRequestDispatcher portletRequestDispatcher = getPortletContext()
				.getRequestDispatcher(path);

		if (portletRequestDispatcher == null) {
			log.error(path + " is not a valid include");
		} else {
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}
	}

	/**
	 * @return portalService
	 */
	public IPortalesService getPortalService() {
		return portalService;
	}

	/**
	 * @param portalService
	 */
	public void setPortalService(IPortalesService portalService) {
		this.portalService = portalService;
	}

}
