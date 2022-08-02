import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPago } from '../pago.model';

@Component({
  selector: 'app-pago-detail',
  templateUrl: './pago-detail.component.html',
})
export class PagoDetailComponent implements OnInit {
  pago: IPago | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pago }) => {
      this.pago = pago;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
