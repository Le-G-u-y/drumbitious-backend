import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { FinishedExcerciseDeleteDialogComponent } from 'app/entities/finished-excercise/finished-excercise-delete-dialog.component';
import { FinishedExcerciseService } from 'app/entities/finished-excercise/finished-excercise.service';

describe('Component Tests', () => {
  describe('FinishedExcercise Management Delete Component', () => {
    let comp: FinishedExcerciseDeleteDialogComponent;
    let fixture: ComponentFixture<FinishedExcerciseDeleteDialogComponent>;
    let service: FinishedExcerciseService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [FinishedExcerciseDeleteDialogComponent]
      })
        .overrideTemplate(FinishedExcerciseDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FinishedExcerciseDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FinishedExcerciseService);
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
