import dayjs from 'dayjs/esm';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 7813,
};

export const sampleWithPartialData: IEmployee = {
  id: 67981,
  lastName: 'Mann',
  email: 'Zoie8@yahoo.com',
  phoneNumber: 'Cambridgeshire',
  salary: 28480,
  commissionPct: 86212,
};

export const sampleWithFullData: IEmployee = {
  id: 42369,
  firstName: 'Eldred',
  lastName: 'Wiza',
  email: 'Justen_Kreiger@gmail.com',
  age: 'strategic',
  phoneNumber: 'paradigm',
  hireDate: dayjs('2022-11-09T17:10'),
  salary: 93513,
  commissionPct: 89015,
};

export const sampleWithNewData: NewEmployee = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
