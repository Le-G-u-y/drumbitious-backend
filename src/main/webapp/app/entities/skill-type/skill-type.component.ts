import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { ISkillType } from 'app/shared/model/skill-type.model';
import { AccountService } from 'app/core/auth/account.service';
import { SkillTypeService } from './skill-type.service';

@Component({
  selector: 'jhi-skill-type',
  templateUrl: './skill-type.component.html'
})
export class SkillTypeComponent implements OnInit, OnDestroy {
  skillTypes: ISkillType[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected skillTypeService: SkillTypeService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.skillTypeService
      .query()
      .pipe(
        filter((res: HttpResponse<ISkillType[]>) => res.ok),
        map((res: HttpResponse<ISkillType[]>) => res.body)
      )
      .subscribe((res: ISkillType[]) => {
        this.skillTypes = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInSkillTypes();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ISkillType) {
    return item.id;
  }

  registerChangeInSkillTypes() {
    this.eventSubscriber = this.eventManager.subscribe('skillTypeListModification', response => this.loadAll());
  }
}
