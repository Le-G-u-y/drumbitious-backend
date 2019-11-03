import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { DrummerStatisticsComponent } from 'app/entities/drummer-statistics/drummer-statistics.component';
import { DrummerStatisticsService } from 'app/entities/drummer-statistics/drummer-statistics.service';
import { DrummerStatistics } from 'app/shared/model/drummer-statistics.model';

describe('Component Tests', () => {
  describe('DrummerStatistics Management Component', () => {
    let comp: DrummerStatisticsComponent;
    let fixture: ComponentFixture<DrummerStatisticsComponent>;
    let service: DrummerStatisticsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [DrummerStatisticsComponent],
        providers: []
      })
        .overrideTemplate(DrummerStatisticsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DrummerStatisticsComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DrummerStatisticsService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new DrummerStatistics(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.drummerStatistics[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
