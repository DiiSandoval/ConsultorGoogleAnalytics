package main.java.es.uniovi.innova.services.ga.implementation.portales;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liferay.portal.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;

import main.java.es.uniovi.innova.services.ga.IPortalesService;

public class APILiferayPortalesDAO implements IPortalesService {

	@Override
	public Map<String, String> getPortales() {
		Map<String, String> mapPortal = new HashMap<String, String>();
		List<Group> listaGrupos;
		try {
			listaGrupos = GroupLocalServiceUtil.getGroups(0,GroupLocalServiceUtil.getGroupsCount());
			for (Group group: listaGrupos) {
				String name = group.getDescriptiveName();
				String idGoogleAnalytics = group.getTypeSettingsProperties().getProperty("googleAnalyticsId");
				if(idGoogleAnalytics!=null)
					mapPortal.put(name, idGoogleAnalytics);
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return mapPortal;
	}

	@Override
	public Map<String, String> getPortalesScope(ThemeDisplay themeDisplay) {
		Map<String, String> mapPortal = new HashMap<String, String>();
		Group group = themeDisplay.getScopeGroup();
		mapPortal.put("idGoogleAnalytics", group.getTypeSettingsProperties().getProperty("googleAnalyticsId"));
		return mapPortal;
	}

}
