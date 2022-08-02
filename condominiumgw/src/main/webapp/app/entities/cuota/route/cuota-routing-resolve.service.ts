import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICuota, Cuota } from '../cuota.model';
import { CuotaService } from '../service/cuota.service';

@Injectable({ providedIn: 'root' })
export class CuotaRoutingResolveService implements Resolve<ICuota> {
  constructor(protected service: CuotaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICuota> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cuota: HttpResponse<Cuota>) => {
          if (cuota.body) {
            return of(cuota.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cuota());
  }
}
