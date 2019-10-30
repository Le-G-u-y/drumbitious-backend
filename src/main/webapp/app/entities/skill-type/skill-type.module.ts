import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DrumbitiousBackendSharedModule } from 'app/shared/shared.module';
import { SkillTypeComponent } from './skill-type.component';
import { SkillTypeDetailComponent } from './skill-type-detail.component';
import { SkillTypeUpdateComponent } from './skill-type-update.component';
import { SkillTypeDeletePopupComponent, SkillTypeDeleteDialogComponent } from './skill-type-delete-dialog.component';
import { skillTypeRoute, skillTypePopupRoute } from './skill-type.route';

const ENTITY_STATES = [...skillTypeRoute, ...skillTypePopupRoute];

@NgModule({
  imports: [DrumbitiousBackendSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    SkillTypeComponent,
    SkillTypeDetailComponent,
    SkillTypeUpdateComponent,
    SkillTypeDeleteDialogComponent,
    SkillTypeDeletePopupComponent
  ],
  entryComponents: [SkillTypeDeleteDialogComponent]
})
export class DrumbitiousBackendSkillTypeModule {}
