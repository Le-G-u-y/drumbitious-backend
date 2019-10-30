import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { FinishedExcerciseDetailComponent } from 'app/entities/finished-excercise/finished-excercise-detail.component';
import { FinishedExcercise } from 'app/shared/model/finished-excercise.model';

describe('Component Tests', () => {
  describe('FinishedExcercise Management Detail Component', () => {
    let comp: FinishedExcerciseDetailComponent;
    let fixture: ComponentFixture<FinishedExcerciseDetailComponent>;
    const route = ({ data: of({ finishedExcercise: new FinishedExcercise(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [FinishedExcerciseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FinishedExcerciseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FinishedExcerciseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.finishedExcercise).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
