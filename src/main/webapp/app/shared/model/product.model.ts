import dayjs from 'dayjs';
import { IInventory } from 'app/shared/model/inventory.model';
import { IPromotionInfo } from 'app/shared/model/promotion-info.model';

export interface IProduct {
  id?: number;
  code?: string;
  category?: string;
  name?: string;
  family?: string;
  detail1?: string | null;
  detail2?: string | null;
  price?: number;
  createAt?: string;
  modifyAt?: string;
  inventory?: IInventory | null;
  promotionInfos?: IPromotionInfo[] | null;
}

export const defaultValue: Readonly<IProduct> = {};
