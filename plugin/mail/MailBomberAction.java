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

import gui.docking.*;

import java.awt.event.*;

import net.infonode.docking.*;
import action.*;

/**
 * build and load dashboard for mail plugin
 * 
 * @author terry
 * 
 */
public class MailBomberAction extends TAbstractAction {

	public MailBomberAction() {
		super("mail.action", "/plugin/mail/mail2", NO_SCOPE, "ttmail.action");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		View hlp = DockingContainer.createDynamicView("plugin.xdoc.XDocHelpBrowser");
		SplitWindow sw = new SplitWindow(false, 0.6f, DockingContainer.createDynamicView("plugin.mail.AddressBook"),
				DockingContainer.createDynamicView("plugin.mail.MailFolders"));
		SplitWindow sw1 = new SplitWindow(true, 0.6f, sw, hlp);
		DockingContainer.setWindow(sw1, getClass().getName());
		// WindowBar wb = rootw.getWindowBar(Direction.RIGHT);
		// wb.addTab(hlp);
	}
}
