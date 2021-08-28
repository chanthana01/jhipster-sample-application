import { IPromotion } from 'app/shared/model/promotion.model';
import { IProduct } from 'app/shared/model/product.model';

export interface IPromotionInfo {
  id?: number;
  promotion?: IPromotion | null;
  product?: IProduct | null;
}

export const defaultValue: Readonly<IPromotionInfo> = {};
