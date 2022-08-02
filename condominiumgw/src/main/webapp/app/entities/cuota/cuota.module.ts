import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CuotaComponent } from './list/cuota.component';
import { CuotaDetailComponent } from './detail/cuota-detail.component';
import { CuotaUpdateComponent } from './update/cuota-update.component';
import { CuotaDeleteDialogComponent } from './delete/cuota-delete-dialog.component';
import { CuotaRoutingModule } from './route/cuota-routing.module';

@NgModule({
  imports: [SharedModule, CuotaRoutingModule],
  declarations: [CuotaComponent, CuotaDetailComponent, CuotaUpdateComponent, CuotaDeleteDialogComponent],
  entryComponents: [CuotaDeleteDialogComponent],
})
export class CuotaModule {}
