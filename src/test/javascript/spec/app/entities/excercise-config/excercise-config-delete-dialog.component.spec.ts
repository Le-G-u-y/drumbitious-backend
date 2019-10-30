import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { ExcerciseConfigDeleteDialogComponent } from 'app/entities/excercise-config/excercise-config-delete-dialog.component';
import { ExcerciseConfigService } from 'app/entities/excercise-config/excercise-config.service';

describe('Component Tests', () => {
  describe('ExcerciseConfig Management Delete Component', () => {
    let comp: ExcerciseConfigDeleteDialogComponent;
    let fixture: ComponentFixture<ExcerciseConfigDeleteDialogComponent>;
    let service: ExcerciseConfigService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [ExcerciseConfigDeleteDialogComponent]
      })
        .overrideTemplate(ExcerciseConfigDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExcerciseConfigDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExcerciseConfigService);
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
