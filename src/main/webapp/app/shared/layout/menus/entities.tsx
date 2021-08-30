import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/customer-details">
      <Translate contentKey="global.menu.entities.customerDetails" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/product">
      <Translate contentKey="global.menu.entities.product" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/inventory">
      <Translate contentKey="global.menu.entities.inventory" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/order">
      <Translate contentKey="global.menu.entities.order" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/order-info">
      <Translate contentKey="global.menu.entities.orderInfo" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/promotion">
      <Translate contentKey="global.menu.entities.promotion" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/promotion-info">
      <Translate contentKey="global.menu.entities.promotionInfo" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
