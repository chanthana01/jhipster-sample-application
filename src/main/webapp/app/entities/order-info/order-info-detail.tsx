import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './order-info.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OrderInfoDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const orderInfoEntity = useAppSelector(state => state.orderInfo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderInfoDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.orderInfo.detail.title">OrderInfo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderInfoEntity.id}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="jhipsterSampleApplicationApp.orderInfo.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{orderInfoEntity.quantity}</dd>
          <dt>
            <span id="pricePerUnit">
              <Translate contentKey="jhipsterSampleApplicationApp.orderInfo.pricePerUnit">Price Per Unit</Translate>
            </span>
          </dt>
          <dd>{orderInfoEntity.pricePerUnit}</dd>
          <dt>
            <span id="createAt">
              <Translate contentKey="jhipsterSampleApplicationApp.orderInfo.createAt">Create At</Translate>
            </span>
          </dt>
          <dd>{orderInfoEntity.createAt ? <TextFormat value={orderInfoEntity.createAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="modifyAt">
              <Translate contentKey="jhipsterSampleApplicationApp.orderInfo.modifyAt">Modify At</Translate>
            </span>
          </dt>
          <dd>{orderInfoEntity.modifyAt ? <TextFormat value={orderInfoEntity.modifyAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.orderInfo.promotionInfo">Promotion Info</Translate>
          </dt>
          <dd>{orderInfoEntity.promotionInfo ? orderInfoEntity.promotionInfo.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.orderInfo.product">Product</Translate>
          </dt>
          <dd>{orderInfoEntity.product ? orderInfoEntity.product.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.orderInfo.order">Order</Translate>
          </dt>
          <dd>{orderInfoEntity.order ? orderInfoEntity.order.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order-info" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-info/${orderInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderInfoDetail;
