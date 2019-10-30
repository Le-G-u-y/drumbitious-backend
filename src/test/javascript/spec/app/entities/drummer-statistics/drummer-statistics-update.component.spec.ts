import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { DrummerStatisticsUpdateComponent } from 'app/entities/drummer-statistics/drummer-statistics-update.component';
import { DrummerStatisticsService } from 'app/entities/drummer-statistics/drummer-statistics.service';
import { DrummerStatistics } from 'app/shared/model/drummer-statistics.model';

describe('Component Tests', () => {
  describe('DrummerStatistics Management Update Component', () => {
    let comp: DrummerStatisticsUpdateComponent;
    let fixture: ComponentFixture<DrummerStatisticsUpdateComponent>;
    let service: DrummerStatisticsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [DrummerStatisticsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DrummerStatisticsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DrummerStatisticsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DrummerStatisticsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DrummerStatistics(123);
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
        const entity = new DrummerStatistics();
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
