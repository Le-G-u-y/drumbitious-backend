import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { I18nTextDeleteDialogComponent } from 'app/entities/i-18-n-text/i-18-n-text-delete-dialog.component';
import { I18nTextService } from 'app/entities/i-18-n-text/i-18-n-text.service';

describe('Component Tests', () => {
  describe('I18nText Management Delete Component', () => {
    let comp: I18nTextDeleteDialogComponent;
    let fixture: ComponentFixture<I18nTextDeleteDialogComponent>;
    let service: I18nTextService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [I18nTextDeleteDialogComponent]
      })
        .overrideTemplate(I18nTextDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(I18nTextDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(I18nTextService);
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
