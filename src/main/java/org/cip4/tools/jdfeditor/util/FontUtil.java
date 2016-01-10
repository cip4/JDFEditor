package org.cip4.tools.jdfeditor.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.RuntimeProperties;
import org.cip4.tools.jdfeditor.service.SettingService;

public class FontUtil
{
	private static final Log LOG = LogFactory.getLog(FontUtil.class);

	private static final SettingService settingService = SettingService.getSettingService();


	public static void calcFontSize()
	{
		int fontSize = (RuntimeProperties.originalTextFontSize * settingService.getInt(SettingKey.FONT_SIZE_ENLARGED)) / 100;
		LOG.info("Recalc enlarged font size, set it to: " + fontSize);
		RuntimeProperties.enlargedTextFontSize = fontSize;
	}
}
