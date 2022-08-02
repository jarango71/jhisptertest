import dayjs from 'dayjs/esm';
import { ICuota } from 'app/entities/cuota/cuota.model';
import { RegisterState } from 'app/entities/enumerations/register-state.model';

export interface IPago {
  id?: number;
  anio?: string;
  mes?: string;
  valor?: number;
  estado?: RegisterState;
  fechaGeneracion?: dayjs.Dayjs;
  fechaPago?: dayjs.Dayjs;
  cuota?: ICuota;
}

export class Pago implements IPago {
  constructor(
    public id?: number,
    public anio?: string,
    public mes?: string,
    public valor?: number,
    public estado?: RegisterState,
    public fechaGeneracion?: dayjs.Dayjs,
    public fechaPago?: dayjs.Dayjs,
    public cuota?: ICuota
  ) {}
}

export function getPagoIdentifier(pago: IPago): number | undefined {
  return pago.id;
}
