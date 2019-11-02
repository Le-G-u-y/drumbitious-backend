import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { DrummerStatisticsDetailComponent } from 'app/entities/drummer-statistics/drummer-statistics-detail.component';
import { DrummerStatistics } from 'app/shared/model/drummer-statistics.model';

describe('Component Tests', () => {
  describe('DrummerStatistics Management Detail Component', () => {
    let comp: DrummerStatisticsDetailComponent;
    let fixture: ComponentFixture<DrummerStatisticsDetailComponent>;
    const route = ({ data: of({ drummerStatistics: new DrummerStatistics(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [DrummerStatisticsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DrummerStatisticsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DrummerStatisticsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.drummerStatistics).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
