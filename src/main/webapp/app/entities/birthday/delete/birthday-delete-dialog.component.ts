import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBirthday } from '../birthday.model';
import { BirthdayService } from '../service/birthday.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './birthday-delete-dialog.component.html',
})
export class BirthdayDeleteDialogComponent {
  birthday?: IBirthday;

  constructor(protected birthdayService: BirthdayService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.birthdayService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
