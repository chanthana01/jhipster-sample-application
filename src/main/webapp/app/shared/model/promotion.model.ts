import dayjs from 'dayjs';
import { IPromotionInfo } from 'app/shared/model/promotion-info.model';

export interface IPromotion {
  id?: number;
  promotionDescription?: string;
  promorionFormular?: string;
  expireAt?: string | null;
  createAt?: string;
  modifyAt?: string;
  promotionInfos?: IPromotionInfo[] | null;
}

export const defaultValue: Readonly<IPromotion> = {};
