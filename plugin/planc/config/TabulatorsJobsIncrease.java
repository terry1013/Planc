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
 * planc SLE_TAB_JOBS increase
 * 
 */
public class TabulatorsJobsIncrease extends AbstractFileIncreaseSupport implements DockingComponent, CellEditorListener {

	private Record tabvalRcd, jobRcd;
	private String companyId;

	public TabulatorsJobsIncrease() {
		super();
		putClientProperty(TConstants.SHOW_COLUMNS, "step;amount");
		putClientProperty(TConstants.ICON_PARAMETERS, "-1; ");
		putClientProperty(TConstants.ALLOW_INPUT_FROM_CELL, false);
	}

	@Override
	public void init() {
		setView(TABLE_VIEW);
		setMessage("sle.ui.msg06");
		setFormattForColums(0, "Paso #0");
	}

	
	@Override
	public UIComponentPanel getUIFor(AbstractAction aa) {
		return null;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object src = evt.getSource();
//		Object prp = evt.getPropertyName();
		Object selobj = evt.getNewValue();
				
		// tabulator vigency selected
		if ((src instanceof TabulatorsTree)) {
			Record r = (Record) selobj;
			tabvalRcd =  (r == null) || r.getFieldValue("subnode").equals("") ? null : r;
			// save companyid
			companyId = PlanCSelector.getNodeValue(PlanCSelector.COMPANY);
		}

		// jobs selected
		if ((src instanceof JobsTree)) {
			jobRcd =  (Record) selobj;
		}

		// set the request
		if (tabvalRcd != null && jobRcd != null) {
			
			Object jobid = jobRcd.getFieldValue("id");
			Object tavid = tabvalRcd.getFieldValue("node");
			String wc = "tab_validity_id = '" + tavid + "' AND company_id = '" + companyId +"' AND job_id = '"+jobid+"'";
			DBAccess dba = ConnectionManager.getAccessTo("sle_tab_jobs");
			Vector<Record> tlist = dba.search(wc, null);
			Record mod = dba.getModel();
			mod.setFieldValue("tab_validity_id", tavid);
			mod.setFieldValue("company_id", companyId);
			mod.setFieldValue("job_id", jobid);

			ServiceRequest sr = getServicerRequestFromStep(tlist, mod);
			setServiceRequest(sr);

			// override table elements and editor listeners
			JTable jt = getJTable();
			jt.getDefaultEditor(Double.class).addCellEditorListener(this);
		} else {
			setMessage("sle.ui.msg06");
		}
	}
}
