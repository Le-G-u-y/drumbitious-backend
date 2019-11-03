import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { FinishedExerciseDeleteDialogComponent } from 'app/entities/finished-exercise/finished-exercise-delete-dialog.component';
import { FinishedExerciseService } from 'app/entities/finished-exercise/finished-exercise.service';

describe('Component Tests', () => {
  describe('FinishedExercise Management Delete Component', () => {
    let comp: FinishedExerciseDeleteDialogComponent;
    let fixture: ComponentFixture<FinishedExerciseDeleteDialogComponent>;
    let service: FinishedExerciseService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [FinishedExerciseDeleteDialogComponent]
      })
        .overrideTemplate(FinishedExerciseDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FinishedExerciseDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FinishedExerciseService);
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
