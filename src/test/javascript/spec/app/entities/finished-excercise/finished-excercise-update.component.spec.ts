import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { FinishedExcerciseUpdateComponent } from 'app/entities/finished-excercise/finished-excercise-update.component';
import { FinishedExcerciseService } from 'app/entities/finished-excercise/finished-excercise.service';
import { FinishedExcercise } from 'app/shared/model/finished-excercise.model';

describe('Component Tests', () => {
  describe('FinishedExcercise Management Update Component', () => {
    let comp: FinishedExcerciseUpdateComponent;
    let fixture: ComponentFixture<FinishedExcerciseUpdateComponent>;
    let service: FinishedExcerciseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [FinishedExcerciseUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FinishedExcerciseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FinishedExcerciseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FinishedExcerciseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FinishedExcercise(123);
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
        const entity = new FinishedExcercise();
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
