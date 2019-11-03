import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { PlanComponent } from 'app/entities/plan/plan.component';
import { PlanService } from 'app/entities/plan/plan.service';
import { Plan } from 'app/shared/model/plan.model';

describe('Component Tests', () => {
  describe('Plan Management Component', () => {
    let comp: PlanComponent;
    let fixture: ComponentFixture<PlanComponent>;
    let service: PlanService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [PlanComponent],
        providers: []
      })
        .overrideTemplate(PlanComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlanComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlanService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Plan(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.plans[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
