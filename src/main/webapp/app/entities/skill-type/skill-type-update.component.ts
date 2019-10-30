import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ISkillType, SkillType } from 'app/shared/model/skill-type.model';
import { SkillTypeService } from './skill-type.service';

@Component({
  selector: 'jhi-skill-type-update',
  templateUrl: './skill-type-update.component.html'
})
export class SkillTypeUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    skillTextKey: [null, [Validators.required, Validators.maxLength(50)]]
  });

  constructor(protected skillTypeService: SkillTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ skillType }) => {
      this.updateForm(skillType);
    });
  }

  updateForm(skillType: ISkillType) {
    this.editForm.patchValue({
      id: skillType.id,
      skillTextKey: skillType.skillTextKey
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const skillType = this.createFromForm();
    if (skillType.id !== undefined) {
      this.subscribeToSaveResponse(this.skillTypeService.update(skillType));
    } else {
      this.subscribeToSaveResponse(this.skillTypeService.create(skillType));
    }
  }

  private createFromForm(): ISkillType {
    return {
      ...new SkillType(),
      id: this.editForm.get(['id']).value,
      skillTextKey: this.editForm.get(['skillTextKey']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISkillType>>) {
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
