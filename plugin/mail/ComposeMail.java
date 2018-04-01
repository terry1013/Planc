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
import gui.html.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;


import action.*;

import com.alee.extended.filechooser.*;
import com.alee.extended.list.*;
import com.alee.laf.button.*;
import com.alee.managers.popup.*;
import com.jgoodies.forms.builder.*;
import com.jgoodies.forms.layout.*;

import core.*;
import core.datasource.*;

/**
 * compose mail dialog
 */
public class ComposeMail extends AbstractRecordDataInput implements ListSelectionListener {

	private HTMLEditor htmlEditor;
	private WebFileDrop fileDrop;
	private SaveAsTaskAction saveAction;
	private JComboBox templateComboBox;
	private JTextField toJTextField;
	private WebCheckBoxList checkBoxList;

	/**
	 * new instance
	 * 
	 * @param rcd - record
	 * @param newr - new or not
	 */
	public ComposeMail(Record rcd, boolean newr) {
		super(null, rcd, newr);

		this.toJTextField = TUIUtils.getJTextField(rcd, "m_meto");
		addInputComponent("m_meto", toJTextField, true, true);

		templateComboBox = new JComboBox(getTemplates());
		templateComboBox.addActionListener(this);
		addInputComponent("m_metemplate", templateComboBox, false, true);
		// addInputComponent("m_mebcc", UIUtilities.getJTextField(rcd, "m_mebcc"), false, true);
		addInputComponent("m_mesubject", TUIUtils.getJTextField(rcd, "m_mesubject"), true, true);

		// body editor
		this.htmlEditor = new HTMLEditor();
		// htmlEditor.setText((String) rcd.getFieldValue("m_mebody"));
		addInputComponent("m_mebody", htmlEditor, true, true);
		htmlEditor.setPreferredSize(new Dimension(1000, 1000));

		// File drop area
		this.fileDrop = new WebFileDrop();
		JScrollPane jsp = new JScrollPane(fileDrop);
		jsp.setPreferredSize(new Dimension(1000, 1000));
		if (!newr) {
			ArrayList<File> fls = (ArrayList<File>) TPreferences
					.getObjectFromByteArray((byte[]) rcd.getFieldValue("m_meattachment"));
			fileDrop.setSelectedFiles(fls);
		}

		FormLayout lay = new FormLayout("left:pref, 3dlu, 320dlu", // columns
				"p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, 250dlu, 3dlu, 50dlu"); // rows
		CellConstraints cc = new CellConstraints();
		PanelBuilder build = new PanelBuilder(lay);

		build.add(createTOWebButton(), cc.xy(1, 1));
		build.add(getInputComponent("m_meto"), cc.xy(3, 1));
		build.add(getLabelFor("m_metemplate"), cc.xy(1, 3));
		build.add(getInputComponent("m_metemplate"), cc.xy(3, 3));

		build.add(getLabelFor("m_mesubject"), cc.xy(1, 7));
		build.add(getInputComponent("m_mesubject"), cc.xy(3, 7));
		build.add(htmlEditor, cc.xyw(1, 9, 3));
		build.add(jsp, cc.xyw(1, 11, 3));

		saveAction = new SaveAsTaskAction(this, "/plugin/mail/mail2", MailBomberTask.class.getName());
		saveAction.setEnabled(false);
		setActionBar(new AbstractAction[]{saveAction, new AceptAction(this), new CancelAction(this)});
		add(build.getPanel());
		preValidate(null);
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		try {
			TEntry te = (TEntry) templateComboBox.getSelectedItem();
			if (!te.getKey().equals("*none")) {

				PlanC.showConfirmDialog("txt");

				if (htmlEditor.getJEditorPane().getText().equals(""))
					// FileInputStream fis = new FileInputStream((File) te.getKey());
					htmlEditor.getJEditorPane().setPage(((File) te.getKey()).toURI().toURL());
			} else {
				htmlEditor.getJEditorPane().setText("");
			}
		} catch (Exception e) {
			SystemLog.logException1(e);
		}
	}

	@Override
	public void preValidate(Object src) {
		super.preValidate(src);
		if (htmlEditor.getText().equals("")) {

		}
		saveAction.setEnabled((!isShowingError() && isDefaultButtonEnabled()));
	}

	@Override
	public Record getRecord() {
		Record rcd = super.getRecord();
		rcd.setFieldValue("m_mebody", htmlEditor.getText());
		rcd.setFieldValue("m_meattachment", TPreferences.getByteArrayFromObject(fileDrop.getSelectedFiles()));
		return rcd;
	}

	private TEntry[] getTemplates() {
		String pip = TResourceUtils.USER_DIR + "/plugin/mail/template/";
		Vector<File> plugprp = TResourceUtils.findFiles(new File(pip), ".html");
		TEntry[] tt = new TEntry[plugprp.size() + 1];
		tt[0] = TStringUtils.getTEntry("mail.template.none");
		int i = 1;
		for (File f : plugprp) {
			tt[i++] = new TEntry(f, f.getName());
		}
		return tt;
	}

	private WebButton createTOWebButton() {
		WebButton towb = new WebButton(TStringUtils.getBundleString("m_meto"));
		WebButtonPopup popup = new WebButtonPopup(towb, PopupWay.rightDown);
		String abn = PluginManager.getMyFile("MailBomber", "m_address_book");
		Vector<Record> rlst = ConnectionManager.getAccessTo(abn).search(null, null);
		popup.setContent(createCheckBoxList(rlst));
		return towb;
	}

	private WebCheckBoxList createCheckBoxList(Vector<Record> rcdlst) {
		// create checkbokmodel
		CheckBoxListModel cblmodel = new CheckBoxListModel();
		for (Record r : rcdlst) {
			TEntry te = new TEntry(r.getFieldValue("m_abemail"), r.getFieldValue("m_abname"));
			cblmodel.addCheckBoxElement(te);
		}
		checkBoxList = new WebCheckBoxList(cblmodel);
		checkBoxList.setVisibleRowCount(10);
		checkBoxList.addListSelectionListener(this);
		checkBoxList.setEditable(true);
		return checkBoxList;
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// dont care about selection. update all field values
		String tos = " ";
		ListModel model = checkBoxList.getModel();
		int siz = model.getSize();
		for (int i = 0; i < siz; i++) {
			CheckBoxCellData cd = (CheckBoxCellData) model.getElementAt(i);
			TEntry te = (TEntry) cd.getUserObject();
			tos += cd.isSelected() ? te.getKey() + ";" : "";
		}
		toJTextField.setText(tos.substring(0, tos.length()-1));
	}
}
