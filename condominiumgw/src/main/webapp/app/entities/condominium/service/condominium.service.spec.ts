import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICondominium, Condominium } from '../condominium.model';

import { CondominiumService } from './condominium.service';

describe('Condominium Service', () => {
  let service: CondominiumService;
  let httpMock: HttpTestingController;
  let elemDefault: ICondominium;
  let expectedResult: ICondominium | ICondominium[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CondominiumService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      direccion: 'AAAAAAA',
      logo: 'AAAAAAA',
      latitud: 0,
      longitud: 0,
      estado: false,
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

    it('should create a Condominium', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Condominium()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Condominium', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          direccion: 'BBBBBB',
          logo: 'BBBBBB',
          latitud: 1,
          longitud: 1,
          estado: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Condominium', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          direccion: 'BBBBBB',
          logo: 'BBBBBB',
          longitud: 1,
          estado: true,
        },
        new Condominium()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Condominium', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          direccion: 'BBBBBB',
          logo: 'BBBBBB',
          latitud: 1,
          longitud: 1,
          estado: true,
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

    it('should delete a Condominium', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCondominiumToCollectionIfMissing', () => {
      it('should add a Condominium to an empty array', () => {
        const condominium: ICondominium = { id: 123 };
        expectedResult = service.addCondominiumToCollectionIfMissing([], condominium);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(condominium);
      });

      it('should not add a Condominium to an array that contains it', () => {
        const condominium: ICondominium = { id: 123 };
        const condominiumCollection: ICondominium[] = [
          {
            ...condominium,
          },
          { id: 456 },
        ];
        expectedResult = service.addCondominiumToCollectionIfMissing(condominiumCollection, condominium);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Condominium to an array that doesn't contain it", () => {
        const condominium: ICondominium = { id: 123 };
        const condominiumCollection: ICondominium[] = [{ id: 456 }];
        expectedResult = service.addCondominiumToCollectionIfMissing(condominiumCollection, condominium);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(condominium);
      });

      it('should add only unique Condominium to an array', () => {
        const condominiumArray: ICondominium[] = [{ id: 123 }, { id: 456 }, { id: 90337 }];
        const condominiumCollection: ICondominium[] = [{ id: 123 }];
        expectedResult = service.addCondominiumToCollectionIfMissing(condominiumCollection, ...condominiumArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const condominium: ICondominium = { id: 123 };
        const condominium2: ICondominium = { id: 456 };
        expectedResult = service.addCondominiumToCollectionIfMissing([], condominium, condominium2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(condominium);
        expect(expectedResult).toContain(condominium2);
      });

      it('should accept null and undefined values', () => {
        const condominium: ICondominium = { id: 123 };
        expectedResult = service.addCondominiumToCollectionIfMissing([], null, condominium, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(condominium);
      });

      it('should return initial array if no Condominium is added', () => {
        const condominiumCollection: ICondominium[] = [{ id: 123 }];
        expectedResult = service.addCondominiumToCollectionIfMissing(condominiumCollection, undefined, null);
        expect(expectedResult).toEqual(condominiumCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
