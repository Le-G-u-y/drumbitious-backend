import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { ExcerciseDeleteDialogComponent } from 'app/entities/excercise/excercise-delete-dialog.component';
import { ExcerciseService } from 'app/entities/excercise/excercise.service';

describe('Component Tests', () => {
  describe('Excercise Management Delete Component', () => {
    let comp: ExcerciseDeleteDialogComponent;
    let fixture: ComponentFixture<ExcerciseDeleteDialogComponent>;
    let service: ExcerciseService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [ExcerciseDeleteDialogComponent]
      })
        .overrideTemplate(ExcerciseDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExcerciseDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExcerciseService);
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
