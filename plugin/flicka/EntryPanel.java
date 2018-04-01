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

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import core.*;
import core.datasource.*;

public class EntryPanel extends JPanel implements DockingComponent, ActionListener {

	ServiceRequest request;
	private JTextArea leftTextArea, resultTextArea;
	private String magazinefile;
	private JButton testLeft, send;
	private DBAccess dbAccess;

	/**
	 * new instance
	 */
	public EntryPanel() {
		super(new BorderLayout());
		this.magazinefile = PluginManager.getMyFile("Flicka", "reslr");
		this.dbAccess = ConnectionManager.getAccessTo(magazinefile);
	}

	@Override
	public void init() {
		Font f = new Font("Courier New", Font.PLAIN, 12);
		this.leftTextArea = new JTextArea(20, 100);
		leftTextArea.setFont(f);
		this.resultTextArea = new JTextArea(20, 100);
		resultTextArea.setFont(f);

		this.testLeft = new JButton("test Left");
		this.testLeft.addActionListener(this);
		this.send = new JButton("Send");
		this.send.addActionListener(this);

		JPanel jp1 = new JPanel(new GridLayout(1, 2, 4, 4));
		jp1.add(new JScrollPane(leftTextArea));
		jp1.add(new JScrollPane(resultTextArea));

		JPanel jpb = new JPanel(new GridLayout(0, 2));
		jpb.add(testLeft);
		jpb.add(send);

		JPanel jp2 = new JPanel(new BorderLayout(4, 4));
		jp2.add(jp1, BorderLayout.CENTER);
		jp2.add(jpb, BorderLayout.SOUTH);

		add(jp2, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		resultTextArea.setText("");

		if (e.getSource() == testLeft) {
			parseLeft();
		}
		if (e.getSource() == send) {
			sendToFile();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

	}

	private void sendToFile() {
		Vector<Record> leftList = parseLeft();
		for (Record rcd : leftList) {
			print(rcd);
			dbAccess.write(rcd);
		}
	}

	private Vector<Record> parseLeft() {
		Vector<Record> list = new Vector<Record>();
		String l = leftTextArea.getText();
		l = l.replaceAll("[']", " ");
		int lincnt = 0;
		String[] lines = l.split("\n");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			for (lincnt = 0; lincnt < lines.length; lincnt++) {
				Record mod = dbAccess.getModel();
				// mark header group
				if (lines[lincnt].startsWith("Obs.")) {
					// put the horse gender at rigth of Obs. mark (M or F)
					resultTextArea.append("gender and racedate\n");
					mod.setFieldValue("rehorsegender", lines[lincnt].split(" ")[1]);
					mod.setFieldValue("reracedate", sdf.parseObject(lines[lincnt + 2]));

					resultTextArea.append("race data line " +lines[lincnt + 3]+"\n");
					String fields[] = lines[lincnt + 3].split(" ");
					mod.setFieldValue("rerace", new Integer(fields[2].substring(1, fields[2].length() - 1)));
					mod.setFieldValue("redistance", new Integer(fields[4]));
					mod.setFieldValue("reracetime", new Double(fields[8]));

					resultTextArea.append("Serie data line "+lines[lincnt + 4]+"\n");
					fields = lines[lincnt + 4].split(" ");
					mod.setFieldValue("reserie", fields[1]);
					mod.setFieldValue("repartial1", new Double(fields[5]));
					mod.setFieldValue("repartial2", new Double(fields[7]));

					resultTextArea.append("race result. do until next Obs mark or end of lines\n");
					int newlincnt = lincnt + 6;
					while (newlincnt < lines.length) {
						String newline = lines[newlincnt];
						newlincnt++;
						if (newline.startsWith("Obs.")) {
							break;
						}
						newline = newline.replaceAll(",", ".");
						fields = newline.split(" ");
						Record rcd = new Record(mod);

						resultTextArea.append("reend_pos\n");
						rcd.setFieldValue("reend_pos", new Integer(fields[0]));
						// check until double parseable field. the gap between is the horse name
						resultTextArea.append("rehorse & rejockey_weight\n");
						String tmp = "";
						int fldcnt = 0;
						for (int j = 1; j < 5; j++) {
							try {
								Double d = new Double(fields[j]);
								rcd.setFieldValue("rehorse", tmp.trim());
								rcd.setFieldValue("rejockey_weight", d);
								fldcnt = j;
								break;
							} catch (Exception e) {
								// build horsename
								tmp += fields[j] + " ";
							}
						}
						tmp = "";
						resultTextArea.append("restar_lane\n");
						rcd.setFieldValue("restar_lane", fields[++fldcnt]);
						// look for the jockey name. numbers between starlane and jocky name put in unk field
						for (int j = fldcnt; j < fldcnt + 5; j++) {
							if (Character.isLetter(fields[j].charAt(0))) {
								fldcnt = j - 1;
								break;
							}
							// build unk field
							tmp += fields[j] + " ";
						}
						resultTextArea.append("reunk & rerating\n");
						rcd.setFieldValue("reunk", tmp.trim());
						rcd.setFieldValue("rerating", new Integer(fields[fldcnt++]));
						// jocky name are between last element and fldcnt
						resultTextArea.append("rejockey_name\n");
						tmp = "";
						for (int j = fldcnt; j < fields.length; j++) {
							if (Character.isLetter(fields[j].charAt(0))) {
								tmp += fields[j] + " ";
							}
						}
						rcd.setFieldValue("rejockey_name", tmp.substring(0, tmp.length()-2)); // espace and final dot
						list.add(rcd);
//						print(rcd);
					}
					lincnt = newlincnt - 2;
				}
			}

		} catch (Exception e) {
			resultTextArea.append(e.getMessage() + "\n");
			return null;
		}
		resultTextArea.setText("\nParse Left data are Ok!" + "\n");
		return list;
	}	

	private void print(Record r) {
		String txt = "";
		for (int c = 0; c < r.getFieldCount(); c++) {
			txt += r.getFieldName(c) + "\t >" + r.getFieldValue(c) + "<\n";
		}
		resultTextArea.append(txt + "\n");
	}
}
