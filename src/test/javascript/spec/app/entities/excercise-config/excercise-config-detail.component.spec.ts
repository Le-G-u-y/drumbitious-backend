import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { ExcerciseConfigDetailComponent } from 'app/entities/excercise-config/excercise-config-detail.component';
import { ExcerciseConfig } from 'app/shared/model/excercise-config.model';

describe('Component Tests', () => {
  describe('ExcerciseConfig Management Detail Component', () => {
    let comp: ExcerciseConfigDetailComponent;
    let fixture: ComponentFixture<ExcerciseConfigDetailComponent>;
    const route = ({ data: of({ excerciseConfig: new ExcerciseConfig(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [ExcerciseConfigDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ExcerciseConfigDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExcerciseConfigDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.excerciseConfig).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
