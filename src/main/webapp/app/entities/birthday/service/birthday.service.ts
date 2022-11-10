import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBirthday, NewBirthday } from '../birthday.model';

export type PartialUpdateBirthday = Partial<IBirthday> & Pick<IBirthday, 'id'>;

export type EntityResponseType = HttpResponse<IBirthday>;
export type EntityArrayResponseType = HttpResponse<IBirthday[]>;

@Injectable({ providedIn: 'root' })
export class BirthdayService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/birthdays');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(birthday: NewBirthday): Observable<EntityResponseType> {
    return this.http.post<IBirthday>(this.resourceUrl, birthday, { observe: 'response' });
  }

  update(birthday: IBirthday): Observable<EntityResponseType> {
    return this.http.put<IBirthday>(`${this.resourceUrl}/${this.getBirthdayIdentifier(birthday)}`, birthday, { observe: 'response' });
  }

  partialUpdate(birthday: PartialUpdateBirthday): Observable<EntityResponseType> {
    return this.http.patch<IBirthday>(`${this.resourceUrl}/${this.getBirthdayIdentifier(birthday)}`, birthday, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBirthday>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBirthday[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBirthdayIdentifier(birthday: Pick<IBirthday, 'id'>): number {
    return birthday.id;
  }

  compareBirthday(o1: Pick<IBirthday, 'id'> | null, o2: Pick<IBirthday, 'id'> | null): boolean {
    return o1 && o2 ? this.getBirthdayIdentifier(o1) === this.getBirthdayIdentifier(o2) : o1 === o2;
  }

  addBirthdayToCollectionIfMissing<Type extends Pick<IBirthday, 'id'>>(
    birthdayCollection: Type[],
    ...birthdaysToCheck: (Type | null | undefined)[]
  ): Type[] {
    const birthdays: Type[] = birthdaysToCheck.filter(isPresent);
    if (birthdays.length > 0) {
      const birthdayCollectionIdentifiers = birthdayCollection.map(birthdayItem => this.getBirthdayIdentifier(birthdayItem)!);
      const birthdaysToAdd = birthdays.filter(birthdayItem => {
        const birthdayIdentifier = this.getBirthdayIdentifier(birthdayItem);
        if (birthdayCollectionIdentifiers.includes(birthdayIdentifier)) {
          return false;
        }
        birthdayCollectionIdentifiers.push(birthdayIdentifier);
        return true;
      });
      return [...birthdaysToAdd, ...birthdayCollection];
    }
    return birthdayCollection;
  }
}
