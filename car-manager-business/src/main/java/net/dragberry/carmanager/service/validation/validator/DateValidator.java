package net.dragberry.carmanager.service.validation.validator;

import java.util.Collection;

import net.dragberry.carmanager.domain.Transaction;
import net.dragberry.carmanager.service.validation.Validator;
import net.dragberry.carmanager.to.ValidationIssue;

public class DateValidator implements Validator<Transaction> {

	@Override
	public Collection<ValidationIssue<Transaction>> validate(Transaction entity) {
		if (entity.getExecutionDate() == null) {
			return issues(issue(entity, 1000001));
		}
		return noIssues();
	}
	
	

}
