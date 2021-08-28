import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IPromotionInfo } from 'app/shared/model/promotion-info.model';
import { getEntities as getPromotionInfos } from 'app/entities/promotion-info/promotion-info.reducer';
import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { IOrder } from 'app/shared/model/order.model';
import { getEntities as getOrders } from 'app/entities/order/order.reducer';
import { getEntity, updateEntity, createEntity, reset } from './order-info.reducer';
import { IOrderInfo } from 'app/shared/model/order-info.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OrderInfoUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const promotionInfos = useAppSelector(state => state.promotionInfo.entities);
  const products = useAppSelector(state => state.product.entities);
  const orders = useAppSelector(state => state.order.entities);
  const orderInfoEntity = useAppSelector(state => state.orderInfo.entity);
  const loading = useAppSelector(state => state.orderInfo.loading);
  const updating = useAppSelector(state => state.orderInfo.updating);
  const updateSuccess = useAppSelector(state => state.orderInfo.updateSuccess);

  const handleClose = () => {
    props.history.push('/order-info' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPromotionInfos({}));
    dispatch(getProducts({}));
    dispatch(getOrders({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createAt = convertDateTimeToServer(values.createAt);
    values.modifyAt = convertDateTimeToServer(values.modifyAt);

    const entity = {
      ...orderInfoEntity,
      ...values,
      promotionInfo: promotionInfos.find(it => it.id.toString() === values.promotionInfoId.toString()),
      product: products.find(it => it.id.toString() === values.productId.toString()),
      order: orders.find(it => it.id.toString() === values.orderId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createAt: displayDefaultDateTime(),
          modifyAt: displayDefaultDateTime(),
        }
      : {
          ...orderInfoEntity,
          createAt: convertDateTimeFromServer(orderInfoEntity.createAt),
          modifyAt: convertDateTimeFromServer(orderInfoEntity.modifyAt),
          promotionInfoId: orderInfoEntity?.promotionInfo?.id,
          productId: orderInfoEntity?.product?.id,
          orderId: orderInfoEntity?.order?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.orderInfo.home.createOrEditLabel" data-cy="OrderInfoCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplicationApp.orderInfo.home.createOrEditLabel">Create or edit a OrderInfo</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="order-info-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.orderInfo.quantity')}
                id="order-info-quantity"
                name="quantity"
                data-cy="quantity"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.orderInfo.pricePerUnit')}
                id="order-info-pricePerUnit"
                name="pricePerUnit"
                data-cy="pricePerUnit"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.orderInfo.createAt')}
                id="order-info-createAt"
                name="createAt"
                data-cy="createAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.orderInfo.modifyAt')}
                id="order-info-modifyAt"
                name="modifyAt"
                data-cy="modifyAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="order-info-promotionInfo"
                name="promotionInfoId"
                data-cy="promotionInfo"
                label={translate('jhipsterSampleApplicationApp.orderInfo.promotionInfo')}
                type="select"
              >
                <option value="" key="0" />
                {promotionInfos
                  ? promotionInfos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="order-info-product"
                name="productId"
                data-cy="product"
                label={translate('jhipsterSampleApplicationApp.orderInfo.product')}
                type="select"
              >
                <option value="" key="0" />
                {products
                  ? products.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="order-info-order"
                name="orderId"
                data-cy="order"
                label={translate('jhipsterSampleApplicationApp.orderInfo.order')}
                type="select"
              >
                <option value="" key="0" />
                {orders
                  ? orders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/order-info" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default OrderInfoUpdate;
