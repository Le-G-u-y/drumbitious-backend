import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IFinishedExercise } from 'app/shared/model/finished-exercise.model';
import { AccountService } from 'app/core/auth/account.service';
import { FinishedExerciseService } from './finished-exercise.service';

@Component({
  selector: 'jhi-finished-exercise',
  templateUrl: './finished-exercise.component.html'
})
export class FinishedExerciseComponent implements OnInit, OnDestroy {
  finishedExercises: IFinishedExercise[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected finishedExerciseService: FinishedExerciseService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.finishedExerciseService
      .query()
      .pipe(
        filter((res: HttpResponse<IFinishedExercise[]>) => res.ok),
        map((res: HttpResponse<IFinishedExercise[]>) => res.body)
      )
      .subscribe((res: IFinishedExercise[]) => {
        this.finishedExercises = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInFinishedExercises();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IFinishedExercise) {
    return item.id;
  }

  registerChangeInFinishedExercises() {
    this.eventSubscriber = this.eventManager.subscribe('finishedExerciseListModification', response => this.loadAll());
  }
}
