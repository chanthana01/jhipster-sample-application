import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './customer-details.reducer';
import { ICustomerDetails } from 'app/shared/model/customer-details.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CustomerDetails = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const customerDetailsList = useAppSelector(state => state.customerDetails.entities);
  const loading = useAppSelector(state => state.customerDetails.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="customer-details-heading" data-cy="CustomerDetailsHeading">
        <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.home.title">Customer Details</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.home.createLabel">Create new Customer Details</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {customerDetailsList && customerDetailsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.phone">Phone</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.addressLine1">Address Line 1</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.addressLine2">Address Line 2</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.city">City</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.country">Country</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.user">User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {customerDetailsList.map((customerDetails, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${customerDetails.id}`} color="link" size="sm">
                      {customerDetails.id}
                    </Button>
                  </td>
                  <td>{customerDetails.phone}</td>
                  <td>{customerDetails.addressLine1}</td>
                  <td>{customerDetails.addressLine2}</td>
                  <td>{customerDetails.city}</td>
                  <td>{customerDetails.country}</td>
                  <td>{customerDetails.user ? customerDetails.user.login : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${customerDetails.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${customerDetails.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${customerDetails.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.home.notFound">No Customer Details found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CustomerDetails;
