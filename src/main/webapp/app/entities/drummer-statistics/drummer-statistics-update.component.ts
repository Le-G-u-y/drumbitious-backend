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
import { IDrummerStatistics, DrummerStatistics } from 'app/shared/model/drummer-statistics.model';
import { DrummerStatisticsService } from './drummer-statistics.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-drummer-statistics-update',
  templateUrl: './drummer-statistics-update.component.html'
})
export class DrummerStatisticsUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    selfAssessedLevelSpeed: [null, [Validators.min(0), Validators.max(10)]],
    selfAssessedLevelGroove: [null, [Validators.min(0), Validators.max(10)]],
    selfAssessedLevelCreativity: [null, [Validators.min(0), Validators.max(10)]],
    selfAssessedLevelAdaptability: [null, [Validators.min(0), Validators.max(10)]],
    selfAssessedLevelDynamics: [null, [Validators.min(0), Validators.max(10)]],
    selfAssessedLevelIndependence: [null, [Validators.min(0), Validators.max(10)]],
    selfAssessedLevelLivePerformance: [null, [Validators.min(0), Validators.max(10)]],
    selfAssessedLevelReadingMusic: [null, [Validators.min(0), Validators.max(10)]],
    note: [null, [Validators.maxLength(10000)]],
    createDate: [null, [Validators.required]],
    modifyDate: [null, [Validators.required]],
    userId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected drummerStatisticsService: DrummerStatisticsService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ drummerStatistics }) => {
      this.updateForm(drummerStatistics);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(drummerStatistics: IDrummerStatistics) {
    this.editForm.patchValue({
      id: drummerStatistics.id,
      selfAssessedLevelSpeed: drummerStatistics.selfAssessedLevelSpeed,
      selfAssessedLevelGroove: drummerStatistics.selfAssessedLevelGroove,
      selfAssessedLevelCreativity: drummerStatistics.selfAssessedLevelCreativity,
      selfAssessedLevelAdaptability: drummerStatistics.selfAssessedLevelAdaptability,
      selfAssessedLevelDynamics: drummerStatistics.selfAssessedLevelDynamics,
      selfAssessedLevelIndependence: drummerStatistics.selfAssessedLevelIndependence,
      selfAssessedLevelLivePerformance: drummerStatistics.selfAssessedLevelLivePerformance,
      selfAssessedLevelReadingMusic: drummerStatistics.selfAssessedLevelReadingMusic,
      note: drummerStatistics.note,
      createDate: drummerStatistics.createDate != null ? drummerStatistics.createDate.format(DATE_TIME_FORMAT) : null,
      modifyDate: drummerStatistics.modifyDate != null ? drummerStatistics.modifyDate.format(DATE_TIME_FORMAT) : null,
      userId: drummerStatistics.userId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const drummerStatistics = this.createFromForm();
    if (drummerStatistics.id !== undefined) {
      this.subscribeToSaveResponse(this.drummerStatisticsService.update(drummerStatistics));
    } else {
      this.subscribeToSaveResponse(this.drummerStatisticsService.create(drummerStatistics));
    }
  }

  private createFromForm(): IDrummerStatistics {
    return {
      ...new DrummerStatistics(),
      id: this.editForm.get(['id']).value,
      selfAssessedLevelSpeed: this.editForm.get(['selfAssessedLevelSpeed']).value,
      selfAssessedLevelGroove: this.editForm.get(['selfAssessedLevelGroove']).value,
      selfAssessedLevelCreativity: this.editForm.get(['selfAssessedLevelCreativity']).value,
      selfAssessedLevelAdaptability: this.editForm.get(['selfAssessedLevelAdaptability']).value,
      selfAssessedLevelDynamics: this.editForm.get(['selfAssessedLevelDynamics']).value,
      selfAssessedLevelIndependence: this.editForm.get(['selfAssessedLevelIndependence']).value,
      selfAssessedLevelLivePerformance: this.editForm.get(['selfAssessedLevelLivePerformance']).value,
      selfAssessedLevelReadingMusic: this.editForm.get(['selfAssessedLevelReadingMusic']).value,
      note: this.editForm.get(['note']).value,
      createDate:
        this.editForm.get(['createDate']).value != null ? moment(this.editForm.get(['createDate']).value, DATE_TIME_FORMAT) : undefined,
      modifyDate:
        this.editForm.get(['modifyDate']).value != null ? moment(this.editForm.get(['modifyDate']).value, DATE_TIME_FORMAT) : undefined,
      userId: this.editForm.get(['userId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDrummerStatistics>>) {
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
