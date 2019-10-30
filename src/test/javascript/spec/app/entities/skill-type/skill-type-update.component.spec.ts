import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { SkillTypeUpdateComponent } from 'app/entities/skill-type/skill-type-update.component';
import { SkillTypeService } from 'app/entities/skill-type/skill-type.service';
import { SkillType } from 'app/shared/model/skill-type.model';

describe('Component Tests', () => {
  describe('SkillType Management Update Component', () => {
    let comp: SkillTypeUpdateComponent;
    let fixture: ComponentFixture<SkillTypeUpdateComponent>;
    let service: SkillTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [SkillTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SkillTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SkillTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SkillTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SkillType(123);
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
        const entity = new SkillType();
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
