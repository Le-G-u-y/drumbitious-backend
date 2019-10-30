import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IExcerciseType } from 'app/shared/model/excercise-type.model';
import { AccountService } from 'app/core/auth/account.service';
import { ExcerciseTypeService } from './excercise-type.service';

@Component({
  selector: 'jhi-excercise-type',
  templateUrl: './excercise-type.component.html'
})
export class ExcerciseTypeComponent implements OnInit, OnDestroy {
  excerciseTypes: IExcerciseType[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected excerciseTypeService: ExcerciseTypeService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.excerciseTypeService
      .query()
      .pipe(
        filter((res: HttpResponse<IExcerciseType[]>) => res.ok),
        map((res: HttpResponse<IExcerciseType[]>) => res.body)
      )
      .subscribe((res: IExcerciseType[]) => {
        this.excerciseTypes = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInExcerciseTypes();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IExcerciseType) {
    return item.id;
  }

  registerChangeInExcerciseTypes() {
    this.eventSubscriber = this.eventManager.subscribe('excerciseTypeListModification', response => this.loadAll());
  }
}
