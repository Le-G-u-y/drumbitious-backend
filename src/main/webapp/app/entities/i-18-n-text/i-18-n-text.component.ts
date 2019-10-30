import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { II18nText } from 'app/shared/model/i-18-n-text.model';
import { AccountService } from 'app/core/auth/account.service';
import { I18nTextService } from './i-18-n-text.service';

@Component({
  selector: 'jhi-i-18-n-text',
  templateUrl: './i-18-n-text.component.html'
})
export class I18nTextComponent implements OnInit, OnDestroy {
  i18nTexts: II18nText[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected i18nTextService: I18nTextService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.i18nTextService
      .query()
      .pipe(
        filter((res: HttpResponse<II18nText[]>) => res.ok),
        map((res: HttpResponse<II18nText[]>) => res.body)
      )
      .subscribe((res: II18nText[]) => {
        this.i18nTexts = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInI18nTexts();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: II18nText) {
    return item.id;
  }

  registerChangeInI18nTexts() {
    this.eventSubscriber = this.eventManager.subscribe('i18nTextListModification', response => this.loadAll());
  }
}
