import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CustomerDetails from './customer-details';
import Product from './product';
import Inventory from './inventory';
import Order from './order';
import OrderInfo from './order-info';
import Promotion from './promotion';
import PromotionInfo from './promotion-info';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}customer-details`} component={CustomerDetails} />
      <ErrorBoundaryRoute path={`${match.url}product`} component={Product} />
      <ErrorBoundaryRoute path={`${match.url}inventory`} component={Inventory} />
      <ErrorBoundaryRoute path={`${match.url}order`} component={Order} />
      <ErrorBoundaryRoute path={`${match.url}order-info`} component={OrderInfo} />
      <ErrorBoundaryRoute path={`${match.url}promotion`} component={Promotion} />
      <ErrorBoundaryRoute path={`${match.url}promotion-info`} component={PromotionInfo} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
