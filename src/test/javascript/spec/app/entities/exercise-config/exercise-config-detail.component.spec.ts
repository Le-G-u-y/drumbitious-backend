import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { ExerciseConfigDetailComponent } from 'app/entities/exercise-config/exercise-config-detail.component';
import { ExerciseConfig } from 'app/shared/model/exercise-config.model';

describe('Component Tests', () => {
  describe('ExerciseConfig Management Detail Component', () => {
    let comp: ExerciseConfigDetailComponent;
    let fixture: ComponentFixture<ExerciseConfigDetailComponent>;
    const route = ({ data: of({ exerciseConfig: new ExerciseConfig(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [ExerciseConfigDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ExerciseConfigDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExerciseConfigDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.exerciseConfig).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
