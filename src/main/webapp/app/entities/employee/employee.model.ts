import dayjs from 'dayjs/esm';
import { IDepartment } from 'app/entities/department/department.model';
import { IBirthday } from 'app/entities/birthday/birthday.model';

export interface IEmployee {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  age?: string | null;
  phoneNumber?: string | null;
  hireDate?: dayjs.Dayjs | null;
  salary?: number | null;
  commissionPct?: number | null;
  manager?: Pick<IEmployee, 'id'> | null;
  department?: Pick<IDepartment, 'id'> | null;
  birthday?: Pick<IBirthday, 'id'> | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
