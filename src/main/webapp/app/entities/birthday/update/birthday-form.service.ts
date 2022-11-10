import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBirthday, NewBirthday } from '../birthday.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBirthday for edit and NewBirthdayFormGroupInput for create.
 */
type BirthdayFormGroupInput = IBirthday | PartialWithRequiredKeyOf<NewBirthday>;

type BirthdayFormDefaults = Pick<NewBirthday, 'id'>;

type BirthdayFormGroupContent = {
  id: FormControl<IBirthday['id'] | NewBirthday['id']>;
  day: FormControl<IBirthday['day']>;
  month: FormControl<IBirthday['month']>;
  year: FormControl<IBirthday['year']>;
};

export type BirthdayFormGroup = FormGroup<BirthdayFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BirthdayFormService {
  createBirthdayFormGroup(birthday: BirthdayFormGroupInput = { id: null }): BirthdayFormGroup {
    const birthdayRawValue = {
      ...this.getFormDefaults(),
      ...birthday,
    };
    return new FormGroup<BirthdayFormGroupContent>({
      id: new FormControl(
        { value: birthdayRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      day: new FormControl(birthdayRawValue.day),
      month: new FormControl(birthdayRawValue.month),
      year: new FormControl(birthdayRawValue.year),
    });
  }

  getBirthday(form: BirthdayFormGroup): IBirthday | NewBirthday {
    return form.getRawValue() as IBirthday | NewBirthday;
  }

  resetForm(form: BirthdayFormGroup, birthday: BirthdayFormGroupInput): void {
    const birthdayRawValue = { ...this.getFormDefaults(), ...birthday };
    form.reset(
      {
        ...birthdayRawValue,
        id: { value: birthdayRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BirthdayFormDefaults {
    return {
      id: null,
    };
  }
}
