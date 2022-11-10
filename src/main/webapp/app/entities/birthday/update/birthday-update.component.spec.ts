import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BirthdayFormService } from './birthday-form.service';
import { BirthdayService } from '../service/birthday.service';
import { IBirthday } from '../birthday.model';

import { BirthdayUpdateComponent } from './birthday-update.component';

describe('Birthday Management Update Component', () => {
  let comp: BirthdayUpdateComponent;
  let fixture: ComponentFixture<BirthdayUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let birthdayFormService: BirthdayFormService;
  let birthdayService: BirthdayService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BirthdayUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BirthdayUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BirthdayUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    birthdayFormService = TestBed.inject(BirthdayFormService);
    birthdayService = TestBed.inject(BirthdayService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const birthday: IBirthday = { id: 456 };

      activatedRoute.data = of({ birthday });
      comp.ngOnInit();

      expect(comp.birthday).toEqual(birthday);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBirthday>>();
      const birthday = { id: 123 };
      jest.spyOn(birthdayFormService, 'getBirthday').mockReturnValue(birthday);
      jest.spyOn(birthdayService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ birthday });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: birthday }));
      saveSubject.complete();

      // THEN
      expect(birthdayFormService.getBirthday).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(birthdayService.update).toHaveBeenCalledWith(expect.objectContaining(birthday));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBirthday>>();
      const birthday = { id: 123 };
      jest.spyOn(birthdayFormService, 'getBirthday').mockReturnValue({ id: null });
      jest.spyOn(birthdayService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ birthday: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: birthday }));
      saveSubject.complete();

      // THEN
      expect(birthdayFormService.getBirthday).toHaveBeenCalled();
      expect(birthdayService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBirthday>>();
      const birthday = { id: 123 };
      jest.spyOn(birthdayService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ birthday });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(birthdayService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
