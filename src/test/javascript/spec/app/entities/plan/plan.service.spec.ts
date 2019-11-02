import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { PlanService } from 'app/entities/plan/plan.service';
import { IPlan, Plan } from 'app/shared/model/plan.model';

describe('Service Tests', () => {
  describe('Plan Service', () => {
    let injector: TestBed;
    let service: PlanService;
    let httpMock: HttpTestingController;
    let elemDefault: IPlan;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(PlanService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Plan(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, 0, currentDate, false, currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            targetDate: currentDate.format(DATE_TIME_FORMAT),
            createDate: currentDate.format(DATE_TIME_FORMAT),
            modifyDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Plan', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            targetDate: currentDate.format(DATE_TIME_FORMAT),
            createDate: currentDate.format(DATE_TIME_FORMAT),
            modifyDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            targetDate: currentDate,
            createDate: currentDate,
            modifyDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Plan(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Plan', () => {
        const returnedFromService = Object.assign(
          {
            planName: 'BBBBBB',
            planFocus: 'BBBBBB',
            description: 'BBBBBB',
            minutesPerSession: 1,
            sessionsPerWeek: 1,
            targetDate: currentDate.format(DATE_TIME_FORMAT),
            active: true,
            createDate: currentDate.format(DATE_TIME_FORMAT),
            modifyDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            targetDate: currentDate,
            createDate: currentDate,
            modifyDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Plan', () => {
        const returnedFromService = Object.assign(
          {
            planName: 'BBBBBB',
            planFocus: 'BBBBBB',
            description: 'BBBBBB',
            minutesPerSession: 1,
            sessionsPerWeek: 1,
            targetDate: currentDate.format(DATE_TIME_FORMAT),
            active: true,
            createDate: currentDate.format(DATE_TIME_FORMAT),
            modifyDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            targetDate: currentDate,
            createDate: currentDate,
            modifyDate: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Plan', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
