import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICondominium, Condominium } from '../condominium.model';
import { CondominiumService } from '../service/condominium.service';

@Injectable({ providedIn: 'root' })
export class CondominiumRoutingResolveService implements Resolve<ICondominium> {
  constructor(protected service: CondominiumService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICondominium> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((condominium: HttpResponse<Condominium>) => {
          if (condominium.body) {
            return of(condominium.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Condominium());
  }
}
