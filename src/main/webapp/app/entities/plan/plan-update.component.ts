import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IPlan, Plan } from 'app/shared/model/plan.model';
import { PlanService } from './plan.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IExcerciseConfig } from 'app/shared/model/excercise-config.model';
import { ExcerciseConfigService } from 'app/entities/excercise-config/excercise-config.service';

@Component({
  selector: 'jhi-plan-update',
  templateUrl: './plan-update.component.html'
})
export class PlanUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  excerciseconfigs: IExcerciseConfig[];

  editForm = this.fb.group({
    id: [],
    planName: [null, [Validators.required]],
    planFocus: [null, [Validators.required]],
    description: [],
    minutesPerSession: [],
    sessionsPerWeek: [],
    targetDate: [],
    active: [],
    createDate: [null, [Validators.required]],
    modifyDate: [null, [Validators.required]],
    ownerId: [],
    creatorId: [],
    excerciseConfigId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected planService: PlanService,
    protected userService: UserService,
    protected excerciseConfigService: ExcerciseConfigService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ plan }) => {
      this.updateForm(plan);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.excerciseConfigService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IExcerciseConfig[]>) => mayBeOk.ok),
        map((response: HttpResponse<IExcerciseConfig[]>) => response.body)
      )
      .subscribe((res: IExcerciseConfig[]) => (this.excerciseconfigs = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(plan: IPlan) {
    this.editForm.patchValue({
      id: plan.id,
      planName: plan.planName,
      planFocus: plan.planFocus,
      description: plan.description,
      minutesPerSession: plan.minutesPerSession,
      sessionsPerWeek: plan.sessionsPerWeek,
      targetDate: plan.targetDate != null ? plan.targetDate.format(DATE_TIME_FORMAT) : null,
      active: plan.active,
      createDate: plan.createDate != null ? plan.createDate.format(DATE_TIME_FORMAT) : null,
      modifyDate: plan.modifyDate != null ? plan.modifyDate.format(DATE_TIME_FORMAT) : null,
      ownerId: plan.ownerId,
      creatorId: plan.creatorId,
      excerciseConfigId: plan.excerciseConfigId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const plan = this.createFromForm();
    if (plan.id !== undefined) {
      this.subscribeToSaveResponse(this.planService.update(plan));
    } else {
      this.subscribeToSaveResponse(this.planService.create(plan));
    }
  }

  private createFromForm(): IPlan {
    return {
      ...new Plan(),
      id: this.editForm.get(['id']).value,
      planName: this.editForm.get(['planName']).value,
      planFocus: this.editForm.get(['planFocus']).value,
      description: this.editForm.get(['description']).value,
      minutesPerSession: this.editForm.get(['minutesPerSession']).value,
      sessionsPerWeek: this.editForm.get(['sessionsPerWeek']).value,
      targetDate:
        this.editForm.get(['targetDate']).value != null ? moment(this.editForm.get(['targetDate']).value, DATE_TIME_FORMAT) : undefined,
      active: this.editForm.get(['active']).value,
      createDate:
        this.editForm.get(['createDate']).value != null ? moment(this.editForm.get(['createDate']).value, DATE_TIME_FORMAT) : undefined,
      modifyDate:
        this.editForm.get(['modifyDate']).value != null ? moment(this.editForm.get(['modifyDate']).value, DATE_TIME_FORMAT) : undefined,
      ownerId: this.editForm.get(['ownerId']).value,
      creatorId: this.editForm.get(['creatorId']).value,
      excerciseConfigId: this.editForm.get(['excerciseConfigId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlan>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackExcerciseConfigById(index: number, item: IExcerciseConfig) {
    return item.id;
  }
}
