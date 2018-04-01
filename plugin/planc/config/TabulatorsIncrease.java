package plugin.planc.config;

import gui.*;
import gui.docking.*;

import java.beans.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import core.*;
import core.datasource.*;

import plugin.planc.*;


/**
 * planc SLE_TAB_AMOUNT increase
 * 
 */
public class TabulatorsIncrease extends AbstractFileIncreaseSupport implements DockingComponent, CellEditorListener {

	private Record tabvalRcd;

	public TabulatorsIncrease() {
		super();
		putClientProperty(TConstants.SHOW_COLUMNS, "step;amount");
		putClientProperty(TConstants.ICON_PARAMETERS, "-1; ");
		putClientProperty(TConstants.ALLOW_INPUT_FROM_CELL, false);
	}

	@Override
	public void init() {
		setView(TABLE_VIEW);
		setMessage("sle.ui.msg07");
		setFormattForColums(0, "Paso #0");
	}

	
	@Override
	public UIComponentPanel getUIFor(AbstractAction aa) {
		return null;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object src = evt.getSource();
		Object prp = evt.getPropertyName();
		Object selobj = evt.getNewValue();
		
		// tabulator vigency selected
		if ((src instanceof TabulatorsTree) && prp.equals(TConstants.RECORD_SELECTED)) {
			Record r = (Record) selobj;
			tabvalRcd =  (r == null) || r.getFieldValue("subnode").equals("") ? null : r;
		}

		// set the request
		if (tabvalRcd != null) {
			Long tabv = new Long((String) tabvalRcd.getFieldValue("node"));
			Vector<Record> srclist = ConnectionManager.getAccessTo("SLE_TAB_AMOUNT").search("tab_validity_id = " + tabv,
					null);
			Record rmod = ConnectionManager.getAccessTo("SLE_TAB_AMOUNT").getModel();
			rmod.setFieldValue("tab_validity_id", tabv);

			ServiceRequest sr = getServicerRequestFromStep(srclist, rmod);
			setServiceRequest(sr);

			// override table elements and editor listeners
			JTable jt = getJTable();
			jt.getDefaultEditor(Double.class).addCellEditorListener(this);
		} else {
			setMessage("sle.ui.msg07");
		}
	}
}
