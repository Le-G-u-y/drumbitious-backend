import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { ExcerciseTypeDetailComponent } from 'app/entities/excercise-type/excercise-type-detail.component';
import { ExcerciseType } from 'app/shared/model/excercise-type.model';

describe('Component Tests', () => {
  describe('ExcerciseType Management Detail Component', () => {
    let comp: ExcerciseTypeDetailComponent;
    let fixture: ComponentFixture<ExcerciseTypeDetailComponent>;
    const route = ({ data: of({ excerciseType: new ExcerciseType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [ExcerciseTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ExcerciseTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExcerciseTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.excerciseType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
