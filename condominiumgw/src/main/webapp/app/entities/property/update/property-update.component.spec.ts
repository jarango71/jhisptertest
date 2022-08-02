import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PropertyService } from '../service/property.service';
import { IProperty, Property } from '../property.model';
import { ICondominium } from 'app/entities/condominium/condominium.model';
import { CondominiumService } from 'app/entities/condominium/service/condominium.service';

import { PropertyUpdateComponent } from './property-update.component';

describe('Property Management Update Component', () => {
  let comp: PropertyUpdateComponent;
  let fixture: ComponentFixture<PropertyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let propertyService: PropertyService;
  let condominiumService: CondominiumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PropertyUpdateComponent],
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
      .overrideTemplate(PropertyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PropertyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    propertyService = TestBed.inject(PropertyService);
    condominiumService = TestBed.inject(CondominiumService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Condominium query and add missing value', () => {
      const property: IProperty = { id: 456 };
      const condominium: ICondominium = { id: 76986 };
      property.condominium = condominium;

      const condominiumCollection: ICondominium[] = [{ id: 18534 }];
      jest.spyOn(condominiumService, 'query').mockReturnValue(of(new HttpResponse({ body: condominiumCollection })));
      const additionalCondominiums = [condominium];
      const expectedCollection: ICondominium[] = [...additionalCondominiums, ...condominiumCollection];
      jest.spyOn(condominiumService, 'addCondominiumToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ property });
      comp.ngOnInit();

      expect(condominiumService.query).toHaveBeenCalled();
      expect(condominiumService.addCondominiumToCollectionIfMissing).toHaveBeenCalledWith(condominiumCollection, ...additionalCondominiums);
      expect(comp.condominiumsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const property: IProperty = { id: 456 };
      const condominium: ICondominium = { id: 61355 };
      property.condominium = condominium;

      activatedRoute.data = of({ property });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(property));
      expect(comp.condominiumsSharedCollection).toContain(condominium);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Property>>();
      const property = { id: 123 };
      jest.spyOn(propertyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ property });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: property }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(propertyService.update).toHaveBeenCalledWith(property);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Property>>();
      const property = new Property();
      jest.spyOn(propertyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ property });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: property }));
      saveSubject.complete();

      // THEN
      expect(propertyService.create).toHaveBeenCalledWith(property);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Property>>();
      const property = { id: 123 };
      jest.spyOn(propertyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ property });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(propertyService.update).toHaveBeenCalledWith(property);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCondominiumById', () => {
      it('Should return tracked Condominium primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCondominiumById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
