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
import org.openmrs.Encounter;
import org.openmrs.module.posttoopenhim.api.Tunnel;

/**
 * This class listens for Encounter CREATED events. If MPI is enabled, it exports Encounter to MPI,
 * and updates the local Encounter with the new Encounter identifier generated by the MPI.
 */
public class EncounterCreatedListener extends EncounterActionListener {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EncounterCreatedListener.class);
	
	/**
	 * Defines the list of Actions that this subscribable event listener class listens out for.
	 * 
	 * @return a list of Actions this listener can deal with
	 */
	public List<String> subscribeToActions() {
		List actions = new ArrayList<String>();
		actions.add(Event.Action.CREATED.name());
		return actions;
	}
	
	/**
	 * Exports Encounter to MPI, and updates the local Encounter with the new Encounter identifier
	 * generated by the MPI.
	 * 
	 * @param message message with properties.
	 */
	@Override
	public void performAction(Message message) {
		log.info("[info]------ a new Encounter");
		Encounter encounter = extractEncounter(message);
		Tunnel tunnel = new Tunnel(encounter);
		tunnel.send();
	}
	
}
