import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { ExerciseConfigUpdateComponent } from 'app/entities/exercise-config/exercise-config-update.component';
import { ExerciseConfigService } from 'app/entities/exercise-config/exercise-config.service';
import { ExerciseConfig } from 'app/shared/model/exercise-config.model';

describe('Component Tests', () => {
  describe('ExerciseConfig Management Update Component', () => {
    let comp: ExerciseConfigUpdateComponent;
    let fixture: ComponentFixture<ExerciseConfigUpdateComponent>;
    let service: ExerciseConfigService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [ExerciseConfigUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ExerciseConfigUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExerciseConfigUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExerciseConfigService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ExerciseConfig(123);
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
        const entity = new ExerciseConfig();
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
