import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { ExcerciseConfigComponent } from 'app/entities/excercise-config/excercise-config.component';
import { ExcerciseConfigService } from 'app/entities/excercise-config/excercise-config.service';
import { ExcerciseConfig } from 'app/shared/model/excercise-config.model';

describe('Component Tests', () => {
  describe('ExcerciseConfig Management Component', () => {
    let comp: ExcerciseConfigComponent;
    let fixture: ComponentFixture<ExcerciseConfigComponent>;
    let service: ExcerciseConfigService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [ExcerciseConfigComponent],
        providers: []
      })
        .overrideTemplate(ExcerciseConfigComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExcerciseConfigComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExcerciseConfigService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ExcerciseConfig(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.excerciseConfigs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
