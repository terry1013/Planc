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
package plugin.xdoc;

import gui.*;
import gui.docking.*;

import java.beans.*;

import javax.swing.*;

import core.*;
import core.datasource.*;

public class DocumentRecipient extends UIListPanel implements DockingComponent {

	private ServiceRequest serviceRequest;

	public DocumentRecipient() {
		super(null);
		// configure based on plugin parameteres
		String dbf = PluginManager.getPluginProperty("XDoc", "xdoc.config.recipient.file");
		String dbfwc = PluginManager.getPluginProperty("XDoc", "xdoc.config.recipient.where");

		this.serviceRequest = new ServiceRequest(ServiceRequest.DB_QUERY, dbf, dbfwc);
		setToolBar(new XdocReportAction(this));
		// new JButton(new ExportToFile("sle_users", "")), });
		putClientProperty(TConstants.SHOW_COLUMNS,
				PluginManager.getPluginProperty("XDoc", "xdoc.config.recipient." + TConstants.SHOW_COLUMNS));
		// putClientProperty(PropertyNames.SPECIAL_COLUMN, "t_ususer_id");
		putClientProperty(TConstants.ICON_PARAMETERS,
				PluginManager.getPluginProperty("XDoc", "xdoc.config.recipient." + TConstants.ICON_PARAMETERS));
	}

	@Override
	public void init() {
		setView(TABLE_VIEW);
		setServiceRequest(serviceRequest);
		// getToolBar().setVisible(false);
		// setMessage("sle.ui.msg15");
	}

	@Override
	public UIComponentPanel getUIFor(AbstractAction aa) {
		UIComponentPanel pane = null;

		return pane;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

	}
}
