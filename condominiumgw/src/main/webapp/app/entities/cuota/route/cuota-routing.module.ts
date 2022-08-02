import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CuotaComponent } from '../list/cuota.component';
import { CuotaDetailComponent } from '../detail/cuota-detail.component';
import { CuotaUpdateComponent } from '../update/cuota-update.component';
import { CuotaRoutingResolveService } from './cuota-routing-resolve.service';

const cuotaRoute: Routes = [
  {
    path: '',
    component: CuotaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CuotaDetailComponent,
    resolve: {
      cuota: CuotaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CuotaUpdateComponent,
    resolve: {
      cuota: CuotaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CuotaUpdateComponent,
    resolve: {
      cuota: CuotaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cuotaRoute)],
  exports: [RouterModule],
})
export class CuotaRoutingModule {}
