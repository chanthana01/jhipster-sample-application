import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './promotion.reducer';
import { IPromotion } from 'app/shared/model/promotion.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PromotionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const promotionEntity = useAppSelector(state => state.promotion.entity);
  const loading = useAppSelector(state => state.promotion.loading);
  const updating = useAppSelector(state => state.promotion.updating);
  const updateSuccess = useAppSelector(state => state.promotion.updateSuccess);

  const handleClose = () => {
    props.history.push('/promotion' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.expireAt = convertDateTimeToServer(values.expireAt);
    values.createAt = convertDateTimeToServer(values.createAt);
    values.modifyAt = convertDateTimeToServer(values.modifyAt);

    const entity = {
      ...promotionEntity,
      ...values,
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
          expireAt: displayDefaultDateTime(),
          createAt: displayDefaultDateTime(),
          modifyAt: displayDefaultDateTime(),
        }
      : {
          ...promotionEntity,
          expireAt: convertDateTimeFromServer(promotionEntity.expireAt),
          createAt: convertDateTimeFromServer(promotionEntity.createAt),
          modifyAt: convertDateTimeFromServer(promotionEntity.modifyAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.promotion.home.createOrEditLabel" data-cy="PromotionCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplicationApp.promotion.home.createOrEditLabel">Create or edit a Promotion</Translate>
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
                  id="promotion-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.promotion.promotionDescription')}
                id="promotion-promotionDescription"
                name="promotionDescription"
                data-cy="promotionDescription"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.promotion.promorionFormular')}
                id="promotion-promorionFormular"
                name="promorionFormular"
                data-cy="promorionFormular"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.promotion.expireAt')}
                id="promotion-expireAt"
                name="expireAt"
                data-cy="expireAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.promotion.createAt')}
                id="promotion-createAt"
                name="createAt"
                data-cy="createAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.promotion.modifyAt')}
                id="promotion-modifyAt"
                name="modifyAt"
                data-cy="modifyAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/promotion" replace color="info">
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

export default PromotionUpdate;
