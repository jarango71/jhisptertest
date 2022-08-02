import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPago, getPagoIdentifier } from '../pago.model';

export type EntityResponseType = HttpResponse<IPago>;
export type EntityArrayResponseType = HttpResponse<IPago[]>;

@Injectable({ providedIn: 'root' })
export class PagoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pagos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pago: IPago): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pago);
    return this.http
      .post<IPago>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(pago: IPago): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pago);
    return this.http
      .put<IPago>(`${this.resourceUrl}/${getPagoIdentifier(pago) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(pago: IPago): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pago);
    return this.http
      .patch<IPago>(`${this.resourceUrl}/${getPagoIdentifier(pago) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPago>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPago[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPagoToCollectionIfMissing(pagoCollection: IPago[], ...pagosToCheck: (IPago | null | undefined)[]): IPago[] {
    const pagos: IPago[] = pagosToCheck.filter(isPresent);
    if (pagos.length > 0) {
      const pagoCollectionIdentifiers = pagoCollection.map(pagoItem => getPagoIdentifier(pagoItem)!);
      const pagosToAdd = pagos.filter(pagoItem => {
        const pagoIdentifier = getPagoIdentifier(pagoItem);
        if (pagoIdentifier == null || pagoCollectionIdentifiers.includes(pagoIdentifier)) {
          return false;
        }
        pagoCollectionIdentifiers.push(pagoIdentifier);
        return true;
      });
      return [...pagosToAdd, ...pagoCollection];
    }
    return pagoCollection;
  }

  protected convertDateFromClient(pago: IPago): IPago {
    return Object.assign({}, pago, {
      fechaGeneracion: pago.fechaGeneracion?.isValid() ? pago.fechaGeneracion.format(DATE_FORMAT) : undefined,
      fechaPago: pago.fechaPago?.isValid() ? pago.fechaPago.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaGeneracion = res.body.fechaGeneracion ? dayjs(res.body.fechaGeneracion) : undefined;
      res.body.fechaPago = res.body.fechaPago ? dayjs(res.body.fechaPago) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pago: IPago) => {
        pago.fechaGeneracion = pago.fechaGeneracion ? dayjs(pago.fechaGeneracion) : undefined;
        pago.fechaPago = pago.fechaPago ? dayjs(pago.fechaPago) : undefined;
      });
    }
    return res;
  }
}
