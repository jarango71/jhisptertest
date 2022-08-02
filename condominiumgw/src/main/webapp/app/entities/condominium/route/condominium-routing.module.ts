import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CondominiumComponent } from '../list/condominium.component';
import { CondominiumDetailComponent } from '../detail/condominium-detail.component';
import { CondominiumUpdateComponent } from '../update/condominium-update.component';
import { CondominiumRoutingResolveService } from './condominium-routing-resolve.service';

const condominiumRoute: Routes = [
  {
    path: '',
    component: CondominiumComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CondominiumDetailComponent,
    resolve: {
      condominium: CondominiumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CondominiumUpdateComponent,
    resolve: {
      condominium: CondominiumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CondominiumUpdateComponent,
    resolve: {
      condominium: CondominiumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(condominiumRoute)],
  exports: [RouterModule],
})
export class CondominiumRoutingModule {}
