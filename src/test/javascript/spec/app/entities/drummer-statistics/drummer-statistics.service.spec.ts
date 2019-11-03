import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { DrummerStatisticsService } from 'app/entities/drummer-statistics/drummer-statistics.service';
import { IDrummerStatistics, DrummerStatistics } from 'app/shared/model/drummer-statistics.model';

describe('Service Tests', () => {
  describe('DrummerStatistics Service', () => {
    let injector: TestBed;
    let service: DrummerStatisticsService;
    let httpMock: HttpTestingController;
    let elemDefault: IDrummerStatistics;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(DrummerStatisticsService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new DrummerStatistics(0, 0, 0, 0, 0, 0, 0, 0, 0, 'AAAAAAA', currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
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

      it('should create a DrummerStatistics', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createDate: currentDate.format(DATE_TIME_FORMAT),
            modifyDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createDate: currentDate,
            modifyDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new DrummerStatistics(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a DrummerStatistics', () => {
        const returnedFromService = Object.assign(
          {
            selfAssessedLevelSpeed: 1,
            selfAssessedLevelGroove: 1,
            selfAssessedLevelCreativity: 1,
            selfAssessedLevelAdaptability: 1,
            selfAssessedLevelDynamics: 1,
            selfAssessedLevelIndependence: 1,
            selfAssessedLevelLivePerformance: 1,
            selfAssessedLevelReadingMusic: 1,
            note: 'BBBBBB',
            createDate: currentDate.format(DATE_TIME_FORMAT),
            modifyDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
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

      it('should return a list of DrummerStatistics', () => {
        const returnedFromService = Object.assign(
          {
            selfAssessedLevelSpeed: 1,
            selfAssessedLevelGroove: 1,
            selfAssessedLevelCreativity: 1,
            selfAssessedLevelAdaptability: 1,
            selfAssessedLevelDynamics: 1,
            selfAssessedLevelIndependence: 1,
            selfAssessedLevelLivePerformance: 1,
            selfAssessedLevelReadingMusic: 1,
            note: 'BBBBBB',
            createDate: currentDate.format(DATE_TIME_FORMAT),
            modifyDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
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

      it('should delete a DrummerStatistics', () => {
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
