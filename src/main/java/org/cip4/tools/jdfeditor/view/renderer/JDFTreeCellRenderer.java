package org.cip4.tools.jdfeditor.view.renderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.tools.jdfeditor.service.SettingService;

/**
 * Render JDF Elements in tree view.
 */
public class JDFTreeCellRenderer extends AbstractTreeCellRenderer
{

	private static final Log LOGGER = LogFactory.getLog(SettingService.class);

	private static final long serialVersionUID = 1526856515806803255L;

	@Override
	protected Log getLogger()
	{
		return LOGGER;
	}

}