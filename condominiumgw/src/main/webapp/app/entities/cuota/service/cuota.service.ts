import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICuota, getCuotaIdentifier } from '../cuota.model';

export type EntityResponseType = HttpResponse<ICuota>;
export type EntityArrayResponseType = HttpResponse<ICuota[]>;

@Injectable({ providedIn: 'root' })
export class CuotaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cuotas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cuota: ICuota): Observable<EntityResponseType> {
    return this.http.post<ICuota>(this.resourceUrl, cuota, { observe: 'response' });
  }

  update(cuota: ICuota): Observable<EntityResponseType> {
    return this.http.put<ICuota>(`${this.resourceUrl}/${getCuotaIdentifier(cuota) as number}`, cuota, { observe: 'response' });
  }

  partialUpdate(cuota: ICuota): Observable<EntityResponseType> {
    return this.http.patch<ICuota>(`${this.resourceUrl}/${getCuotaIdentifier(cuota) as number}`, cuota, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICuota>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICuota[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCuotaToCollectionIfMissing(cuotaCollection: ICuota[], ...cuotasToCheck: (ICuota | null | undefined)[]): ICuota[] {
    const cuotas: ICuota[] = cuotasToCheck.filter(isPresent);
    if (cuotas.length > 0) {
      const cuotaCollectionIdentifiers = cuotaCollection.map(cuotaItem => getCuotaIdentifier(cuotaItem)!);
      const cuotasToAdd = cuotas.filter(cuotaItem => {
        const cuotaIdentifier = getCuotaIdentifier(cuotaItem);
        if (cuotaIdentifier == null || cuotaCollectionIdentifiers.includes(cuotaIdentifier)) {
          return false;
        }
        cuotaCollectionIdentifiers.push(cuotaIdentifier);
        return true;
      });
      return [...cuotasToAdd, ...cuotaCollection];
    }
    return cuotaCollection;
  }
}
