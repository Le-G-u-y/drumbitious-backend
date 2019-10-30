import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IPlan } from 'app/shared/model/plan.model';
import { AccountService } from 'app/core/auth/account.service';
import { PlanService } from './plan.service';

@Component({
  selector: 'jhi-plan',
  templateUrl: './plan.component.html'
})
export class PlanComponent implements OnInit, OnDestroy {
  plans: IPlan[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(protected planService: PlanService, protected eventManager: JhiEventManager, protected accountService: AccountService) {}

  loadAll() {
    this.planService
      .query()
      .pipe(
        filter((res: HttpResponse<IPlan[]>) => res.ok),
        map((res: HttpResponse<IPlan[]>) => res.body)
      )
      .subscribe((res: IPlan[]) => {
        this.plans = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInPlans();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPlan) {
    return item.id;
  }

  registerChangeInPlans() {
    this.eventSubscriber = this.eventManager.subscribe('planListModification', response => this.loadAll());
  }
}
