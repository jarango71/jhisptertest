import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICondominium } from '../condominium.model';
import { CondominiumService } from '../service/condominium.service';

@Component({
  templateUrl: './condominium-delete-dialog.component.html',
})
export class CondominiumDeleteDialogComponent {
  condominium?: ICondominium;

  constructor(protected condominiumService: CondominiumService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.condominiumService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
