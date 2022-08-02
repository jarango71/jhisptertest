import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { RegisterState } from 'app/entities/enumerations/register-state.model';
import { IPago, Pago } from '../pago.model';

import { PagoService } from './pago.service';

describe('Pago Service', () => {
  let service: PagoService;
  let httpMock: HttpTestingController;
  let elemDefault: IPago;
  let expectedResult: IPago | IPago[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PagoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      anio: 'AAAAAAA',
      mes: 'AAAAAAA',
      valor: 0,
      estado: RegisterState.ACTIVO,
      fechaGeneracion: currentDate,
      fechaPago: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaGeneracion: currentDate.format(DATE_FORMAT),
          fechaPago: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Pago', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaGeneracion: currentDate.format(DATE_FORMAT),
          fechaPago: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaGeneracion: currentDate,
          fechaPago: currentDate,
        },
        returnedFromService
      );

      service.create(new Pago()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pago', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          anio: 'BBBBBB',
          mes: 'BBBBBB',
          valor: 1,
          estado: 'BBBBBB',
          fechaGeneracion: currentDate.format(DATE_FORMAT),
          fechaPago: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaGeneracion: currentDate,
          fechaPago: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pago', () => {
      const patchObject = Object.assign(
        {
          estado: 'BBBBBB',
          fechaGeneracion: currentDate.format(DATE_FORMAT),
          fechaPago: currentDate.format(DATE_FORMAT),
        },
        new Pago()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaGeneracion: currentDate,
          fechaPago: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pago', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          anio: 'BBBBBB',
          mes: 'BBBBBB',
          valor: 1,
          estado: 'BBBBBB',
          fechaGeneracion: currentDate.format(DATE_FORMAT),
          fechaPago: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaGeneracion: currentDate,
          fechaPago: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Pago', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPagoToCollectionIfMissing', () => {
      it('should add a Pago to an empty array', () => {
        const pago: IPago = { id: 123 };
        expectedResult = service.addPagoToCollectionIfMissing([], pago);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pago);
      });

      it('should not add a Pago to an array that contains it', () => {
        const pago: IPago = { id: 123 };
        const pagoCollection: IPago[] = [
          {
            ...pago,
          },
          { id: 456 },
        ];
        expectedResult = service.addPagoToCollectionIfMissing(pagoCollection, pago);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pago to an array that doesn't contain it", () => {
        const pago: IPago = { id: 123 };
        const pagoCollection: IPago[] = [{ id: 456 }];
        expectedResult = service.addPagoToCollectionIfMissing(pagoCollection, pago);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pago);
      });

      it('should add only unique Pago to an array', () => {
        const pagoArray: IPago[] = [{ id: 123 }, { id: 456 }, { id: 19612 }];
        const pagoCollection: IPago[] = [{ id: 123 }];
        expectedResult = service.addPagoToCollectionIfMissing(pagoCollection, ...pagoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pago: IPago = { id: 123 };
        const pago2: IPago = { id: 456 };
        expectedResult = service.addPagoToCollectionIfMissing([], pago, pago2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pago);
        expect(expectedResult).toContain(pago2);
      });

      it('should accept null and undefined values', () => {
        const pago: IPago = { id: 123 };
        expectedResult = service.addPagoToCollectionIfMissing([], null, pago, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pago);
      });

      it('should return initial array if no Pago is added', () => {
        const pagoCollection: IPago[] = [{ id: 123 }];
        expectedResult = service.addPagoToCollectionIfMissing(pagoCollection, undefined, null);
        expect(expectedResult).toEqual(pagoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
