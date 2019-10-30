import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IExcerciseConfig } from 'app/shared/model/excercise-config.model';
import { AccountService } from 'app/core/auth/account.service';
import { ExcerciseConfigService } from './excercise-config.service';

@Component({
  selector: 'jhi-excercise-config',
  templateUrl: './excercise-config.component.html'
})
export class ExcerciseConfigComponent implements OnInit, OnDestroy {
  excerciseConfigs: IExcerciseConfig[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected excerciseConfigService: ExcerciseConfigService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.excerciseConfigService
      .query()
      .pipe(
        filter((res: HttpResponse<IExcerciseConfig[]>) => res.ok),
        map((res: HttpResponse<IExcerciseConfig[]>) => res.body)
      )
      .subscribe((res: IExcerciseConfig[]) => {
        this.excerciseConfigs = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInExcerciseConfigs();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IExcerciseConfig) {
    return item.id;
  }

  registerChangeInExcerciseConfigs() {
    this.eventSubscriber = this.eventManager.subscribe('excerciseConfigListModification', response => this.loadAll());
  }
}
