import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'condominium',
        data: { pageTitle: 'condominiumgwApp.condominium.home.title' },
        loadChildren: () => import('./condominium/condominium.module').then(m => m.CondominiumModule),
      },
      {
        path: 'cuota',
        data: { pageTitle: 'condominiumgwApp.cuota.home.title' },
        loadChildren: () => import('./cuota/cuota.module').then(m => m.CuotaModule),
      },
      {
        path: 'pago',
        data: { pageTitle: 'condominiumgwApp.pago.home.title' },
        loadChildren: () => import('./pago/pago.module').then(m => m.PagoModule),
      },
      {
        path: 'property',
        data: { pageTitle: 'condominiumgwApp.property.home.title' },
        loadChildren: () => import('./property/property.module').then(m => m.PropertyModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
