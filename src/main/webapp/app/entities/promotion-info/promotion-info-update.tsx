import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IPromotion } from 'app/shared/model/promotion.model';
import { getEntities as getPromotions } from 'app/entities/promotion/promotion.reducer';
import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { getEntity, updateEntity, createEntity, reset } from './promotion-info.reducer';
import { IPromotionInfo } from 'app/shared/model/promotion-info.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PromotionInfoUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const promotions = useAppSelector(state => state.promotion.entities);
  const products = useAppSelector(state => state.product.entities);
  const promotionInfoEntity = useAppSelector(state => state.promotionInfo.entity);
  const loading = useAppSelector(state => state.promotionInfo.loading);
  const updating = useAppSelector(state => state.promotionInfo.updating);
  const updateSuccess = useAppSelector(state => state.promotionInfo.updateSuccess);

  const handleClose = () => {
    props.history.push('/promotion-info' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPromotions({}));
    dispatch(getProducts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...promotionInfoEntity,
      ...values,
      promotion: promotions.find(it => it.id.toString() === values.promotionId.toString()),
      product: products.find(it => it.id.toString() === values.productId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...promotionInfoEntity,
          promotionId: promotionInfoEntity?.promotion?.id,
          productId: promotionInfoEntity?.product?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.promotionInfo.home.createOrEditLabel" data-cy="PromotionInfoCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplicationApp.promotionInfo.home.createOrEditLabel">
              Create or edit a PromotionInfo
            </Translate>
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
                  id="promotion-info-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                id="promotion-info-promotion"
                name="promotionId"
                data-cy="promotion"
                label={translate('jhipsterSampleApplicationApp.promotionInfo.promotion')}
                type="select"
              >
                <option value="" key="0" />
                {promotions
                  ? promotions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="promotion-info-product"
                name="productId"
                data-cy="product"
                label={translate('jhipsterSampleApplicationApp.promotionInfo.product')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/promotion-info" replace color="info">
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

export default PromotionInfoUpdate;
