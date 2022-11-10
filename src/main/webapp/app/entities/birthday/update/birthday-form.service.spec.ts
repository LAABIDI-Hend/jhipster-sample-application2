import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../birthday.test-samples';

import { BirthdayFormService } from './birthday-form.service';

describe('Birthday Form Service', () => {
  let service: BirthdayFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BirthdayFormService);
  });

  describe('Service methods', () => {
    describe('createBirthdayFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBirthdayFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            day: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
          })
        );
      });

      it('passing IBirthday should create a new form with FormGroup', () => {
        const formGroup = service.createBirthdayFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            day: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
          })
        );
      });
    });

    describe('getBirthday', () => {
      it('should return NewBirthday for default Birthday initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBirthdayFormGroup(sampleWithNewData);

        const birthday = service.getBirthday(formGroup) as any;

        expect(birthday).toMatchObject(sampleWithNewData);
      });

      it('should return NewBirthday for empty Birthday initial value', () => {
        const formGroup = service.createBirthdayFormGroup();

        const birthday = service.getBirthday(formGroup) as any;

        expect(birthday).toMatchObject({});
      });

      it('should return IBirthday', () => {
        const formGroup = service.createBirthdayFormGroup(sampleWithRequiredData);

        const birthday = service.getBirthday(formGroup) as any;

        expect(birthday).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBirthday should not enable id FormControl', () => {
        const formGroup = service.createBirthdayFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBirthday should disable id FormControl', () => {
        const formGroup = service.createBirthdayFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
