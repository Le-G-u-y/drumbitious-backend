import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { ExcerciseTypeComponent } from 'app/entities/excercise-type/excercise-type.component';
import { ExcerciseTypeService } from 'app/entities/excercise-type/excercise-type.service';
import { ExcerciseType } from 'app/shared/model/excercise-type.model';

describe('Component Tests', () => {
  describe('ExcerciseType Management Component', () => {
    let comp: ExcerciseTypeComponent;
    let fixture: ComponentFixture<ExcerciseTypeComponent>;
    let service: ExcerciseTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [ExcerciseTypeComponent],
        providers: []
      })
        .overrideTemplate(ExcerciseTypeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExcerciseTypeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExcerciseTypeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ExcerciseType(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.excerciseTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
