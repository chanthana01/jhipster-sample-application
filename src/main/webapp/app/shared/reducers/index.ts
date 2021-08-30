import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import customerDetails from 'app/entities/customer-details/customer-details.reducer';
// prettier-ignore
import product from 'app/entities/product/product.reducer';
// prettier-ignore
import inventory from 'app/entities/inventory/inventory.reducer';
// prettier-ignore
import order from 'app/entities/order/order.reducer';
// prettier-ignore
import orderInfo from 'app/entities/order-info/order-info.reducer';
// prettier-ignore
import promotion from 'app/entities/promotion/promotion.reducer';
// prettier-ignore
import promotionInfo from 'app/entities/promotion-info/promotion-info.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  customerDetails,
  product,
  inventory,
  order,
  orderInfo,
  promotion,
  promotionInfo,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
