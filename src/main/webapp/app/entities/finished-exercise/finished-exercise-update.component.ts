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
import { IFinishedExercise, FinishedExercise } from 'app/shared/model/finished-exercise.model';
import { FinishedExerciseService } from './finished-exercise.service';
import { IFinishedSession } from 'app/shared/model/finished-session.model';
import { FinishedSessionService } from 'app/entities/finished-session/finished-session.service';
import { IExercise } from 'app/shared/model/exercise.model';
import { ExerciseService } from 'app/entities/exercise/exercise.service';

@Component({
  selector: 'jhi-finished-exercise-update',
  templateUrl: './finished-exercise-update.component.html'
})
export class FinishedExerciseUpdateComponent implements OnInit {
  isSaving: boolean;

  finishedsessions: IFinishedSession[];

  exercises: IExercise[];

  editForm = this.fb.group({
    id: [],
    actualBpm: [null, [Validators.min(1), Validators.max(500)]],
    actualMinutes: [null, [Validators.min(1), Validators.max(600)]],
    createDate: [null, [Validators.required]],
    modifyDate: [null, [Validators.required]],
    finishedSessionId: [],
    exerciseId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected finishedExerciseService: FinishedExerciseService,
    protected finishedSessionService: FinishedSessionService,
    protected exerciseService: ExerciseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ finishedExercise }) => {
      this.updateForm(finishedExercise);
    });
    this.finishedSessionService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IFinishedSession[]>) => mayBeOk.ok),
        map((response: HttpResponse<IFinishedSession[]>) => response.body)
      )
      .subscribe((res: IFinishedSession[]) => (this.finishedsessions = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.exerciseService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IExercise[]>) => mayBeOk.ok),
        map((response: HttpResponse<IExercise[]>) => response.body)
      )
      .subscribe((res: IExercise[]) => (this.exercises = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(finishedExercise: IFinishedExercise) {
    this.editForm.patchValue({
      id: finishedExercise.id,
      actualBpm: finishedExercise.actualBpm,
      actualMinutes: finishedExercise.actualMinutes,
      createDate: finishedExercise.createDate != null ? finishedExercise.createDate.format(DATE_TIME_FORMAT) : null,
      modifyDate: finishedExercise.modifyDate != null ? finishedExercise.modifyDate.format(DATE_TIME_FORMAT) : null,
      finishedSessionId: finishedExercise.finishedSessionId,
      exerciseId: finishedExercise.exerciseId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const finishedExercise = this.createFromForm();
    if (finishedExercise.id !== undefined) {
      this.subscribeToSaveResponse(this.finishedExerciseService.update(finishedExercise));
    } else {
      this.subscribeToSaveResponse(this.finishedExerciseService.create(finishedExercise));
    }
  }

  private createFromForm(): IFinishedExercise {
    return {
      ...new FinishedExercise(),
      id: this.editForm.get(['id']).value,
      actualBpm: this.editForm.get(['actualBpm']).value,
      actualMinutes: this.editForm.get(['actualMinutes']).value,
      createDate:
        this.editForm.get(['createDate']).value != null ? moment(this.editForm.get(['createDate']).value, DATE_TIME_FORMAT) : undefined,
      modifyDate:
        this.editForm.get(['modifyDate']).value != null ? moment(this.editForm.get(['modifyDate']).value, DATE_TIME_FORMAT) : undefined,
      finishedSessionId: this.editForm.get(['finishedSessionId']).value,
      exerciseId: this.editForm.get(['exerciseId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFinishedExercise>>) {
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

  trackExerciseById(index: number, item: IExercise) {
    return item.id;
  }
}
