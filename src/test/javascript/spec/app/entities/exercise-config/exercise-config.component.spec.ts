import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { ExerciseConfigComponent } from 'app/entities/exercise-config/exercise-config.component';
import { ExerciseConfigService } from 'app/entities/exercise-config/exercise-config.service';
import { ExerciseConfig } from 'app/shared/model/exercise-config.model';

describe('Component Tests', () => {
  describe('ExerciseConfig Management Component', () => {
    let comp: ExerciseConfigComponent;
    let fixture: ComponentFixture<ExerciseConfigComponent>;
    let service: ExerciseConfigService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [ExerciseConfigComponent],
        providers: []
      })
        .overrideTemplate(ExerciseConfigComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExerciseConfigComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExerciseConfigService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ExerciseConfig(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.exerciseConfigs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
