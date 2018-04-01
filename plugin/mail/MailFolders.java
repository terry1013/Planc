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
package plugin.mail;

import gui.*;
import gui.docking.*;

import java.awt.event.*;
import java.beans.*;
import java.sql.*;

import javax.swing.*;

import action.*;
import core.*;
import core.datasource.*;

/**
 * draft folder is where all composed mail are stored.
 * 
 * @author terry
 * 
 */
public class MailFolders extends UIListPanel implements DockingComponent, ActionListener {

	private ServiceRequest request;
	private JComboBox<TEntry> folderJCB;

	/**
	 * new instance
	 */
	public MailFolders() {
		super(null);
		TEntry te[] = TStringUtils.getTEntryGroup("mail.folders.");
		this.folderJCB = TUIUtils.getJComboBox("ttmail.folders", te, te[0]);
		folderJCB.addActionListener(this);

		this.request = new ServiceRequest(ServiceRequest.DB_QUERY, "m_messages", null);

		setToolBar(new NewRecord(this), new EditRecord(this), new DeleteRecord(this), new SendMailAction(this));
		putClientProperty(TConstants.SHOW_COLUMNS, "m_mesubject;m_meto;m_mecreated_at");
		putClientProperty(TConstants.ICON_PARAMETERS, "0;/plugin/mail/mail2");
	}

	@Override
	public UIComponentPanel getUIFor(AbstractAction aa) {
		UIComponentPanel pane = null;
		if (aa instanceof NewRecord) {
			Record r = getRecordModel();
			r.setFieldValue(0, System.currentTimeMillis());
			r.setFieldValue("m_mecreated_at", new Timestamp(System.currentTimeMillis()));
			r.setFieldValue("m_mefolder", "draft");
			pane = new ComposeMail(r, true);
		}
		if (aa instanceof EditRecord) {
			pane = new ComposeMail(getRecord(), false);
		}
		return pane;
	}

	@Override
	public void init() {
		actionPerformed(null);
		// setView(LIST_VIEW_MOSAIC);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		request.setData("m_mefolder = '" + ((TEntry) folderJCB.getSelectedItem()).getKey() + "'");
		setServiceRequest(request);
	}
}
