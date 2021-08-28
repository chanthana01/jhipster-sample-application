package com.chanctn.jhipster_sample_application.repository;

import com.chanctn.jhipster_sample_application.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
