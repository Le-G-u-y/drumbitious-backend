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
import { IExerciseConfig, ExerciseConfig } from 'app/shared/model/exercise-config.model';
import { ExerciseConfigService } from './exercise-config.service';
import { IExercise } from 'app/shared/model/exercise.model';
import { ExerciseService } from 'app/entities/exercise/exercise.service';

@Component({
  selector: 'jhi-exercise-config-update',
  templateUrl: './exercise-config-update.component.html'
})
export class ExerciseConfigUpdateComponent implements OnInit {
  isSaving: boolean;

  exercises: IExercise[];

  editForm = this.fb.group({
    id: [],
    practiceBpm: [null, [Validators.min(1), Validators.max(500)]],
    targetBpm: [null, [Validators.min(1), Validators.max(500)]],
    minutes: [null, [Validators.min(1), Validators.max(500)]],
    note: [null, [Validators.maxLength(5000)]],
    createDate: [null, [Validators.required]],
    modifyDate: [null, [Validators.required]],
    exerciseId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected exerciseConfigService: ExerciseConfigService,
    protected exerciseService: ExerciseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ exerciseConfig }) => {
      this.updateForm(exerciseConfig);
    });
    this.exerciseService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IExercise[]>) => mayBeOk.ok),
        map((response: HttpResponse<IExercise[]>) => response.body)
      )
      .subscribe((res: IExercise[]) => (this.exercises = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(exerciseConfig: IExerciseConfig) {
    this.editForm.patchValue({
      id: exerciseConfig.id,
      practiceBpm: exerciseConfig.practiceBpm,
      targetBpm: exerciseConfig.targetBpm,
      minutes: exerciseConfig.minutes,
      note: exerciseConfig.note,
      createDate: exerciseConfig.createDate != null ? exerciseConfig.createDate.format(DATE_TIME_FORMAT) : null,
      modifyDate: exerciseConfig.modifyDate != null ? exerciseConfig.modifyDate.format(DATE_TIME_FORMAT) : null,
      exerciseId: exerciseConfig.exerciseId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const exerciseConfig = this.createFromForm();
    if (exerciseConfig.id !== undefined) {
      this.subscribeToSaveResponse(this.exerciseConfigService.update(exerciseConfig));
    } else {
      this.subscribeToSaveResponse(this.exerciseConfigService.create(exerciseConfig));
    }
  }

  private createFromForm(): IExerciseConfig {
    return {
      ...new ExerciseConfig(),
      id: this.editForm.get(['id']).value,
      practiceBpm: this.editForm.get(['practiceBpm']).value,
      targetBpm: this.editForm.get(['targetBpm']).value,
      minutes: this.editForm.get(['minutes']).value,
      note: this.editForm.get(['note']).value,
      createDate:
        this.editForm.get(['createDate']).value != null ? moment(this.editForm.get(['createDate']).value, DATE_TIME_FORMAT) : undefined,
      modifyDate:
        this.editForm.get(['modifyDate']).value != null ? moment(this.editForm.get(['modifyDate']).value, DATE_TIME_FORMAT) : undefined,
      exerciseId: this.editForm.get(['exerciseId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExerciseConfig>>) {
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

  trackExerciseById(index: number, item: IExercise) {
    return item.id;
  }
}
