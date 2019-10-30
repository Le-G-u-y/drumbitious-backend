import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { ExcerciseTypeUpdateComponent } from 'app/entities/excercise-type/excercise-type-update.component';
import { ExcerciseTypeService } from 'app/entities/excercise-type/excercise-type.service';
import { ExcerciseType } from 'app/shared/model/excercise-type.model';

describe('Component Tests', () => {
  describe('ExcerciseType Management Update Component', () => {
    let comp: ExcerciseTypeUpdateComponent;
    let fixture: ComponentFixture<ExcerciseTypeUpdateComponent>;
    let service: ExcerciseTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [ExcerciseTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ExcerciseTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExcerciseTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExcerciseTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ExcerciseType(123);
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
        const entity = new ExcerciseType();
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
