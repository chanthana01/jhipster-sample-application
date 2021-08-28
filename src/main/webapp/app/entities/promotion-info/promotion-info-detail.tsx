import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './promotion-info.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PromotionInfoDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const promotionInfoEntity = useAppSelector(state => state.promotionInfo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="promotionInfoDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.promotionInfo.detail.title">PromotionInfo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{promotionInfoEntity.id}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.promotionInfo.promotion">Promotion</Translate>
          </dt>
          <dd>{promotionInfoEntity.promotion ? promotionInfoEntity.promotion.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.promotionInfo.product">Product</Translate>
          </dt>
          <dd>{promotionInfoEntity.product ? promotionInfoEntity.product.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/promotion-info" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/promotion-info/${promotionInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PromotionInfoDetail;
