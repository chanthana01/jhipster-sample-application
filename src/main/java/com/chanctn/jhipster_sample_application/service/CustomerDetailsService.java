package com.chanctn.jhipster_sample_application.service;

import com.chanctn.jhipster_sample_application.domain.CustomerDetails;
import com.chanctn.jhipster_sample_application.repository.CustomerDetailsRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link CustomerDetails}.
 */
@Service
@Transactional
public class CustomerDetailsService {

    private final Logger log = LoggerFactory.getLogger(CustomerDetailsService.class);

    private final CustomerDetailsRepository customerDetailsRepository;

    public CustomerDetailsService(CustomerDetailsRepository customerDetailsRepository) {
        this.customerDetailsRepository = customerDetailsRepository;
    }

    /**
     * Save a customerDetails.
     *
     * @param customerDetails the entity to save.
     * @return the persisted entity.
     */
    public Mono<CustomerDetails> save(CustomerDetails customerDetails) {
        log.debug("Request to save CustomerDetails : {}", customerDetails);
        return customerDetailsRepository.save(customerDetails);
    }

    /**
     * Partially update a customerDetails.
     *
     * @param customerDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CustomerDetails> partialUpdate(CustomerDetails customerDetails) {
        log.debug("Request to partially update CustomerDetails : {}", customerDetails);

        return customerDetailsRepository
            .findById(customerDetails.getId())
            .map(
                existingCustomerDetails -> {
                    if (customerDetails.getPhone() != null) {
                        existingCustomerDetails.setPhone(customerDetails.getPhone());
                    }
                    if (customerDetails.getAddressLine1() != null) {
                        existingCustomerDetails.setAddressLine1(customerDetails.getAddressLine1());
                    }
                    if (customerDetails.getAddressLine2() != null) {
                        existingCustomerDetails.setAddressLine2(customerDetails.getAddressLine2());
                    }
                    if (customerDetails.getCity() != null) {
                        existingCustomerDetails.setCity(customerDetails.getCity());
                    }
                    if (customerDetails.getCountry() != null) {
                        existingCustomerDetails.setCountry(customerDetails.getCountry());
                    }

                    return existingCustomerDetails;
                }
            )
            .flatMap(customerDetailsRepository::save);
    }

    /**
     * Get all the customerDetails.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CustomerDetails> findAll() {
        log.debug("Request to get all CustomerDetails");
        return customerDetailsRepository.findAll();
    }

    /**
     * Returns the number of customerDetails available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return customerDetailsRepository.count();
    }

    /**
     * Get one customerDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CustomerDetails> findOne(Long id) {
        log.debug("Request to get CustomerDetails : {}", id);
        return customerDetailsRepository.findById(id);
    }

    /**
     * Delete the customerDetails by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete CustomerDetails : {}", id);
        return customerDetailsRepository.deleteById(id);
    }
}
