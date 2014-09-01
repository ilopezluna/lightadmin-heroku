package org.lightadmin.test.scope;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.lightadmin.api.config.utils.DomainTypeSpecification;
import org.lightadmin.test.model.TestCustomer;

public class DeleteTestSpecification implements
		DomainTypeSpecification<TestCustomer> {

	@Override
	public Predicate toPredicate(Root<TestCustomer> root,
			CriteriaQuery<?> query, CriteriaBuilder cb) {
		return cb.equal(root.get("firstname"), "Test");
	}

}
