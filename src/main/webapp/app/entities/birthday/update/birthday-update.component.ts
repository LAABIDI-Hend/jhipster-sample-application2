import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { BirthdayFormService, BirthdayFormGroup } from './birthday-form.service';
import { IBirthday } from '../birthday.model';
import { BirthdayService } from '../service/birthday.service';

@Component({
  selector: 'jhi-birthday-update',
  templateUrl: './birthday-update.component.html',
})
export class BirthdayUpdateComponent implements OnInit {
  isSaving = false;
  birthday: IBirthday | null = null;

  editForm: BirthdayFormGroup = this.birthdayFormService.createBirthdayFormGroup();

  constructor(
    protected birthdayService: BirthdayService,
    protected birthdayFormService: BirthdayFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ birthday }) => {
      this.birthday = birthday;
      if (birthday) {
        this.updateForm(birthday);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const birthday = this.birthdayFormService.getBirthday(this.editForm);
    if (birthday.id !== null) {
      this.subscribeToSaveResponse(this.birthdayService.update(birthday));
    } else {
      this.subscribeToSaveResponse(this.birthdayService.create(birthday));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBirthday>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(birthday: IBirthday): void {
    this.birthday = birthday;
    this.birthdayFormService.resetForm(this.editForm, birthday);
  }
}
