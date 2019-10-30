import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { ExcerciseComponent } from 'app/entities/excercise/excercise.component';
import { ExcerciseService } from 'app/entities/excercise/excercise.service';
import { Excercise } from 'app/shared/model/excercise.model';

describe('Component Tests', () => {
  describe('Excercise Management Component', () => {
    let comp: ExcerciseComponent;
    let fixture: ComponentFixture<ExcerciseComponent>;
    let service: ExcerciseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [ExcerciseComponent],
        providers: []
      })
        .overrideTemplate(ExcerciseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExcerciseComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExcerciseService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Excercise(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.excercises[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
