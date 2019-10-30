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
import { IExcercise, Excercise } from 'app/shared/model/excercise.model';
import { ExcerciseService } from './excercise.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { ISkillType } from 'app/shared/model/skill-type.model';
import { SkillTypeService } from 'app/entities/skill-type/skill-type.service';
import { IExcerciseType } from 'app/shared/model/excercise-type.model';
import { ExcerciseTypeService } from 'app/entities/excercise-type/excercise-type.service';

@Component({
  selector: 'jhi-excercise-update',
  templateUrl: './excercise-update.component.html'
})
export class ExcerciseUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  skilltypes: ISkillType[];

  excercisetypes: IExcerciseType[];

  editForm = this.fb.group({
    id: [],
    excerciseName: [null, [Validators.required, Validators.maxLength(200)]],
    description: [null, [Validators.maxLength(10000)]],
    sourceUrl: [null, [Validators.maxLength(2083)]],
    defaultMinutes: [null, [Validators.max(9000)]],
    defaultBpmMin: [null, [Validators.min(1), Validators.max(500)]],
    defaultBpmMax: [null, [Validators.min(1), Validators.max(500)]],
    deactivted: [],
    createDate: [null, [Validators.required]],
    modifyDate: [null, [Validators.required]],
    creatorId: [],
    skillNames: [],
    excerciseTypes: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected excerciseService: ExcerciseService,
    protected userService: UserService,
    protected skillTypeService: SkillTypeService,
    protected excerciseTypeService: ExcerciseTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ excercise }) => {
      this.updateForm(excercise);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.skillTypeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ISkillType[]>) => mayBeOk.ok),
        map((response: HttpResponse<ISkillType[]>) => response.body)
      )
      .subscribe((res: ISkillType[]) => (this.skilltypes = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.excerciseTypeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IExcerciseType[]>) => mayBeOk.ok),
        map((response: HttpResponse<IExcerciseType[]>) => response.body)
      )
      .subscribe((res: IExcerciseType[]) => (this.excercisetypes = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(excercise: IExcercise) {
    this.editForm.patchValue({
      id: excercise.id,
      excerciseName: excercise.excerciseName,
      description: excercise.description,
      sourceUrl: excercise.sourceUrl,
      defaultMinutes: excercise.defaultMinutes,
      defaultBpmMin: excercise.defaultBpmMin,
      defaultBpmMax: excercise.defaultBpmMax,
      deactivted: excercise.deactivted,
      createDate: excercise.createDate != null ? excercise.createDate.format(DATE_TIME_FORMAT) : null,
      modifyDate: excercise.modifyDate != null ? excercise.modifyDate.format(DATE_TIME_FORMAT) : null,
      creatorId: excercise.creatorId,
      skillNames: excercise.skillNames,
      excerciseTypes: excercise.excerciseTypes
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const excercise = this.createFromForm();
    if (excercise.id !== undefined) {
      this.subscribeToSaveResponse(this.excerciseService.update(excercise));
    } else {
      this.subscribeToSaveResponse(this.excerciseService.create(excercise));
    }
  }

  private createFromForm(): IExcercise {
    return {
      ...new Excercise(),
      id: this.editForm.get(['id']).value,
      excerciseName: this.editForm.get(['excerciseName']).value,
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
      creatorId: this.editForm.get(['creatorId']).value,
      skillNames: this.editForm.get(['skillNames']).value,
      excerciseTypes: this.editForm.get(['excerciseTypes']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExcercise>>) {
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

  trackSkillTypeById(index: number, item: ISkillType) {
    return item.id;
  }

  trackExcerciseTypeById(index: number, item: IExcerciseType) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
