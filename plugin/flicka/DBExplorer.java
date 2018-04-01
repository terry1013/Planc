/*******************************************************************************
 * Copyright (C) 2017 terry.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     terry - initial API and implementation
 ******************************************************************************/
package plugin.flicka;

import gui.*;
import gui.docking.*;

import java.beans.*;

import javax.swing.*;

import core.*;
import core.datasource.*;

public class DBExplorer extends UIListPanel implements DockingComponent {

	ServiceRequest request;

	/**
	 * new instance
	 */
	public DBExplorer() {
		super(null);
		String qfn = PluginManager.getMyFile("Flicka", "reslr");
		this.request = new ServiceRequest(ServiceRequest.DB_QUERY, qfn, null);
		//setToolBar(new JComponent[] {TUIUtils.getWebFindField(this)});
		putClientProperty(TConstants.SHOW_COLUMNS, "reracedate;rerace;reend_pos;rehorse");
	//	putClientProperty(TConstants.ICON_PARAMETERS,"0;/plugin/flicka/flicka");
	}

	@Override
	public UIComponentPanel getUIFor(AbstractAction aa) {
		UIComponentPanel pane = null;

		return pane;
	}
	
	@Override
	public void init() {
		setServiceRequest(request);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

	}
}
