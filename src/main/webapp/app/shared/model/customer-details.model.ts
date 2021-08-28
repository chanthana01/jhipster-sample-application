import { IUser } from 'app/shared/model/user.model';

export interface ICustomerDetails {
  id?: number;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string | null;
  city?: string;
  country?: string;
  user?: IUser;
}

export const defaultValue: Readonly<ICustomerDetails> = {};
