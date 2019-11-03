import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { I18nTextComponent } from 'app/entities/i-18-n-text/i-18-n-text.component';
import { I18nTextService } from 'app/entities/i-18-n-text/i-18-n-text.service';
import { I18nText } from 'app/shared/model/i-18-n-text.model';

describe('Component Tests', () => {
  describe('I18nText Management Component', () => {
    let comp: I18nTextComponent;
    let fixture: ComponentFixture<I18nTextComponent>;
    let service: I18nTextService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [I18nTextComponent],
        providers: []
      })
        .overrideTemplate(I18nTextComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(I18nTextComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(I18nTextService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new I18nText(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.i18nTexts[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
