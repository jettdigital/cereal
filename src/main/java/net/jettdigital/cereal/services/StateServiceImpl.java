package net.jettdigital.cereal.services;


import java.util.UUID;

import javax.transaction.Transactional;

import net.jettdigital.cereal.domain.Organization;
import net.jettdigital.cereal.domain.Serial;
import net.jettdigital.cereal.domain.State;
import net.jettdigital.cereal.domain.Direction;
import net.jettdigital.cereal.domain.Transaction;
import net.jettdigital.cereal.domain.TransactionRequest;
import net.jettdigital.cereal.domain.TransactionDetail;
import net.jettdigital.cereal.repos.OrganizationRepo;
import net.jettdigital.cereal.repos.SerialRepo;
import net.jettdigital.cereal.repos.StateRepo;
import net.jettdigital.cereal.repos.TransactionRepo;
import net.jettdigital.cereal.repos.TransactionDetailRepo;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StateServiceImpl implements StateService {

	private Logger log = LoggerFactory.getLogger(StateServiceImpl.class);
	
	@Autowired
	private TransactionRepo stateTransactionRepo;
	@Autowired
	private TransactionDetailRepo transactionSummaryRepo;
	@Autowired
	private StateRepo stateRepo;
	@Autowired
	private SerialRepo serialRepo;
	@Autowired
	private OrganizationRepo organizationRepo;
	
	@Override
	@Transactional
	public void changeState(TransactionRequest transactionRequest) {
		
		Organization requestedBy = organizationRepo.findOne( transactionRequest.getRequestedBy() );
		Serial serial = serialRepo.findOneByNum( transactionRequest.getSerial() );
		
		State fromState = serial.getCurrentState();
		State toState = stateRepo.findOne( transactionRequest.getToState());
		State expectedNextState = null;
		String expectedNextStateName = "UNKNOWN";
		if ( transactionRequest.getExpectedNextState() != null ) {
			expectedNextState = stateRepo.findOne( transactionRequest.getExpectedNextState() );
			expectedNextStateName = expectedNextState.getName();
		}
		
		log.info("From="+fromState.getName()+" To="+toState.getName()+" and expected to go To="+expectedNextStateName+" Serial="+serial.getNum());
		DateTime now = DateTime.now();
	
		TransactionDetail summary = new TransactionDetail();
		summary.setRequestedByOrg(requestedBy);
		summary.setExecutedOn(now.toDate());
		summary.setExpectedNextState(expectedNextState);
		transactionSummaryRepo.save(summary);
		
		Transaction fromTrans = new Transaction();
		fromTrans.setDirection(Direction.FROM);
		fromTrans.setState(fromState);
		fromTrans.setSerial(serial);
		fromTrans.setDetail(summary);
		stateTransactionRepo.save(fromTrans);
		
		Transaction toTrans = new Transaction();
		toTrans.setDirection(Direction.TO);
		toTrans.setSerial(serial);
		toTrans.setState(toState);
		toTrans.setDetail(summary);
		stateTransactionRepo.save(toTrans);
		
		serial.setCurrentState( toState );
		serial.setExpectedNextState(expectedNextState);
		serial.setPreviousState(fromState);
		serialRepo.save(serial);
				
	}
	
}
