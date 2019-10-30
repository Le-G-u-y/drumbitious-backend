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
import { IExcerciseConfig, ExcerciseConfig } from 'app/shared/model/excercise-config.model';
import { ExcerciseConfigService } from './excercise-config.service';
import { IExcercise } from 'app/shared/model/excercise.model';
import { ExcerciseService } from 'app/entities/excercise/excercise.service';

@Component({
  selector: 'jhi-excercise-config-update',
  templateUrl: './excercise-config-update.component.html'
})
export class ExcerciseConfigUpdateComponent implements OnInit {
  isSaving: boolean;

  excercises: IExcercise[];

  editForm = this.fb.group({
    id: [],
    practiceBpm: [null, [Validators.min(1), Validators.max(500)]],
    targetBpm: [null, [Validators.min(1), Validators.max(500)]],
    minutes: [null, [Validators.min(1), Validators.max(500)]],
    note: [null, [Validators.maxLength(5000)]],
    createDate: [null, [Validators.required]],
    modifyDate: [null, [Validators.required]],
    excerciseId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected excerciseConfigService: ExcerciseConfigService,
    protected excerciseService: ExcerciseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ excerciseConfig }) => {
      this.updateForm(excerciseConfig);
    });
    this.excerciseService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IExcercise[]>) => mayBeOk.ok),
        map((response: HttpResponse<IExcercise[]>) => response.body)
      )
      .subscribe((res: IExcercise[]) => (this.excercises = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(excerciseConfig: IExcerciseConfig) {
    this.editForm.patchValue({
      id: excerciseConfig.id,
      practiceBpm: excerciseConfig.practiceBpm,
      targetBpm: excerciseConfig.targetBpm,
      minutes: excerciseConfig.minutes,
      note: excerciseConfig.note,
      createDate: excerciseConfig.createDate != null ? excerciseConfig.createDate.format(DATE_TIME_FORMAT) : null,
      modifyDate: excerciseConfig.modifyDate != null ? excerciseConfig.modifyDate.format(DATE_TIME_FORMAT) : null,
      excerciseId: excerciseConfig.excerciseId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const excerciseConfig = this.createFromForm();
    if (excerciseConfig.id !== undefined) {
      this.subscribeToSaveResponse(this.excerciseConfigService.update(excerciseConfig));
    } else {
      this.subscribeToSaveResponse(this.excerciseConfigService.create(excerciseConfig));
    }
  }

  private createFromForm(): IExcerciseConfig {
    return {
      ...new ExcerciseConfig(),
      id: this.editForm.get(['id']).value,
      practiceBpm: this.editForm.get(['practiceBpm']).value,
      targetBpm: this.editForm.get(['targetBpm']).value,
      minutes: this.editForm.get(['minutes']).value,
      note: this.editForm.get(['note']).value,
      createDate:
        this.editForm.get(['createDate']).value != null ? moment(this.editForm.get(['createDate']).value, DATE_TIME_FORMAT) : undefined,
      modifyDate:
        this.editForm.get(['modifyDate']).value != null ? moment(this.editForm.get(['modifyDate']).value, DATE_TIME_FORMAT) : undefined,
      excerciseId: this.editForm.get(['excerciseId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExcerciseConfig>>) {
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

  trackExcerciseById(index: number, item: IExcercise) {
    return item.id;
  }
}
