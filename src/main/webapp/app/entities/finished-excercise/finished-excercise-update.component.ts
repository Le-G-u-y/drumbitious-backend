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
import { IFinishedExcercise, FinishedExcercise } from 'app/shared/model/finished-excercise.model';
import { FinishedExcerciseService } from './finished-excercise.service';
import { IFinishedSession } from 'app/shared/model/finished-session.model';
import { FinishedSessionService } from 'app/entities/finished-session/finished-session.service';
import { IExcercise } from 'app/shared/model/excercise.model';
import { ExcerciseService } from 'app/entities/excercise/excercise.service';

@Component({
  selector: 'jhi-finished-excercise-update',
  templateUrl: './finished-excercise-update.component.html'
})
export class FinishedExcerciseUpdateComponent implements OnInit {
  isSaving: boolean;

  finishedsessions: IFinishedSession[];

  excercises: IExcercise[];

  editForm = this.fb.group({
    id: [],
    actualBpm: [null, [Validators.min(1), Validators.max(500)]],
    actualMinutes: [null, [Validators.min(1), Validators.max(600)]],
    createDate: [null, [Validators.required]],
    modifyDate: [null, [Validators.required]],
    finishedSessionId: [],
    excerciseId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected finishedExcerciseService: FinishedExcerciseService,
    protected finishedSessionService: FinishedSessionService,
    protected excerciseService: ExcerciseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ finishedExcercise }) => {
      this.updateForm(finishedExcercise);
    });
    this.finishedSessionService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IFinishedSession[]>) => mayBeOk.ok),
        map((response: HttpResponse<IFinishedSession[]>) => response.body)
      )
      .subscribe((res: IFinishedSession[]) => (this.finishedsessions = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.excerciseService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IExcercise[]>) => mayBeOk.ok),
        map((response: HttpResponse<IExcercise[]>) => response.body)
      )
      .subscribe((res: IExcercise[]) => (this.excercises = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(finishedExcercise: IFinishedExcercise) {
    this.editForm.patchValue({
      id: finishedExcercise.id,
      actualBpm: finishedExcercise.actualBpm,
      actualMinutes: finishedExcercise.actualMinutes,
      createDate: finishedExcercise.createDate != null ? finishedExcercise.createDate.format(DATE_TIME_FORMAT) : null,
      modifyDate: finishedExcercise.modifyDate != null ? finishedExcercise.modifyDate.format(DATE_TIME_FORMAT) : null,
      finishedSessionId: finishedExcercise.finishedSessionId,
      excerciseId: finishedExcercise.excerciseId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const finishedExcercise = this.createFromForm();
    if (finishedExcercise.id !== undefined) {
      this.subscribeToSaveResponse(this.finishedExcerciseService.update(finishedExcercise));
    } else {
      this.subscribeToSaveResponse(this.finishedExcerciseService.create(finishedExcercise));
    }
  }

  private createFromForm(): IFinishedExcercise {
    return {
      ...new FinishedExcercise(),
      id: this.editForm.get(['id']).value,
      actualBpm: this.editForm.get(['actualBpm']).value,
      actualMinutes: this.editForm.get(['actualMinutes']).value,
      createDate:
        this.editForm.get(['createDate']).value != null ? moment(this.editForm.get(['createDate']).value, DATE_TIME_FORMAT) : undefined,
      modifyDate:
        this.editForm.get(['modifyDate']).value != null ? moment(this.editForm.get(['modifyDate']).value, DATE_TIME_FORMAT) : undefined,
      finishedSessionId: this.editForm.get(['finishedSessionId']).value,
      excerciseId: this.editForm.get(['excerciseId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFinishedExcercise>>) {
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

  trackFinishedSessionById(index: number, item: IFinishedSession) {
    return item.id;
  }

  trackExcerciseById(index: number, item: IExcercise) {
    return item.id;
  }
}
