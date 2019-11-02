import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { FinishedSessionDetailComponent } from 'app/entities/finished-session/finished-session-detail.component';
import { FinishedSession } from 'app/shared/model/finished-session.model';

describe('Component Tests', () => {
  describe('FinishedSession Management Detail Component', () => {
    let comp: FinishedSessionDetailComponent;
    let fixture: ComponentFixture<FinishedSessionDetailComponent>;
    const route = ({ data: of({ finishedSession: new FinishedSession(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [FinishedSessionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FinishedSessionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FinishedSessionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.finishedSession).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
