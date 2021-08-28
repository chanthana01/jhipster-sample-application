import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './product.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProductDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const productEntity = useAppSelector(state => state.product.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.product.detail.title">Product</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="jhipsterSampleApplicationApp.product.code">Code</Translate>
            </span>
          </dt>
          <dd>{productEntity.code}</dd>
          <dt>
            <span id="category">
              <Translate contentKey="jhipsterSampleApplicationApp.product.category">Category</Translate>
            </span>
          </dt>
          <dd>{productEntity.category}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterSampleApplicationApp.product.name">Name</Translate>
            </span>
          </dt>
          <dd>{productEntity.name}</dd>
          <dt>
            <span id="family">
              <Translate contentKey="jhipsterSampleApplicationApp.product.family">Family</Translate>
            </span>
          </dt>
          <dd>{productEntity.family}</dd>
          <dt>
            <span id="detail1">
              <Translate contentKey="jhipsterSampleApplicationApp.product.detail1">Detail 1</Translate>
            </span>
          </dt>
          <dd>{productEntity.detail1}</dd>
          <dt>
            <span id="detail2">
              <Translate contentKey="jhipsterSampleApplicationApp.product.detail2">Detail 2</Translate>
            </span>
          </dt>
          <dd>{productEntity.detail2}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="jhipsterSampleApplicationApp.product.price">Price</Translate>
            </span>
          </dt>
          <dd>{productEntity.price}</dd>
          <dt>
            <span id="createAt">
              <Translate contentKey="jhipsterSampleApplicationApp.product.createAt">Create At</Translate>
            </span>
          </dt>
          <dd>{productEntity.createAt ? <TextFormat value={productEntity.createAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="modifyAt">
              <Translate contentKey="jhipsterSampleApplicationApp.product.modifyAt">Modify At</Translate>
            </span>
          </dt>
          <dd>{productEntity.modifyAt ? <TextFormat value={productEntity.modifyAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.product.inventory">Inventory</Translate>
          </dt>
          <dd>{productEntity.inventory ? productEntity.inventory.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/product" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product/${productEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductDetail;
