import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { I18nTextUpdateComponent } from 'app/entities/i-18-n-text/i-18-n-text-update.component';
import { I18nTextService } from 'app/entities/i-18-n-text/i-18-n-text.service';
import { I18nText } from 'app/shared/model/i-18-n-text.model';

describe('Component Tests', () => {
  describe('I18nText Management Update Component', () => {
    let comp: I18nTextUpdateComponent;
    let fixture: ComponentFixture<I18nTextUpdateComponent>;
    let service: I18nTextService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [I18nTextUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(I18nTextUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(I18nTextUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(I18nTextService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new I18nText(123);
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
        const entity = new I18nText();
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
