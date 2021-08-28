import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PromotionInfo from './promotion-info';
import PromotionInfoDetail from './promotion-info-detail';
import PromotionInfoUpdate from './promotion-info-update';
import PromotionInfoDeleteDialog from './promotion-info-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PromotionInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PromotionInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PromotionInfoDetail} />
      <ErrorBoundaryRoute path={match.url} component={PromotionInfo} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PromotionInfoDeleteDialog} />
  </>
);

export default Routes;
