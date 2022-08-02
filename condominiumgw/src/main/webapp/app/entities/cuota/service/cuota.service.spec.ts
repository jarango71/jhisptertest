import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { CuotaType } from 'app/entities/enumerations/cuota-type.model';
import { RegisterState } from 'app/entities/enumerations/register-state.model';
import { ICuota, Cuota } from '../cuota.model';

import { CuotaService } from './cuota.service';

describe('Cuota Service', () => {
  let service: CuotaService;
  let httpMock: HttpTestingController;
  let elemDefault: ICuota;
  let expectedResult: ICuota | ICuota[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CuotaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      tipo: CuotaType.ORDINARIA,
      periodicidad: 'AAAAAAA',
      aplica: 'AAAAAAA',
      monto: 0,
      diponibilidad: 'AAAAAAA',
      observacion: 'AAAAAAA',
      estado: RegisterState.ACTIVO,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Cuota', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Cuota()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cuota', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          tipo: 'BBBBBB',
          periodicidad: 'BBBBBB',
          aplica: 'BBBBBB',
          monto: 1,
          diponibilidad: 'BBBBBB',
          observacion: 'BBBBBB',
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cuota', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          periodicidad: 'BBBBBB',
          monto: 1,
          estado: 'BBBBBB',
        },
        new Cuota()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cuota', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          tipo: 'BBBBBB',
          periodicidad: 'BBBBBB',
          aplica: 'BBBBBB',
          monto: 1,
          diponibilidad: 'BBBBBB',
          observacion: 'BBBBBB',
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Cuota', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCuotaToCollectionIfMissing', () => {
      it('should add a Cuota to an empty array', () => {
        const cuota: ICuota = { id: 123 };
        expectedResult = service.addCuotaToCollectionIfMissing([], cuota);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cuota);
      });

      it('should not add a Cuota to an array that contains it', () => {
        const cuota: ICuota = { id: 123 };
        const cuotaCollection: ICuota[] = [
          {
            ...cuota,
          },
          { id: 456 },
        ];
        expectedResult = service.addCuotaToCollectionIfMissing(cuotaCollection, cuota);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cuota to an array that doesn't contain it", () => {
        const cuota: ICuota = { id: 123 };
        const cuotaCollection: ICuota[] = [{ id: 456 }];
        expectedResult = service.addCuotaToCollectionIfMissing(cuotaCollection, cuota);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cuota);
      });

      it('should add only unique Cuota to an array', () => {
        const cuotaArray: ICuota[] = [{ id: 123 }, { id: 456 }, { id: 84426 }];
        const cuotaCollection: ICuota[] = [{ id: 123 }];
        expectedResult = service.addCuotaToCollectionIfMissing(cuotaCollection, ...cuotaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cuota: ICuota = { id: 123 };
        const cuota2: ICuota = { id: 456 };
        expectedResult = service.addCuotaToCollectionIfMissing([], cuota, cuota2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cuota);
        expect(expectedResult).toContain(cuota2);
      });

      it('should accept null and undefined values', () => {
        const cuota: ICuota = { id: 123 };
        expectedResult = service.addCuotaToCollectionIfMissing([], null, cuota, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cuota);
      });

      it('should return initial array if no Cuota is added', () => {
        const cuotaCollection: ICuota[] = [{ id: 123 }];
        expectedResult = service.addCuotaToCollectionIfMissing(cuotaCollection, undefined, null);
        expect(expectedResult).toEqual(cuotaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
