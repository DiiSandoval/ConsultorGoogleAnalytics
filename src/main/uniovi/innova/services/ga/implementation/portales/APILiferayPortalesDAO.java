package main.uniovi.innova.services.ga.implementation.portales;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import com.liferay.portal.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;

import main.uniovi.innova.services.ga.IPortalesService;

public class APILiferayPortalesDAO implements IPortalesService {

	final static Logger log = LoggerFactory
			.getLogger(APILiferayPortalesDAO.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * main.java.es.uniovi.innova.services.ga.IPortalesService#getPortales()
	 */
	@Override
	@Cacheable(value = "portalCache", key = "#root.methodName")
	public Map<String, String> getPortales() {
		Map<String, String> mapPortal = new HashMap<String, String>();
		List<Group> listaGrupos;
		try {
			listaGrupos = GroupLocalServiceUtil.getGroups(0,
					GroupLocalServiceUtil.getGroupsCount());
			for (Group group : listaGrupos) {
				String name = group.getDescriptiveName();
				String idGoogleAnalytics = group.getTypeSettingsProperties()
						.getProperty("googleAnalyticsId");
				if (idGoogleAnalytics != null)
					mapPortal.put(name, idGoogleAnalytics);
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		log.info("Portals have been stored succesfully!");
		return mapPortal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * main.java.es.uniovi.innova.services.ga.IPortalesService#getPortalesScope
	 * (com.liferay.portal.theme.ThemeDisplay)
	 */
	@Override
	public Map<String, String> getPortalesScope(ThemeDisplay themeDisplay) {
		Map<String, String> mapPortal = new HashMap<String, String>();
		Group group = themeDisplay.getScopeGroup();
		mapPortal.put("name", group.getDescriptiveName());
		mapPortal.put("idGoogleAnalytics", group.getTypeSettingsProperties()
				.getProperty("googleAnalyticsId"));
		log.info("Portals with your scope have been stored succesfully!");
		return mapPortal;
	}

}
