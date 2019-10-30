import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { II18nText, I18nText } from 'app/shared/model/i-18-n-text.model';
import { I18nTextService } from './i-18-n-text.service';

@Component({
  selector: 'jhi-i-18-n-text-update',
  templateUrl: './i-18-n-text-update.component.html'
})
export class I18nTextUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    locale: [null, [Validators.required, Validators.maxLength(5)]],
    textKey: [null, [Validators.required]],
    textContent: [],
    createDate: [null, [Validators.required]],
    modifyDate: [null, [Validators.required]]
  });

  constructor(protected i18nTextService: I18nTextService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ i18nText }) => {
      this.updateForm(i18nText);
    });
  }

  updateForm(i18nText: II18nText) {
    this.editForm.patchValue({
      id: i18nText.id,
      locale: i18nText.locale,
      textKey: i18nText.textKey,
      textContent: i18nText.textContent,
      createDate: i18nText.createDate != null ? i18nText.createDate.format(DATE_TIME_FORMAT) : null,
      modifyDate: i18nText.modifyDate != null ? i18nText.modifyDate.format(DATE_TIME_FORMAT) : null
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const i18nText = this.createFromForm();
    if (i18nText.id !== undefined) {
      this.subscribeToSaveResponse(this.i18nTextService.update(i18nText));
    } else {
      this.subscribeToSaveResponse(this.i18nTextService.create(i18nText));
    }
  }

  private createFromForm(): II18nText {
    return {
      ...new I18nText(),
      id: this.editForm.get(['id']).value,
      locale: this.editForm.get(['locale']).value,
      textKey: this.editForm.get(['textKey']).value,
      textContent: this.editForm.get(['textContent']).value,
      createDate:
        this.editForm.get(['createDate']).value != null ? moment(this.editForm.get(['createDate']).value, DATE_TIME_FORMAT) : undefined,
      modifyDate:
        this.editForm.get(['modifyDate']).value != null ? moment(this.editForm.get(['modifyDate']).value, DATE_TIME_FORMAT) : undefined
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<II18nText>>) {
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
