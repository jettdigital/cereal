package net.jettdigital.cereal.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import net.jettdigital.cereal.domain.Organization;
import net.jettdigital.cereal.domain.Serial;
import net.jettdigital.cereal.domain.SerialContent;
import net.jettdigital.cereal.domain.State;
import net.jettdigital.cereal.domain.Transaction;
import net.jettdigital.cereal.domain.TransactionRequest;
import net.jettdigital.cereal.repos.OrganizationRepo;
import net.jettdigital.cereal.repos.SerialRepo;
import net.jettdigital.cereal.repos.StateRepo;
import net.jettdigital.cereal.repos.TransactionRepo;
import net.jettdigital.cereal.services.SerialContentService;
import net.jettdigital.cereal.services.StateService;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class ResourceController {

	@Autowired
	private StateRepo stateRepo;
	@Autowired
	private SerialContentService serialContentService;
	@Autowired
	private SerialRepo serialRepo;
	@Autowired
	private OrganizationRepo organizationRepo;
	@Autowired
	private StateService stateService;
	@Autowired
	private TransactionRepo stateTransactionRepo;
	
	// simple ping
	@RequestMapping("/api/ping")
	public String ping() {
		return String.valueOf(System.currentTimeMillis());
	}
	
	/* -- organization methods -- */
	// create a new org
	@RequestMapping(value="/api/org", method=RequestMethod.PUT)
	public ResponseEntity<Void> putOrganization(@RequestBody Organization organization, UriComponentsBuilder ucb) {
		
		organization = organizationRepo.save(organization);
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(ucb.path("/api/org/{orgId}").buildAndExpand(organization.getId()).toUri());

	    return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}
	// get one org
	@RequestMapping(value="/api/org/{orgId}", method=RequestMethod.GET)
	public Organization getOrganization(@PathVariable Long orgId) {
		
		Organization org = organizationRepo.findOne(orgId);
		if ( org == null ) {
			throw new ResourceNotFoundException("No Organization found");
		}
		return org;
	}
	
	// get all orgs
	@RequestMapping(value="/api/org", method=RequestMethod.GET)
	public Iterable<Organization> getOrganizations() {
		return organizationRepo.findAll();
	}	
	
	/* -- state methods -- */
	// create a state for the given org
	@RequestMapping(value="/api/org/{orgId}/state", method=RequestMethod.PUT)
	public ResponseEntity<Void> putState(@PathVariable Long orgId, @RequestBody State state, UriComponentsBuilder ucb) {
		
		Organization organization = organizationRepo.findOne(orgId);
		state.setOrganization(organization);
		state = stateRepo.save(state);
		organization.getStates().add(state);
		organizationRepo.save(organization);
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(ucb.path("/api/org/{orgId}/state/{stateId}").buildAndExpand(organization.getId(), state.getId()).toUri());

	    return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}
	
	// get one state for the given org
	@RequestMapping(value="/api/org/{orgId}/state/{stateId}", method=RequestMethod.GET)
	public State getState(@PathVariable Long orgId, @PathVariable Long stateId) {
		
		Organization organization = organizationRepo.findOne(orgId);
		State state = stateRepo.findOneByIdAndOrganization(stateId, organization);
		if ( state == null ) {
			throw new ResourceNotFoundException("State not found");
		}
		return state;
	}	
	// get all states for the given org
	@RequestMapping(value="/api/org/{orgId}/state", method=RequestMethod.GET)
	public List<State> getOrganizationStates(@PathVariable Long orgId) {
		
		Organization org = organizationRepo.findOne(orgId);
		return stateRepo.findByOrganization(org);		

	}
	
	// get all serials for the given state and org
	@RequestMapping(value="/api/org/{orgId}/state/{stateId}/serial", method=RequestMethod.GET)
	public List<Serial> getStateSerials(@PathVariable Long orgId, @PathVariable Long stateId) {
		Organization organization = organizationRepo.findOne(orgId);
		return stateRepo.findOneByIdAndOrganization(stateId, organization).getSerials();
	}
	
	/* --- serial methods -- */
	// create a serialized item with the given serial num
	@RequestMapping(value="/api/org/{orgId}/serial/{serialNum}", method=RequestMethod.PUT)
	@Transactional
	public ResponseEntity<Void> putSerial(@PathVariable Long orgId, @PathVariable String serialNum, 
			HttpServletRequest request, UriComponentsBuilder ucb) throws IOException {
		
		Organization organization = organizationRepo.findOne(orgId);
		State initialState = stateRepo.findOneByNameAndOrganization("INITIAL", organization);
		if ( initialState == null ) {
			initialState = new State();
			initialState.setName("INITIAL");
			initialState.setOrganization(organization);
			initialState = stateRepo.save(initialState);
			organization.getStates().add(initialState);
			organizationRepo.save(organization);
		}
		
		Serial serial = new Serial();
		serial.setNum(serialNum);
		serial.setCurrentState(initialState);
		serial = serialRepo.save(serial);
		
		serialContentService.persist(IOUtils.toCharArray( request.getInputStream()), serial);
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(ucb.path("/api/serial/{serialNum}").buildAndExpand(serial.getNum()).toUri());

	    return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}
	
	// update serialized item with the given serial num
	@RequestMapping(value="/api/serial/{serialNum}", method=RequestMethod.POST)
	@Transactional
	public ResponseEntity<Void> updateSerial(@PathVariable String serialNum, 
			HttpServletRequest request, UriComponentsBuilder ucb) throws IOException {
		
		Serial serial = serialRepo.findOneByNum(serialNum);
		serial = serialRepo.save(serial);

		serialContentService.persist(IOUtils.toCharArray( request.getInputStream()), serial);
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(ucb.path("/api/serial/{serialNum}").buildAndExpand(serial.getNum()).toUri());

	    return new ResponseEntity<>(headers, HttpStatus.OK);
	}	
	
	// get one serial
	@RequestMapping(value="/api/serial/{serialNum}", method=RequestMethod.GET)
	public @ResponseBody Serial getSerial(@PathVariable String serialNum) {
		Serial serial = serialRepo.findOneByNum(serialNum);
		if ( serial == null ) {
			throw new ResourceNotFoundException("Serial not found");
		}
		return serial;
	}
	
	// get serial content 
	@RequestMapping(value="/api/serial/{serialNum}/content", method=RequestMethod.GET)
	public @ResponseBody List<SerialContent> getSerialContent(@PathVariable String serialNum, @RequestParam(value="current", required=false) Boolean isCurrent) {
		
		Serial serial = serialRepo.findOneByNum(serialNum);
		
		List<SerialContent> contentList = null;
		if ( isCurrent == true ) {
			contentList = new ArrayList<SerialContent>(1);
			contentList.add( serialContentService.current(serial) );
		} else {
			contentList = serialContentService.history(serial);
		}
		
		if ( contentList == null ) {
			throw new ResourceNotFoundException("No content found");
		}
		return contentList;
	}
	
	/* -- transaction methods -- */
	// add a transaction 
	@RequestMapping(value="/api/transaction", method=RequestMethod.POST)
	public void postTransaction(@RequestBody TransactionRequest transactionRequest) { 
		
		stateService.changeState(transactionRequest);
   
	}
	
	// get all transactions filtered by serial
	@RequestMapping(value="/api/transaction", method=RequestMethod.GET)
	public List<Transaction> getTransactionsBySerial(@RequestParam(value="serial", required=true) String serialNum) {		
		return stateTransactionRepo.findAllBySerialNumOrderByDetailExecutedOnDesc(serialNum);		
	}	
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public class ResourceNotFoundException extends RuntimeException {
	    public ResourceNotFoundException(String msg) {
	    	super(msg);
	    }
	}
		
}
