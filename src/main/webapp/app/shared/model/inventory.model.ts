import { IProduct } from 'app/shared/model/product.model';

export interface IInventory {
  id?: number;
  unit?: number;
  product?: IProduct | null;
}

export const defaultValue: Readonly<IInventory> = {};
