import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICuota } from '../cuota.model';
import { CuotaService } from '../service/cuota.service';

@Component({
  templateUrl: './cuota-delete-dialog.component.html',
})
export class CuotaDeleteDialogComponent {
  cuota?: ICuota;

  constructor(protected cuotaService: CuotaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cuotaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
