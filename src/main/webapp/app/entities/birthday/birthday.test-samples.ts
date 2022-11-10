import { IBirthday, NewBirthday } from './birthday.model';

export const sampleWithRequiredData: IBirthday = {
  id: 57698,
};

export const sampleWithPartialData: IBirthday = {
  id: 52312,
  year: 'Computers Handmade',
};

export const sampleWithFullData: IBirthday = {
  id: 73568,
  day: 'Junctions plum',
  month: 'Locks',
  year: 'Greens infrastructures Avon',
};

export const sampleWithNewData: NewBirthday = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
