import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { SkillTypeDeleteDialogComponent } from 'app/entities/skill-type/skill-type-delete-dialog.component';
import { SkillTypeService } from 'app/entities/skill-type/skill-type.service';

describe('Component Tests', () => {
  describe('SkillType Management Delete Component', () => {
    let comp: SkillTypeDeleteDialogComponent;
    let fixture: ComponentFixture<SkillTypeDeleteDialogComponent>;
    let service: SkillTypeService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [SkillTypeDeleteDialogComponent]
      })
        .overrideTemplate(SkillTypeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SkillTypeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SkillTypeService);
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
