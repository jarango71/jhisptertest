import { ICondominium } from 'app/entities/condominium/condominium.model';
import { RegisterState } from 'app/entities/enumerations/register-state.model';

export interface IProperty {
  id?: number;
  manzana?: string;
  bloque?: string;
  numero?: string;
  ubicacion?: string;
  tipo?: string;
  diponibilidad?: string;
  observacion?: string;
  estado?: RegisterState;
  condominium?: ICondominium;
}

export class Property implements IProperty {
  constructor(
    public id?: number,
    public manzana?: string,
    public bloque?: string,
    public numero?: string,
    public ubicacion?: string,
    public tipo?: string,
    public diponibilidad?: string,
    public observacion?: string,
    public estado?: RegisterState,
    public condominium?: ICondominium
  ) {}
}

export function getPropertyIdentifier(property: IProperty): number | undefined {
  return property.id;
}
