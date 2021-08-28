import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './order.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OrderDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const orderEntity = useAppSelector(state => state.order.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.order.detail.title">Order</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderEntity.id}</dd>
          <dt>
            <span id="customerName">
              <Translate contentKey="jhipsterSampleApplicationApp.order.customerName">Customer Name</Translate>
            </span>
          </dt>
          <dd>{orderEntity.customerName}</dd>
          <dt>
            <span id="orderAddress">
              <Translate contentKey="jhipsterSampleApplicationApp.order.orderAddress">Order Address</Translate>
            </span>
          </dt>
          <dd>{orderEntity.orderAddress}</dd>
          <dt>
            <span id="totalAmount">
              <Translate contentKey="jhipsterSampleApplicationApp.order.totalAmount">Total Amount</Translate>
            </span>
          </dt>
          <dd>{orderEntity.totalAmount}</dd>
          <dt>
            <span id="omiseTxnId">
              <Translate contentKey="jhipsterSampleApplicationApp.order.omiseTxnId">Omise Txn Id</Translate>
            </span>
          </dt>
          <dd>{orderEntity.omiseTxnId}</dd>
          <dt>
            <span id="txnTimeStamp">
              <Translate contentKey="jhipsterSampleApplicationApp.order.txnTimeStamp">Txn Time Stamp</Translate>
            </span>
          </dt>
          <dd>{orderEntity.txnTimeStamp ? <TextFormat value={orderEntity.txnTimeStamp} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="isTxnSuccess">
              <Translate contentKey="jhipsterSampleApplicationApp.order.isTxnSuccess">Is Txn Success</Translate>
            </span>
          </dt>
          <dd>{orderEntity.isTxnSuccess ? 'true' : 'false'}</dd>
          <dt>
            <span id="createAt">
              <Translate contentKey="jhipsterSampleApplicationApp.order.createAt">Create At</Translate>
            </span>
          </dt>
          <dd>{orderEntity.createAt ? <TextFormat value={orderEntity.createAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="modifyAt">
              <Translate contentKey="jhipsterSampleApplicationApp.order.modifyAt">Modify At</Translate>
            </span>
          </dt>
          <dd>{orderEntity.modifyAt ? <TextFormat value={orderEntity.modifyAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/order" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order/${orderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderDetail;
