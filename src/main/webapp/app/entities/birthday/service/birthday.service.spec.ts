import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBirthday } from '../birthday.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../birthday.test-samples';

import { BirthdayService } from './birthday.service';

const requireRestSample: IBirthday = {
  ...sampleWithRequiredData,
};

describe('Birthday Service', () => {
  let service: BirthdayService;
  let httpMock: HttpTestingController;
  let expectedResult: IBirthday | IBirthday[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BirthdayService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Birthday', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const birthday = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(birthday).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Birthday', () => {
      const birthday = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(birthday).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Birthday', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Birthday', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Birthday', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBirthdayToCollectionIfMissing', () => {
      it('should add a Birthday to an empty array', () => {
        const birthday: IBirthday = sampleWithRequiredData;
        expectedResult = service.addBirthdayToCollectionIfMissing([], birthday);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(birthday);
      });

      it('should not add a Birthday to an array that contains it', () => {
        const birthday: IBirthday = sampleWithRequiredData;
        const birthdayCollection: IBirthday[] = [
          {
            ...birthday,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBirthdayToCollectionIfMissing(birthdayCollection, birthday);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Birthday to an array that doesn't contain it", () => {
        const birthday: IBirthday = sampleWithRequiredData;
        const birthdayCollection: IBirthday[] = [sampleWithPartialData];
        expectedResult = service.addBirthdayToCollectionIfMissing(birthdayCollection, birthday);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(birthday);
      });

      it('should add only unique Birthday to an array', () => {
        const birthdayArray: IBirthday[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const birthdayCollection: IBirthday[] = [sampleWithRequiredData];
        expectedResult = service.addBirthdayToCollectionIfMissing(birthdayCollection, ...birthdayArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const birthday: IBirthday = sampleWithRequiredData;
        const birthday2: IBirthday = sampleWithPartialData;
        expectedResult = service.addBirthdayToCollectionIfMissing([], birthday, birthday2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(birthday);
        expect(expectedResult).toContain(birthday2);
      });

      it('should accept null and undefined values', () => {
        const birthday: IBirthday = sampleWithRequiredData;
        expectedResult = service.addBirthdayToCollectionIfMissing([], null, birthday, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(birthday);
      });

      it('should return initial array if no Birthday is added', () => {
        const birthdayCollection: IBirthday[] = [sampleWithRequiredData];
        expectedResult = service.addBirthdayToCollectionIfMissing(birthdayCollection, undefined, null);
        expect(expectedResult).toEqual(birthdayCollection);
      });
    });

    describe('compareBirthday', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBirthday(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBirthday(entity1, entity2);
        const compareResult2 = service.compareBirthday(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBirthday(entity1, entity2);
        const compareResult2 = service.compareBirthday(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBirthday(entity1, entity2);
        const compareResult2 = service.compareBirthday(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
