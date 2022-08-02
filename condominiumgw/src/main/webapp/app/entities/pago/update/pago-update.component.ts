import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPago, Pago } from '../pago.model';
import { PagoService } from '../service/pago.service';
import { ICuota } from 'app/entities/cuota/cuota.model';
import { CuotaService } from 'app/entities/cuota/service/cuota.service';
import { RegisterState } from 'app/entities/enumerations/register-state.model';

@Component({
  selector: 'app-pago-update',
  templateUrl: './pago-update.component.html',
})
export class PagoUpdateComponent implements OnInit {
  isSaving = false;
  registerStateValues = Object.keys(RegisterState);

  cuotasSharedCollection: ICuota[] = [];

  editForm = this.fb.group({
    id: [],
    anio: [null, [Validators.required, Validators.maxLength(4)]],
    mes: [null, [Validators.required, Validators.maxLength(50)]],
    valor: [null, [Validators.required]],
    estado: [null, [Validators.required]],
    fechaGeneracion: [null, [Validators.required]],
    fechaPago: [null, [Validators.required]],
    cuota: [null, Validators.required],
  });

  constructor(
    protected pagoService: PagoService,
    protected cuotaService: CuotaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pago }) => {
      this.updateForm(pago);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pago = this.createFromForm();
    if (pago.id !== undefined) {
      this.subscribeToSaveResponse(this.pagoService.update(pago));
    } else {
      this.subscribeToSaveResponse(this.pagoService.create(pago));
    }
  }

  trackCuotaById(_index: number, item: ICuota): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPago>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(pago: IPago): void {
    this.editForm.patchValue({
      id: pago.id,
      anio: pago.anio,
      mes: pago.mes,
      valor: pago.valor,
      estado: pago.estado,
      fechaGeneracion: pago.fechaGeneracion,
      fechaPago: pago.fechaPago,
      cuota: pago.cuota,
    });

    this.cuotasSharedCollection = this.cuotaService.addCuotaToCollectionIfMissing(this.cuotasSharedCollection, pago.cuota);
  }

  protected loadRelationshipsOptions(): void {
    this.cuotaService
      .query()
      .pipe(map((res: HttpResponse<ICuota[]>) => res.body ?? []))
      .pipe(map((cuotas: ICuota[]) => this.cuotaService.addCuotaToCollectionIfMissing(cuotas, this.editForm.get('cuota')!.value)))
      .subscribe((cuotas: ICuota[]) => (this.cuotasSharedCollection = cuotas));
  }

  protected createFromForm(): IPago {
    return {
      ...new Pago(),
      id: this.editForm.get(['id'])!.value,
      anio: this.editForm.get(['anio'])!.value,
      mes: this.editForm.get(['mes'])!.value,
      valor: this.editForm.get(['valor'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      fechaGeneracion: this.editForm.get(['fechaGeneracion'])!.value,
      fechaPago: this.editForm.get(['fechaPago'])!.value,
      cuota: this.editForm.get(['cuota'])!.value,
    };
  }
}
