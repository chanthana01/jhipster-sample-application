import dayjs from 'dayjs';
import { IOrderInfo } from 'app/shared/model/order-info.model';

export interface IOrder {
  id?: number;
  customerName?: string;
  orderAddress?: string;
  totalAmount?: number;
  omiseTxnId?: string;
  txnTimeStamp?: string;
  isTxnSuccess?: boolean;
  createAt?: string;
  modifyAt?: string;
  orderInfos?: IOrderInfo[] | null;
}

export const defaultValue: Readonly<IOrder> = {
  isTxnSuccess: false,
};
