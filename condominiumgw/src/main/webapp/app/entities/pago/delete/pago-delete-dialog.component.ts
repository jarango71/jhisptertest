import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPago } from '../pago.model';
import { PagoService } from '../service/pago.service';

@Component({
  templateUrl: './pago-delete-dialog.component.html',
})
export class PagoDeleteDialogComponent {
  pago?: IPago;

  constructor(protected pagoService: PagoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pagoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
