import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PagoComponent } from '../list/pago.component';
import { PagoDetailComponent } from '../detail/pago-detail.component';
import { PagoUpdateComponent } from '../update/pago-update.component';
import { PagoRoutingResolveService } from './pago-routing-resolve.service';

const pagoRoute: Routes = [
  {
    path: '',
    component: PagoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PagoDetailComponent,
    resolve: {
      pago: PagoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PagoUpdateComponent,
    resolve: {
      pago: PagoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PagoUpdateComponent,
    resolve: {
      pago: PagoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pagoRoute)],
  exports: [RouterModule],
})
export class PagoRoutingModule {}
