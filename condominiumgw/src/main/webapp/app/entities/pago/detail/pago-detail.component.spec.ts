import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PagoDetailComponent } from './pago-detail.component';

describe('Pago Management Detail Component', () => {
  let comp: PagoDetailComponent;
  let fixture: ComponentFixture<PagoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PagoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pago: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PagoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PagoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pago on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pago).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
