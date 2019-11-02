import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { FinishedSessionUpdateComponent } from 'app/entities/finished-session/finished-session-update.component';
import { FinishedSessionService } from 'app/entities/finished-session/finished-session.service';
import { FinishedSession } from 'app/shared/model/finished-session.model';

describe('Component Tests', () => {
  describe('FinishedSession Management Update Component', () => {
    let comp: FinishedSessionUpdateComponent;
    let fixture: ComponentFixture<FinishedSessionUpdateComponent>;
    let service: FinishedSessionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [FinishedSessionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FinishedSessionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FinishedSessionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FinishedSessionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FinishedSession(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new FinishedSession();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
