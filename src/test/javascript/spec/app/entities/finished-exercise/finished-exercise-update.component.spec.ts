import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { FinishedExerciseUpdateComponent } from 'app/entities/finished-exercise/finished-exercise-update.component';
import { FinishedExerciseService } from 'app/entities/finished-exercise/finished-exercise.service';
import { FinishedExercise } from 'app/shared/model/finished-exercise.model';

describe('Component Tests', () => {
  describe('FinishedExercise Management Update Component', () => {
    let comp: FinishedExerciseUpdateComponent;
    let fixture: ComponentFixture<FinishedExerciseUpdateComponent>;
    let service: FinishedExerciseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [FinishedExerciseUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FinishedExerciseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FinishedExerciseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FinishedExerciseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FinishedExercise(123);
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
        const entity = new FinishedExercise();
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
