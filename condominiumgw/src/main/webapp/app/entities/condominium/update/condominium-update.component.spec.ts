import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CondominiumService } from '../service/condominium.service';
import { ICondominium, Condominium } from '../condominium.model';

import { CondominiumUpdateComponent } from './condominium-update.component';

describe('Condominium Management Update Component', () => {
  let comp: CondominiumUpdateComponent;
  let fixture: ComponentFixture<CondominiumUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let condominiumService: CondominiumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CondominiumUpdateComponent],
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
      .overrideTemplate(CondominiumUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CondominiumUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    condominiumService = TestBed.inject(CondominiumService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const condominium: ICondominium = { id: 456 };

      activatedRoute.data = of({ condominium });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(condominium));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Condominium>>();
      const condominium = { id: 123 };
      jest.spyOn(condominiumService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ condominium });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: condominium }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(condominiumService.update).toHaveBeenCalledWith(condominium);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Condominium>>();
      const condominium = new Condominium();
      jest.spyOn(condominiumService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ condominium });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: condominium }));
      saveSubject.complete();

      // THEN
      expect(condominiumService.create).toHaveBeenCalledWith(condominium);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Condominium>>();
      const condominium = { id: 123 };
      jest.spyOn(condominiumService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ condominium });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(condominiumService.update).toHaveBeenCalledWith(condominium);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
