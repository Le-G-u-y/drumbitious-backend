import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { SkillTypeComponent } from 'app/entities/skill-type/skill-type.component';
import { SkillTypeService } from 'app/entities/skill-type/skill-type.service';
import { SkillType } from 'app/shared/model/skill-type.model';

describe('Component Tests', () => {
  describe('SkillType Management Component', () => {
    let comp: SkillTypeComponent;
    let fixture: ComponentFixture<SkillTypeComponent>;
    let service: SkillTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [SkillTypeComponent],
        providers: []
      })
        .overrideTemplate(SkillTypeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SkillTypeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SkillTypeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new SkillType(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.skillTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
