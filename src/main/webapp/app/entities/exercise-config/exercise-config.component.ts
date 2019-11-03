import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IExerciseConfig } from 'app/shared/model/exercise-config.model';
import { AccountService } from 'app/core/auth/account.service';
import { ExerciseConfigService } from './exercise-config.service';

@Component({
  selector: 'jhi-exercise-config',
  templateUrl: './exercise-config.component.html'
})
export class ExerciseConfigComponent implements OnInit, OnDestroy {
  exerciseConfigs: IExerciseConfig[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected exerciseConfigService: ExerciseConfigService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.exerciseConfigService
      .query()
      .pipe(
        filter((res: HttpResponse<IExerciseConfig[]>) => res.ok),
        map((res: HttpResponse<IExerciseConfig[]>) => res.body)
      )
      .subscribe((res: IExerciseConfig[]) => {
        this.exerciseConfigs = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInExerciseConfigs();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IExerciseConfig) {
    return item.id;
  }

  registerChangeInExerciseConfigs() {
    this.eventSubscriber = this.eventManager.subscribe('exerciseConfigListModification', response => this.loadAll());
  }
}
