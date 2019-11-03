import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { ExerciseConfigDeleteDialogComponent } from 'app/entities/exercise-config/exercise-config-delete-dialog.component';
import { ExerciseConfigService } from 'app/entities/exercise-config/exercise-config.service';

describe('Component Tests', () => {
  describe('ExerciseConfig Management Delete Component', () => {
    let comp: ExerciseConfigDeleteDialogComponent;
    let fixture: ComponentFixture<ExerciseConfigDeleteDialogComponent>;
    let service: ExerciseConfigService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [ExerciseConfigDeleteDialogComponent]
      })
        .overrideTemplate(ExerciseConfigDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExerciseConfigDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExerciseConfigService);
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
