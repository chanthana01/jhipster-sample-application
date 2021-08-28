import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './promotion.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PromotionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const promotionEntity = useAppSelector(state => state.promotion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="promotionDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.promotion.detail.title">Promotion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.id}</dd>
          <dt>
            <span id="promotionDescription">
              <Translate contentKey="jhipsterSampleApplicationApp.promotion.promotionDescription">Promotion Description</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.promotionDescription}</dd>
          <dt>
            <span id="promorionFormular">
              <Translate contentKey="jhipsterSampleApplicationApp.promotion.promorionFormular">Promorion Formular</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.promorionFormular}</dd>
          <dt>
            <span id="expireAt">
              <Translate contentKey="jhipsterSampleApplicationApp.promotion.expireAt">Expire At</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.expireAt ? <TextFormat value={promotionEntity.expireAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="createAt">
              <Translate contentKey="jhipsterSampleApplicationApp.promotion.createAt">Create At</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.createAt ? <TextFormat value={promotionEntity.createAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="modifyAt">
              <Translate contentKey="jhipsterSampleApplicationApp.promotion.modifyAt">Modify At</Translate>
            </span>
          </dt>
          <dd>{promotionEntity.modifyAt ? <TextFormat value={promotionEntity.modifyAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/promotion" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/promotion/${promotionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PromotionDetail;
