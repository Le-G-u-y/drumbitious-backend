import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { FinishedExerciseDetailComponent } from 'app/entities/finished-exercise/finished-exercise-detail.component';
import { FinishedExercise } from 'app/shared/model/finished-exercise.model';

describe('Component Tests', () => {
  describe('FinishedExercise Management Detail Component', () => {
    let comp: FinishedExerciseDetailComponent;
    let fixture: ComponentFixture<FinishedExerciseDetailComponent>;
    const route = ({ data: of({ finishedExercise: new FinishedExercise(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [FinishedExerciseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FinishedExerciseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FinishedExerciseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.finishedExercise).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
