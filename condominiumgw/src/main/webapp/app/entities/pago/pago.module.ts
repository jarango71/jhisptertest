import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PagoComponent } from './list/pago.component';
import { PagoDetailComponent } from './detail/pago-detail.component';
import { PagoUpdateComponent } from './update/pago-update.component';
import { PagoDeleteDialogComponent } from './delete/pago-delete-dialog.component';
import { PagoRoutingModule } from './route/pago-routing.module';

@NgModule({
  imports: [SharedModule, PagoRoutingModule],
  declarations: [PagoComponent, PagoDetailComponent, PagoUpdateComponent, PagoDeleteDialogComponent],
  entryComponents: [PagoDeleteDialogComponent],
})
export class PagoModule {}
