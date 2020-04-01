package org.openmrs.module.posttoopenhim.api.impl;

import javax.jms.Message;
import org.openmrs.Patient;
import org.openmrs.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.posttoopenhim.api.Tunnel;

/**
 * This class listens for patient UNVOIDED events. If MPI is enabled, it exports patient to MPI, and
 * updates the local patient with the new patient identifier generated by the MPI.
 */
public class PatientUnVoidedListener extends PatientActionListener {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PatientUnVoidedListener.class);
	
	/**
	 * Defines the list of Actions that this subscribable event listener class listens out for.
	 * 
	 * @return a list of Actions this listener can deal with
	 */
	public List<String> subscribeToActions() {
		log.info("[info]------ subscribed Patient UNVOIDED event...");
		List actions = new ArrayList<String>();
		actions.add(Event.Action.UNVOIDED.name());
		return actions;
	}
	
	/**
	 * Exports patient to MPI, and updates the local patient with the new patient identifier
	 * generated by the MPI.
	 * 
	 * @param message message with properties.
	 */
	@Override
	public void performAction(Message message) {
		log.info("[info]------ a new patient");
		Patient patient = extractPatient(message);
		Tunnel tunnel = new Tunnel(patient, Event.Action.UNVOIDED.name());
		tunnel.send();
	}
	
}
