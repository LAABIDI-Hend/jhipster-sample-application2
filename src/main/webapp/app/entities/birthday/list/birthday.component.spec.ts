import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BirthdayService } from '../service/birthday.service';

import { BirthdayComponent } from './birthday.component';

describe('Birthday Management Component', () => {
  let comp: BirthdayComponent;
  let fixture: ComponentFixture<BirthdayComponent>;
  let service: BirthdayService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'birthday', component: BirthdayComponent }]), HttpClientTestingModule],
      declarations: [BirthdayComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(BirthdayComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BirthdayComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BirthdayService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.birthdays?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to birthdayService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getBirthdayIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getBirthdayIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
