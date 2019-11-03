import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IDrummerStatistics } from 'app/shared/model/drummer-statistics.model';
import { AccountService } from 'app/core/auth/account.service';
import { DrummerStatisticsService } from './drummer-statistics.service';

@Component({
  selector: 'jhi-drummer-statistics',
  templateUrl: './drummer-statistics.component.html'
})
export class DrummerStatisticsComponent implements OnInit, OnDestroy {
  drummerStatistics: IDrummerStatistics[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected drummerStatisticsService: DrummerStatisticsService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.drummerStatisticsService
      .query()
      .pipe(
        filter((res: HttpResponse<IDrummerStatistics[]>) => res.ok),
        map((res: HttpResponse<IDrummerStatistics[]>) => res.body)
      )
      .subscribe((res: IDrummerStatistics[]) => {
        this.drummerStatistics = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInDrummerStatistics();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IDrummerStatistics) {
    return item.id;
  }

  registerChangeInDrummerStatistics() {
    this.eventSubscriber = this.eventManager.subscribe('drummerStatisticsListModification', response => this.loadAll());
  }
}
