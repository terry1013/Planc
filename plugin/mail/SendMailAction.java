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

import java.awt.event.*;
import java.util.*;

import action.*;
import core.datasource.*;

public class SendMailAction extends TAbstractAction {

	private UIListPanel listPanel;

	public SendMailAction(UIListPanel uilp) {
		super("mail.action.send", "/plugin/mail/mail_out", RECORD_SCOPE, "ttmail.action.send");
		this.listPanel = uilp;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Record rcd = listPanel.getRecord();
		Hashtable parms = new Hashtable();
		for (int i = 0; i < rcd.getFieldCount(); i++) {
			parms.put(rcd.getFieldName(i), rcd.getFieldValue(i));
		}
		MailBomberTask mbt = new MailBomberTask();
		mbt.setTaskParameters(null, parms);
//		TTaskManager.submitInteractiveTask("/plugin/mail/data_gear", "mail.send.txt01", mbt);
	}
}
