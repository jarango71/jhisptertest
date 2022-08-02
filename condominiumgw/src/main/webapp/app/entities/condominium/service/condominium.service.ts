import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICondominium, getCondominiumIdentifier } from '../condominium.model';

export type EntityResponseType = HttpResponse<ICondominium>;
export type EntityArrayResponseType = HttpResponse<ICondominium[]>;

@Injectable({ providedIn: 'root' })
export class CondominiumService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/condominiums');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(condominium: ICondominium): Observable<EntityResponseType> {
    return this.http.post<ICondominium>(this.resourceUrl, condominium, { observe: 'response' });
  }

  update(condominium: ICondominium): Observable<EntityResponseType> {
    return this.http.put<ICondominium>(`${this.resourceUrl}/${getCondominiumIdentifier(condominium) as number}`, condominium, {
      observe: 'response',
    });
  }

  partialUpdate(condominium: ICondominium): Observable<EntityResponseType> {
    return this.http.patch<ICondominium>(`${this.resourceUrl}/${getCondominiumIdentifier(condominium) as number}`, condominium, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICondominium>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICondominium[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCondominiumToCollectionIfMissing(
    condominiumCollection: ICondominium[],
    ...condominiumsToCheck: (ICondominium | null | undefined)[]
  ): ICondominium[] {
    const condominiums: ICondominium[] = condominiumsToCheck.filter(isPresent);
    if (condominiums.length > 0) {
      const condominiumCollectionIdentifiers = condominiumCollection.map(condominiumItem => getCondominiumIdentifier(condominiumItem)!);
      const condominiumsToAdd = condominiums.filter(condominiumItem => {
        const condominiumIdentifier = getCondominiumIdentifier(condominiumItem);
        if (condominiumIdentifier == null || condominiumCollectionIdentifiers.includes(condominiumIdentifier)) {
          return false;
        }
        condominiumCollectionIdentifiers.push(condominiumIdentifier);
        return true;
      });
      return [...condominiumsToAdd, ...condominiumCollection];
    }
    return condominiumCollection;
  }
}
