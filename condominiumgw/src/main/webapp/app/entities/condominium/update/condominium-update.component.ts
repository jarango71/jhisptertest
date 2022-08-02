import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICondominium, Condominium } from '../condominium.model';
import { CondominiumService } from '../service/condominium.service';

@Component({
  selector: 'app-condominium-update',
  templateUrl: './condominium-update.component.html',
})
export class CondominiumUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.maxLength(50)]],
    direccion: [null, [Validators.required, Validators.maxLength(50)]],
    logo: [null, [Validators.maxLength(50)]],
    latitud: [],
    longitud: [],
    estado: [null, [Validators.required]],
  });

  constructor(protected condominiumService: CondominiumService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ condominium }) => {
      this.updateForm(condominium);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const condominium = this.createFromForm();
    if (condominium.id !== undefined) {
      this.subscribeToSaveResponse(this.condominiumService.update(condominium));
    } else {
      this.subscribeToSaveResponse(this.condominiumService.create(condominium));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICondominium>>): void {
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

  protected updateForm(condominium: ICondominium): void {
    this.editForm.patchValue({
      id: condominium.id,
      nombre: condominium.nombre,
      direccion: condominium.direccion,
      logo: condominium.logo,
      latitud: condominium.latitud,
      longitud: condominium.longitud,
      estado: condominium.estado,
    });
  }

  protected createFromForm(): ICondominium {
    return {
      ...new Condominium(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      logo: this.editForm.get(['logo'])!.value,
      latitud: this.editForm.get(['latitud'])!.value,
      longitud: this.editForm.get(['longitud'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
