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
import { IExercise, Exercise } from 'app/shared/model/exercise.model';
import { ExerciseService } from './exercise.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-exercise-update',
  templateUrl: './exercise-update.component.html'
})
export class ExerciseUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    exerciseName: [null, [Validators.required, Validators.maxLength(200)]],
    description: [null, [Validators.maxLength(10000)]],
    sourceUrl: [null, [Validators.maxLength(2083)]],
    defaultMinutes: [null, [Validators.max(9000)]],
    defaultBpmMin: [null, [Validators.min(1), Validators.max(500)]],
    defaultBpmMax: [null, [Validators.min(1), Validators.max(500)]],
    deactivted: [],
    createDate: [null, [Validators.required]],
    modifyDate: [null, [Validators.required]],
    skillType: [null, [Validators.required]],
    exercise: [null, [Validators.required]],
    creatorId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected exerciseService: ExerciseService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ exercise }) => {
      this.updateForm(exercise);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(exercise: IExercise) {
    this.editForm.patchValue({
      id: exercise.id,
      exerciseName: exercise.exerciseName,
      description: exercise.description,
      sourceUrl: exercise.sourceUrl,
      defaultMinutes: exercise.defaultMinutes,
      defaultBpmMin: exercise.defaultBpmMin,
      defaultBpmMax: exercise.defaultBpmMax,
      deactivted: exercise.deactivted,
      createDate: exercise.createDate != null ? exercise.createDate.format(DATE_TIME_FORMAT) : null,
      modifyDate: exercise.modifyDate != null ? exercise.modifyDate.format(DATE_TIME_FORMAT) : null,
      skillType: exercise.skillType,
      exercise: exercise.exercise,
      creatorId: exercise.creatorId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const exercise = this.createFromForm();
    if (exercise.id !== undefined) {
      this.subscribeToSaveResponse(this.exerciseService.update(exercise));
    } else {
      this.subscribeToSaveResponse(this.exerciseService.create(exercise));
    }
  }

  private createFromForm(): IExercise {
    return {
      ...new Exercise(),
      id: this.editForm.get(['id']).value,
      exerciseName: this.editForm.get(['exerciseName']).value,
      description: this.editForm.get(['description']).value,
      sourceUrl: this.editForm.get(['sourceUrl']).value,
      defaultMinutes: this.editForm.get(['defaultMinutes']).value,
      defaultBpmMin: this.editForm.get(['defaultBpmMin']).value,
      defaultBpmMax: this.editForm.get(['defaultBpmMax']).value,
      deactivted: this.editForm.get(['deactivted']).value,
      createDate:
        this.editForm.get(['createDate']).value != null ? moment(this.editForm.get(['createDate']).value, DATE_TIME_FORMAT) : undefined,
      modifyDate:
        this.editForm.get(['modifyDate']).value != null ? moment(this.editForm.get(['modifyDate']).value, DATE_TIME_FORMAT) : undefined,
      skillType: this.editForm.get(['skillType']).value,
      exercise: this.editForm.get(['exercise']).value,
      creatorId: this.editForm.get(['creatorId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExercise>>) {
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
}
