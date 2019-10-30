import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { ExcerciseUpdateComponent } from 'app/entities/excercise/excercise-update.component';
import { ExcerciseService } from 'app/entities/excercise/excercise.service';
import { Excercise } from 'app/shared/model/excercise.model';

describe('Component Tests', () => {
  describe('Excercise Management Update Component', () => {
    let comp: ExcerciseUpdateComponent;
    let fixture: ComponentFixture<ExcerciseUpdateComponent>;
    let service: ExcerciseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [ExcerciseUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ExcerciseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExcerciseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExcerciseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Excercise(123);
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
        const entity = new Excercise();
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
