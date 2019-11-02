import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { II18nText } from 'app/shared/model/i-18-n-text.model';

@Component({
  selector: 'jhi-i-18-n-text-detail',
  templateUrl: './i-18-n-text-detail.component.html'
})
export class I18nTextDetailComponent implements OnInit {
  i18nText: II18nText;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ i18nText }) => {
      this.i18nText = i18nText;
    });
  }

  previousState() {
    window.history.back();
  }
}
