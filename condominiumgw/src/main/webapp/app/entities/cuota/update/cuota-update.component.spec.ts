import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CuotaService } from '../service/cuota.service';
import { ICuota, Cuota } from '../cuota.model';

import { CuotaUpdateComponent } from './cuota-update.component';

describe('Cuota Management Update Component', () => {
  let comp: CuotaUpdateComponent;
  let fixture: ComponentFixture<CuotaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cuotaService: CuotaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CuotaUpdateComponent],
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
      .overrideTemplate(CuotaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CuotaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cuotaService = TestBed.inject(CuotaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cuota: ICuota = { id: 456 };

      activatedRoute.data = of({ cuota });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cuota));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cuota>>();
      const cuota = { id: 123 };
      jest.spyOn(cuotaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cuota });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cuota }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cuotaService.update).toHaveBeenCalledWith(cuota);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cuota>>();
      const cuota = new Cuota();
      jest.spyOn(cuotaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cuota });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cuota }));
      saveSubject.complete();

      // THEN
      expect(cuotaService.create).toHaveBeenCalledWith(cuota);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cuota>>();
      const cuota = { id: 123 };
      jest.spyOn(cuotaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cuota });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cuotaService.update).toHaveBeenCalledWith(cuota);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
