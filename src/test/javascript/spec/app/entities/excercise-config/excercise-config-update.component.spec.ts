import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { ExcerciseConfigUpdateComponent } from 'app/entities/excercise-config/excercise-config-update.component';
import { ExcerciseConfigService } from 'app/entities/excercise-config/excercise-config.service';
import { ExcerciseConfig } from 'app/shared/model/excercise-config.model';

describe('Component Tests', () => {
  describe('ExcerciseConfig Management Update Component', () => {
    let comp: ExcerciseConfigUpdateComponent;
    let fixture: ComponentFixture<ExcerciseConfigUpdateComponent>;
    let service: ExcerciseConfigService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [ExcerciseConfigUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ExcerciseConfigUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExcerciseConfigUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExcerciseConfigService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ExcerciseConfig(123);
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
        const entity = new ExcerciseConfig();
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
