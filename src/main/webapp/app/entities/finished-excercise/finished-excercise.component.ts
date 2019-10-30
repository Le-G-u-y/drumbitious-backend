import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IFinishedExcercise } from 'app/shared/model/finished-excercise.model';
import { AccountService } from 'app/core/auth/account.service';
import { FinishedExcerciseService } from './finished-excercise.service';

@Component({
  selector: 'jhi-finished-excercise',
  templateUrl: './finished-excercise.component.html'
})
export class FinishedExcerciseComponent implements OnInit, OnDestroy {
  finishedExcercises: IFinishedExcercise[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected finishedExcerciseService: FinishedExcerciseService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.finishedExcerciseService
      .query()
      .pipe(
        filter((res: HttpResponse<IFinishedExcercise[]>) => res.ok),
        map((res: HttpResponse<IFinishedExcercise[]>) => res.body)
      )
      .subscribe((res: IFinishedExcercise[]) => {
        this.finishedExcercises = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInFinishedExcercises();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IFinishedExcercise) {
    return item.id;
  }

  registerChangeInFinishedExcercises() {
    this.eventSubscriber = this.eventManager.subscribe('finishedExcerciseListModification', response => this.loadAll());
  }
}
