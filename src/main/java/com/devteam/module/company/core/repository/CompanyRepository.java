package com.devteam.module.company.core.repository;

import java.io.Serializable;

import com.devteam.core.module.data.db.repository.DataTPRepository;
import com.devteam.module.company.core.entity.Company;
import org.springframework.stereotype.Repository;


@Repository
public interface CompanyRepository extends DataTPRepository<Company, Serializable> {
	Company getByCode(String code);
}
