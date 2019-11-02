import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { FinishedExerciseComponent } from 'app/entities/finished-exercise/finished-exercise.component';
import { FinishedExerciseService } from 'app/entities/finished-exercise/finished-exercise.service';
import { FinishedExercise } from 'app/shared/model/finished-exercise.model';

describe('Component Tests', () => {
  describe('FinishedExercise Management Component', () => {
    let comp: FinishedExerciseComponent;
    let fixture: ComponentFixture<FinishedExerciseComponent>;
    let service: FinishedExerciseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [FinishedExerciseComponent],
        providers: []
      })
        .overrideTemplate(FinishedExerciseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FinishedExerciseComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FinishedExerciseService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new FinishedExercise(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.finishedExercises[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
