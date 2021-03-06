package net.dragberry.carmanager.dao;

import java.util.List;

import net.dragberry.carmanager.domain.Transaction;
import net.dragberry.carmanager.transferobject.TransactionQueryListTO;

public interface TransactionDao extends DataAccessObject<Transaction, Long> {

	List<Transaction> fetchList(TransactionQueryListTO query);
	
	Long count(TransactionQueryListTO query);
}
