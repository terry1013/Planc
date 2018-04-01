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

import javax.swing.*;

import action.*;
import core.*;
import core.datasource.*;
import core.reporting.*;

public class DocumentsTemplates extends UIListPanel {

	private String myfile;
	/**
	 * nueva instancia
	 * 
	 */
	public DocumentsTemplates() {
		super("xdoc.title13");
		this.myfile = PluginManager.getMyFile("XDoc", "xd_config");
		setToolBar(new NewRecord(this), new EditRecord(this), new DeleteRecord(this), new ExportToFileAction(this, "xd_codata"));
		putClientProperty(TConstants.SHOW_COLUMNS, "xd_coname;xd_codescription");
		putClientProperty(TConstants.ICON_PARAMETERS, "0;/plugin/xdoc/extension_");
	}

	@Override
	public void init() {
		setView(TABLE_VIEW);
		setServiceRequest(new ServiceRequest(ServiceRequest.DB_QUERY, myfile, null));

	}
	public UIComponentPanel getUIFor(AbstractAction aa) {
		UIComponentPanel pane = null;
		if (aa instanceof NewRecord) {
			Record rcd = getRecordModel();
			rcd.setFieldValue("xd_coid", TStringUtils.getUniqueID());
			pane = new DocumentsRecord(rcd, true);
		}
		if (aa instanceof EditRecord) {
			pane = new DocumentsRecord(getRecord(), false);
		}
		/**
		 * if (aa instanceof DocSignatureAction) { pane = new DocSignatures(getRecord()); } if (aa instanceof
		 * DocCumulativesAction) { pane = new DocCumulatives(getRecord()); }
		 */
		return pane;
	}

}
