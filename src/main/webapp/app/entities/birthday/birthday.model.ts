export interface IBirthday {
  id: number;
  day?: string | null;
  month?: string | null;
  year?: string | null;
}

export type NewBirthday = Omit<IBirthday, 'id'> & { id: null };
