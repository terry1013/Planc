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
public class FlickaAction extends TAbstractAction {

	public FlickaAction() {
		super("flicka.action", "/plugin/flicka/flicka", NO_SCOPE, "ttflicka.action");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		SplitWindow sw = new SplitWindow(false, 0.6f, DockingContainer.createDynamicView("plugin.flicka.EntryPanel"),
				DockingContainer.createDynamicView("plugin.flicka.DBExplorer"));
		DockingContainer.setWindow(sw, getClass().getName());
	}
}
