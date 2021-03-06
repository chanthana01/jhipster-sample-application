import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './customer-details.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CustomerDetailsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const customerDetailsEntity = useAppSelector(state => state.customerDetails.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="customerDetailsDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.detail.title">CustomerDetails</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{customerDetailsEntity.id}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{customerDetailsEntity.phone}</dd>
          <dt>
            <span id="addressLine1">
              <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.addressLine1">Address Line 1</Translate>
            </span>
          </dt>
          <dd>{customerDetailsEntity.addressLine1}</dd>
          <dt>
            <span id="addressLine2">
              <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.addressLine2">Address Line 2</Translate>
            </span>
          </dt>
          <dd>{customerDetailsEntity.addressLine2}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.city">City</Translate>
            </span>
          </dt>
          <dd>{customerDetailsEntity.city}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.country">Country</Translate>
            </span>
          </dt>
          <dd>{customerDetailsEntity.country}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.customerDetails.user">User</Translate>
          </dt>
          <dd>{customerDetailsEntity.user ? customerDetailsEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/customer-details" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/customer-details/${customerDetailsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CustomerDetailsDetail;
