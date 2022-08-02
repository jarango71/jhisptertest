import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PagoService } from '../service/pago.service';
import { IPago, Pago } from '../pago.model';
import { ICuota } from 'app/entities/cuota/cuota.model';
import { CuotaService } from 'app/entities/cuota/service/cuota.service';

import { PagoUpdateComponent } from './pago-update.component';

describe('Pago Management Update Component', () => {
  let comp: PagoUpdateComponent;
  let fixture: ComponentFixture<PagoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pagoService: PagoService;
  let cuotaService: CuotaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PagoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PagoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PagoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pagoService = TestBed.inject(PagoService);
    cuotaService = TestBed.inject(CuotaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cuota query and add missing value', () => {
      const pago: IPago = { id: 456 };
      const cuota: ICuota = { id: 25809 };
      pago.cuota = cuota;

      const cuotaCollection: ICuota[] = [{ id: 50123 }];
      jest.spyOn(cuotaService, 'query').mockReturnValue(of(new HttpResponse({ body: cuotaCollection })));
      const additionalCuotas = [cuota];
      const expectedCollection: ICuota[] = [...additionalCuotas, ...cuotaCollection];
      jest.spyOn(cuotaService, 'addCuotaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pago });
      comp.ngOnInit();

      expect(cuotaService.query).toHaveBeenCalled();
      expect(cuotaService.addCuotaToCollectionIfMissing).toHaveBeenCalledWith(cuotaCollection, ...additionalCuotas);
      expect(comp.cuotasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pago: IPago = { id: 456 };
      const cuota: ICuota = { id: 44427 };
      pago.cuota = cuota;

      activatedRoute.data = of({ pago });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(pago));
      expect(comp.cuotasSharedCollection).toContain(cuota);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pago>>();
      const pago = { id: 123 };
      jest.spyOn(pagoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pago });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pago }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(pagoService.update).toHaveBeenCalledWith(pago);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pago>>();
      const pago = new Pago();
      jest.spyOn(pagoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pago });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pago }));
      saveSubject.complete();

      // THEN
      expect(pagoService.create).toHaveBeenCalledWith(pago);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pago>>();
      const pago = { id: 123 };
      jest.spyOn(pagoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pago });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pagoService.update).toHaveBeenCalledWith(pago);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCuotaById', () => {
      it('Should return tracked Cuota primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCuotaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
