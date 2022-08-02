export interface ICondominium {
  id?: number;
  nombre?: string;
  direccion?: string;
  logo?: string | null;
  latitud?: number | null;
  longitud?: number | null;
  estado?: boolean;
}

export class Condominium implements ICondominium {
  constructor(
    public id?: number,
    public nombre?: string,
    public direccion?: string,
    public logo?: string | null,
    public latitud?: number | null,
    public longitud?: number | null,
    public estado?: boolean
  ) {
    this.estado = this.estado ?? false;
  }
}

export function getCondominiumIdentifier(condominium: ICondominium): number | undefined {
  return condominium.id;
}
