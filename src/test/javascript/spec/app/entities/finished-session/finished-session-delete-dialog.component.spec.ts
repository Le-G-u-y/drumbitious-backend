import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { FinishedSessionDeleteDialogComponent } from 'app/entities/finished-session/finished-session-delete-dialog.component';
import { FinishedSessionService } from 'app/entities/finished-session/finished-session.service';

describe('Component Tests', () => {
  describe('FinishedSession Management Delete Component', () => {
    let comp: FinishedSessionDeleteDialogComponent;
    let fixture: ComponentFixture<FinishedSessionDeleteDialogComponent>;
    let service: FinishedSessionService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [FinishedSessionDeleteDialogComponent]
      })
        .overrideTemplate(FinishedSessionDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FinishedSessionDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FinishedSessionService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
