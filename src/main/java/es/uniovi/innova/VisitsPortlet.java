package main.java.es.uniovi.innova;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import main.java.es.uniovi.innova.factories.Factory;
import main.java.es.uniovi.innova.services.ga.IGAService;
import main.java.es.uniovi.innova.services.ga.IPortalesService;
import main.java.es.uniovi.innova.services.ga.implementation.util.DateFormat;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.liferay.portal.model.Group;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;

public class VisitsPortlet extends GenericPortlet {

	private IGAService gaServiceNewData;
	private IGAService gaServiceOldData;
	private IPortalesService portalService;
	private BeanFactory factory;
	private Factory factoryService;
	private Map<String, String> mapPortal;

	@Override
	public void init() {
		mapPortal = new HashMap<String, String>();
		factory = new ClassPathXmlApplicationContext("beans.xml");
		factoryService = (Factory) factory.getBean("factory");
		gaServiceNewData = factoryService.getServiceGoogleAnalyticsNewData();
		gaServiceOldData = factoryService.getServiceGoogleAnalyticsOldData();
		setPortalService(factoryService.getServicePortales());
	}

	@Override
	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {

		ThemeDisplay themeDisplay = (ThemeDisplay) request
				.getAttribute(WebKeys.THEME_DISPLAY);
		mapPortal = portalService.getPortalesScope(themeDisplay);
		request.setAttribute("mapPortal", mapPortal);

		System.out.println("--El UA del portal es: "
				+ mapPortal.get("idGoogleAnalytics"));
		String id = mapPortal.get("idGoogleAnalytics");
		if (id != null) {
			gaServiceNewData.setUA(id);
			request.setAttribute("numVisitasDay",
					gaServiceNewData.numOfVisitsByDay(6, 1, 2015));
			request.setAttribute("numVisitasMonth",
					gaServiceNewData.numOfVisitsByMonth(12, 2014));
			request.setAttribute("numVisitasYear",
					gaServiceNewData.numOfVisitsByYear(2015));
		}else{
			System.out.println("ME METO POR EL NUUUUUUUUUUUUUUUUUULLLLLLL");
			request.setAttribute("mensaje",
					"No tienes ningún portlet.");
		}
		
		include("/html/view.jsp", request, response);
	}

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
		if(id!=null){
		gaService.setUA(mapPortal.get("idGoogleAnalytics"));

		actionrequest.setAttribute("numVisitasIntervalo", gaService
				.numOfVisitsBetweenTwoDates(dayStart, monthStart, yearStart,
						dayEnd, monthEnd, yearEnd));
		actionrequest.setAttribute("mapCountry", gaService.getVisitsByCountry(
				dayStart, monthStart, yearStart, dayEnd, monthEnd, yearEnd));
		actionrequest.setAttribute("mapSO", gaService.getVisitsBySSOO(dayStart,
				monthStart, yearStart, dayEnd, monthEnd, yearEnd));
		actionrequest.setAttribute("mapPages", gaService.getPageVisits(
				dayStart, monthStart, yearStart, dayEnd, monthEnd, yearEnd));
		}else{
			actionrequest.setAttribute("mensaje",
					"No tienes ningún portlet.");
		}
	}

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

	protected void include(String path, RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {

		PortletRequestDispatcher portletRequestDispatcher = getPortletContext()
				.getRequestDispatcher(path);

		if (portletRequestDispatcher == null) {
			System.err.println(path + " is not a valid include");
		} else {
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}
	}

	public IPortalesService getPortalService() {
		return portalService;
	}

	public void setPortalService(IPortalesService portalService) {
		this.portalService = portalService;
	}

}
