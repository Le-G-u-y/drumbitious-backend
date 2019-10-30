import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IExcerciseType, ExcerciseType } from 'app/shared/model/excercise-type.model';
import { ExcerciseTypeService } from './excercise-type.service';

@Component({
  selector: 'jhi-excercise-type-update',
  templateUrl: './excercise-type-update.component.html'
})
export class ExcerciseTypeUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    excerciseTypeTextKey: [null, [Validators.required, Validators.maxLength(50)]]
  });

  constructor(protected excerciseTypeService: ExcerciseTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ excerciseType }) => {
      this.updateForm(excerciseType);
    });
  }

  updateForm(excerciseType: IExcerciseType) {
    this.editForm.patchValue({
      id: excerciseType.id,
      excerciseTypeTextKey: excerciseType.excerciseTypeTextKey
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const excerciseType = this.createFromForm();
    if (excerciseType.id !== undefined) {
      this.subscribeToSaveResponse(this.excerciseTypeService.update(excerciseType));
    } else {
      this.subscribeToSaveResponse(this.excerciseTypeService.create(excerciseType));
    }
  }

  private createFromForm(): IExcerciseType {
    return {
      ...new ExcerciseType(),
      id: this.editForm.get(['id']).value,
      excerciseTypeTextKey: this.editForm.get(['excerciseTypeTextKey']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExcerciseType>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
