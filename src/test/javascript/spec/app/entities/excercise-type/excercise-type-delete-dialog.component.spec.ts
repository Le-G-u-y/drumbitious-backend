import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { ExcerciseTypeDeleteDialogComponent } from 'app/entities/excercise-type/excercise-type-delete-dialog.component';
import { ExcerciseTypeService } from 'app/entities/excercise-type/excercise-type.service';

describe('Component Tests', () => {
  describe('ExcerciseType Management Delete Component', () => {
    let comp: ExcerciseTypeDeleteDialogComponent;
    let fixture: ComponentFixture<ExcerciseTypeDeleteDialogComponent>;
    let service: ExcerciseTypeService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [ExcerciseTypeDeleteDialogComponent]
      })
        .overrideTemplate(ExcerciseTypeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExcerciseTypeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExcerciseTypeService);
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
