import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ExerciseService } from 'app/entities/exercise/exercise.service';
import { IExercise, Exercise } from 'app/shared/model/exercise.model';
import { SkillType } from 'app/shared/model/enumerations/skill-type.model';
import { ExerciseType } from 'app/shared/model/enumerations/exercise-type.model';

describe('Service Tests', () => {
  describe('Exercise Service', () => {
    let injector: TestBed;
    let service: ExerciseService;
    let httpMock: HttpTestingController;
    let elemDefault: IExercise;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(ExerciseService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Exercise(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        0,
        0,
        false,
        currentDate,
        currentDate,
        SkillType.SPEED,
        ExerciseType.RUDIMENT
      );
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

      it('should create a Exercise', () => {
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
          .create(new Exercise(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Exercise', () => {
        const returnedFromService = Object.assign(
          {
            exerciseName: 'BBBBBB',
            description: 'BBBBBB',
            sourceUrl: 'BBBBBB',
            defaultMinutes: 1,
            defaultBpmMin: 1,
            defaultBpmMax: 1,
            deactivted: true,
            createDate: currentDate.format(DATE_TIME_FORMAT),
            modifyDate: currentDate.format(DATE_TIME_FORMAT),
            skillType: 'BBBBBB',
            exercise: 'BBBBBB'
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

      it('should return a list of Exercise', () => {
        const returnedFromService = Object.assign(
          {
            exerciseName: 'BBBBBB',
            description: 'BBBBBB',
            sourceUrl: 'BBBBBB',
            defaultMinutes: 1,
            defaultBpmMin: 1,
            defaultBpmMax: 1,
            deactivted: true,
            createDate: currentDate.format(DATE_TIME_FORMAT),
            modifyDate: currentDate.format(DATE_TIME_FORMAT),
            skillType: 'BBBBBB',
            exercise: 'BBBBBB'
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

      it('should delete a Exercise', () => {
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
