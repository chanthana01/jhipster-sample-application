import dayjs from 'dayjs';
import { IPromotionInfo } from 'app/shared/model/promotion-info.model';
import { IProduct } from 'app/shared/model/product.model';
import { IOrder } from 'app/shared/model/order.model';

export interface IOrderInfo {
  id?: number;
  quantity?: number;
  pricePerUnit?: number;
  createAt?: string;
  modifyAt?: string;
  promotionInfo?: IPromotionInfo | null;
  product?: IProduct | null;
  order?: IOrder | null;
}

export const defaultValue: Readonly<IOrderInfo> = {};
