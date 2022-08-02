import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CondominiumComponent } from './list/condominium.component';
import { CondominiumDetailComponent } from './detail/condominium-detail.component';
import { CondominiumUpdateComponent } from './update/condominium-update.component';
import { CondominiumDeleteDialogComponent } from './delete/condominium-delete-dialog.component';
import { CondominiumRoutingModule } from './route/condominium-routing.module';

@NgModule({
  imports: [SharedModule, CondominiumRoutingModule],
  declarations: [CondominiumComponent, CondominiumDetailComponent, CondominiumUpdateComponent, CondominiumDeleteDialogComponent],
  entryComponents: [CondominiumDeleteDialogComponent],
})
export class CondominiumModule {}
