import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { FinishedExcerciseComponent } from 'app/entities/finished-excercise/finished-excercise.component';
import { FinishedExcerciseService } from 'app/entities/finished-excercise/finished-excercise.service';
import { FinishedExcercise } from 'app/shared/model/finished-excercise.model';

describe('Component Tests', () => {
  describe('FinishedExcercise Management Component', () => {
    let comp: FinishedExcerciseComponent;
    let fixture: ComponentFixture<FinishedExcerciseComponent>;
    let service: FinishedExcerciseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [FinishedExcerciseComponent],
        providers: []
      })
        .overrideTemplate(FinishedExcerciseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FinishedExcerciseComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FinishedExcerciseService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new FinishedExcercise(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.finishedExcercises[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
