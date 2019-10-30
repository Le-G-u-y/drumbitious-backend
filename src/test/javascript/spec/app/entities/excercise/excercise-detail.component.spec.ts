import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { ExcerciseDetailComponent } from 'app/entities/excercise/excercise-detail.component';
import { Excercise } from 'app/shared/model/excercise.model';

describe('Component Tests', () => {
  describe('Excercise Management Detail Component', () => {
    let comp: ExcerciseDetailComponent;
    let fixture: ComponentFixture<ExcerciseDetailComponent>;
    const route = ({ data: of({ excercise: new Excercise(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [ExcerciseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ExcerciseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExcerciseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.excercise).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
