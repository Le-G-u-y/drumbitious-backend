import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { FinishedSessionComponent } from 'app/entities/finished-session/finished-session.component';
import { FinishedSessionService } from 'app/entities/finished-session/finished-session.service';
import { FinishedSession } from 'app/shared/model/finished-session.model';

describe('Component Tests', () => {
  describe('FinishedSession Management Component', () => {
    let comp: FinishedSessionComponent;
    let fixture: ComponentFixture<FinishedSessionComponent>;
    let service: FinishedSessionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [FinishedSessionComponent],
        providers: []
      })
        .overrideTemplate(FinishedSessionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FinishedSessionComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FinishedSessionService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new FinishedSession(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.finishedSessions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
