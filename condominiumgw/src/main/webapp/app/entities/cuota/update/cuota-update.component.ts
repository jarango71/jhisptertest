import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICuota, Cuota } from '../cuota.model';
import { CuotaService } from '../service/cuota.service';
import { CuotaType } from 'app/entities/enumerations/cuota-type.model';
import { RegisterState } from 'app/entities/enumerations/register-state.model';

@Component({
  selector: 'app-cuota-update',
  templateUrl: './cuota-update.component.html',
})
export class CuotaUpdateComponent implements OnInit {
  isSaving = false;
  cuotaTypeValues = Object.keys(CuotaType);
  registerStateValues = Object.keys(RegisterState);

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.maxLength(50)]],
    tipo: [null, [Validators.required]],
    periodicidad: [null, [Validators.required, Validators.maxLength(25)]],
    aplica: [null, [Validators.required, Validators.maxLength(50)]],
    monto: [null, [Validators.required]],
    diponibilidad: [null, [Validators.required, Validators.maxLength(20)]],
    observacion: [null, [Validators.required, Validators.maxLength(200)]],
    estado: [null, [Validators.required]],
  });

  constructor(protected cuotaService: CuotaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cuota }) => {
      this.updateForm(cuota);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cuota = this.createFromForm();
    if (cuota.id !== undefined) {
      this.subscribeToSaveResponse(this.cuotaService.update(cuota));
    } else {
      this.subscribeToSaveResponse(this.cuotaService.create(cuota));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICuota>>): void {
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

  protected updateForm(cuota: ICuota): void {
    this.editForm.patchValue({
      id: cuota.id,
      nombre: cuota.nombre,
      tipo: cuota.tipo,
      periodicidad: cuota.periodicidad,
      aplica: cuota.aplica,
      monto: cuota.monto,
      diponibilidad: cuota.diponibilidad,
      observacion: cuota.observacion,
      estado: cuota.estado,
    });
  }

  protected createFromForm(): ICuota {
    return {
      ...new Cuota(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      periodicidad: this.editForm.get(['periodicidad'])!.value,
      aplica: this.editForm.get(['aplica'])!.value,
      monto: this.editForm.get(['monto'])!.value,
      diponibilidad: this.editForm.get(['diponibilidad'])!.value,
      observacion: this.editForm.get(['observacion'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
