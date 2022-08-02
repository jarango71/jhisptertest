import { CuotaType } from 'app/entities/enumerations/cuota-type.model';
import { RegisterState } from 'app/entities/enumerations/register-state.model';

export interface ICuota {
  id?: number;
  nombre?: string;
  tipo?: CuotaType;
  periodicidad?: string;
  aplica?: string;
  monto?: number;
  diponibilidad?: string;
  observacion?: string;
  estado?: RegisterState;
}

export class Cuota implements ICuota {
  constructor(
    public id?: number,
    public nombre?: string,
    public tipo?: CuotaType,
    public periodicidad?: string,
    public aplica?: string,
    public monto?: number,
    public diponibilidad?: string,
    public observacion?: string,
    public estado?: RegisterState
  ) {}
}

export function getCuotaIdentifier(cuota: ICuota): number | undefined {
  return cuota.id;
}
