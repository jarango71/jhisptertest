import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CondominiumDetailComponent } from './condominium-detail.component';

describe('Condominium Management Detail Component', () => {
  let comp: CondominiumDetailComponent;
  let fixture: ComponentFixture<CondominiumDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CondominiumDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ condominium: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CondominiumDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CondominiumDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load condominium on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.condominium).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
