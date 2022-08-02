import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProperty, Property } from '../property.model';
import { PropertyService } from '../service/property.service';
import { ICondominium } from 'app/entities/condominium/condominium.model';
import { CondominiumService } from 'app/entities/condominium/service/condominium.service';
import { RegisterState } from 'app/entities/enumerations/register-state.model';

@Component({
  selector: 'app-property-update',
  templateUrl: './property-update.component.html',
})
export class PropertyUpdateComponent implements OnInit {
  isSaving = false;
  registerStateValues = Object.keys(RegisterState);

  condominiumsSharedCollection: ICondominium[] = [];

  editForm = this.fb.group({
    id: [],
    manzana: [null, [Validators.required, Validators.maxLength(15)]],
    bloque: [null, [Validators.required, Validators.maxLength(15)]],
    numero: [null, [Validators.required, Validators.maxLength(10)]],
    ubicacion: [null, [Validators.required, Validators.maxLength(100)]],
    tipo: [null, [Validators.required, Validators.maxLength(20)]],
    diponibilidad: [null, [Validators.required, Validators.maxLength(20)]],
    observacion: [null, [Validators.required, Validators.maxLength(200)]],
    estado: [null, [Validators.required]],
    condominium: [null, Validators.required],
  });

  constructor(
    protected propertyService: PropertyService,
    protected condominiumService: CondominiumService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ property }) => {
      this.updateForm(property);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const property = this.createFromForm();
    if (property.id !== undefined) {
      this.subscribeToSaveResponse(this.propertyService.update(property));
    } else {
      this.subscribeToSaveResponse(this.propertyService.create(property));
    }
  }

  trackCondominiumById(_index: number, item: ICondominium): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProperty>>): void {
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

  protected updateForm(property: IProperty): void {
    this.editForm.patchValue({
      id: property.id,
      manzana: property.manzana,
      bloque: property.bloque,
      numero: property.numero,
      ubicacion: property.ubicacion,
      tipo: property.tipo,
      diponibilidad: property.diponibilidad,
      observacion: property.observacion,
      estado: property.estado,
      condominium: property.condominium,
    });

    this.condominiumsSharedCollection = this.condominiumService.addCondominiumToCollectionIfMissing(
      this.condominiumsSharedCollection,
      property.condominium
    );
  }

  protected loadRelationshipsOptions(): void {
    this.condominiumService
      .query()
      .pipe(map((res: HttpResponse<ICondominium[]>) => res.body ?? []))
      .pipe(
        map((condominiums: ICondominium[]) =>
          this.condominiumService.addCondominiumToCollectionIfMissing(condominiums, this.editForm.get('condominium')!.value)
        )
      )
      .subscribe((condominiums: ICondominium[]) => (this.condominiumsSharedCollection = condominiums));
  }

  protected createFromForm(): IProperty {
    return {
      ...new Property(),
      id: this.editForm.get(['id'])!.value,
      manzana: this.editForm.get(['manzana'])!.value,
      bloque: this.editForm.get(['bloque'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      ubicacion: this.editForm.get(['ubicacion'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      diponibilidad: this.editForm.get(['diponibilidad'])!.value,
      observacion: this.editForm.get(['observacion'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      condominium: this.editForm.get(['condominium'])!.value,
    };
  }
}
