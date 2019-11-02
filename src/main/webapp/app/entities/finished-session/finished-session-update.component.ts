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
import { IFinishedSession, FinishedSession } from 'app/shared/model/finished-session.model';
import { FinishedSessionService } from './finished-session.service';
import { IPlan } from 'app/shared/model/plan.model';
import { PlanService } from 'app/entities/plan/plan.service';

@Component({
  selector: 'jhi-finished-session-update',
  templateUrl: './finished-session-update.component.html'
})
export class FinishedSessionUpdateComponent implements OnInit {
  isSaving: boolean;

  plans: IPlan[];

  editForm = this.fb.group({
    id: [],
    minutesTotal: [null, [Validators.min(1), Validators.max(600)]],
    note: [null, [Validators.maxLength(5000)]],
    createDate: [null, [Validators.required]],
    modifyDate: [null, [Validators.required]],
    planId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected finishedSessionService: FinishedSessionService,
    protected planService: PlanService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ finishedSession }) => {
      this.updateForm(finishedSession);
    });
    this.planService
      .query({ 'finishedSessionId.specified': 'false' })
      .pipe(
        filter((mayBeOk: HttpResponse<IPlan[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPlan[]>) => response.body)
      )
      .subscribe(
        (res: IPlan[]) => {
          if (!this.editForm.get('planId').value) {
            this.plans = res;
          } else {
            this.planService
              .find(this.editForm.get('planId').value)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IPlan>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IPlan>) => subResponse.body)
              )
              .subscribe(
                (subRes: IPlan) => (this.plans = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(finishedSession: IFinishedSession) {
    this.editForm.patchValue({
      id: finishedSession.id,
      minutesTotal: finishedSession.minutesTotal,
      note: finishedSession.note,
      createDate: finishedSession.createDate != null ? finishedSession.createDate.format(DATE_TIME_FORMAT) : null,
      modifyDate: finishedSession.modifyDate != null ? finishedSession.modifyDate.format(DATE_TIME_FORMAT) : null,
      planId: finishedSession.planId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const finishedSession = this.createFromForm();
    if (finishedSession.id !== undefined) {
      this.subscribeToSaveResponse(this.finishedSessionService.update(finishedSession));
    } else {
      this.subscribeToSaveResponse(this.finishedSessionService.create(finishedSession));
    }
  }

  private createFromForm(): IFinishedSession {
    return {
      ...new FinishedSession(),
      id: this.editForm.get(['id']).value,
      minutesTotal: this.editForm.get(['minutesTotal']).value,
      note: this.editForm.get(['note']).value,
      createDate:
        this.editForm.get(['createDate']).value != null ? moment(this.editForm.get(['createDate']).value, DATE_TIME_FORMAT) : undefined,
      modifyDate:
        this.editForm.get(['modifyDate']).value != null ? moment(this.editForm.get(['modifyDate']).value, DATE_TIME_FORMAT) : undefined,
      planId: this.editForm.get(['planId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFinishedSession>>) {
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

  trackPlanById(index: number, item: IPlan) {
    return item.id;
  }
}
