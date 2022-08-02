import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CuotaDetailComponent } from './cuota-detail.component';

describe('Cuota Management Detail Component', () => {
  let comp: CuotaDetailComponent;
  let fixture: ComponentFixture<CuotaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CuotaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cuota: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CuotaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CuotaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cuota on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cuota).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
