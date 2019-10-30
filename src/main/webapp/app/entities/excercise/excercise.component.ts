import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IExcercise } from 'app/shared/model/excercise.model';
import { AccountService } from 'app/core/auth/account.service';
import { ExcerciseService } from './excercise.service';

@Component({
  selector: 'jhi-excercise',
  templateUrl: './excercise.component.html'
})
export class ExcerciseComponent implements OnInit, OnDestroy {
  excercises: IExcercise[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected excerciseService: ExcerciseService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.excerciseService
      .query()
      .pipe(
        filter((res: HttpResponse<IExcercise[]>) => res.ok),
        map((res: HttpResponse<IExcercise[]>) => res.body)
      )
      .subscribe((res: IExcercise[]) => {
        this.excercises = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInExcercises();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IExcercise) {
    return item.id;
  }

  registerChangeInExcercises() {
    this.eventSubscriber = this.eventManager.subscribe('excerciseListModification', response => this.loadAll());
  }
}
