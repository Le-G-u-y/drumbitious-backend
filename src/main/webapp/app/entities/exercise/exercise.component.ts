import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IExercise } from 'app/shared/model/exercise.model';
import { AccountService } from 'app/core/auth/account.service';
import { ExerciseService } from './exercise.service';

@Component({
  selector: 'jhi-exercise',
  templateUrl: './exercise.component.html'
})
export class ExerciseComponent implements OnInit, OnDestroy {
  exercises: IExercise[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected exerciseService: ExerciseService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.exerciseService
      .query()
      .pipe(
        filter((res: HttpResponse<IExercise[]>) => res.ok),
        map((res: HttpResponse<IExercise[]>) => res.body)
      )
      .subscribe((res: IExercise[]) => {
        this.exercises = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInExercises();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IExercise) {
    return item.id;
  }

  registerChangeInExercises() {
    this.eventSubscriber = this.eventManager.subscribe('exerciseListModification', response => this.loadAll());
  }
}
