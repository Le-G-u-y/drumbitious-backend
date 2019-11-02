import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DrumbitiousBackendTestModule } from '../../../test.module';
import { I18nTextDetailComponent } from 'app/entities/i-18-n-text/i-18-n-text-detail.component';
import { I18nText } from 'app/shared/model/i-18-n-text.model';

describe('Component Tests', () => {
  describe('I18nText Management Detail Component', () => {
    let comp: I18nTextDetailComponent;
    let fixture: ComponentFixture<I18nTextDetailComponent>;
    const route = ({ data: of({ i18nText: new I18nText(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DrumbitiousBackendTestModule],
        declarations: [I18nTextDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(I18nTextDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(I18nTextDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.i18nText).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
