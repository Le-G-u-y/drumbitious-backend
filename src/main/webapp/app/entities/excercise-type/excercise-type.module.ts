import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DrumbitiousBackendSharedModule } from 'app/shared/shared.module';
import { ExcerciseTypeComponent } from './excercise-type.component';
import { ExcerciseTypeDetailComponent } from './excercise-type-detail.component';
import { ExcerciseTypeUpdateComponent } from './excercise-type-update.component';
import { ExcerciseTypeDeletePopupComponent, ExcerciseTypeDeleteDialogComponent } from './excercise-type-delete-dialog.component';
import { excerciseTypeRoute, excerciseTypePopupRoute } from './excercise-type.route';

const ENTITY_STATES = [...excerciseTypeRoute, ...excerciseTypePopupRoute];

@NgModule({
  imports: [DrumbitiousBackendSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ExcerciseTypeComponent,
    ExcerciseTypeDetailComponent,
    ExcerciseTypeUpdateComponent,
    ExcerciseTypeDeleteDialogComponent,
    ExcerciseTypeDeletePopupComponent
  ],
  entryComponents: [ExcerciseTypeDeleteDialogComponent]
})
export class DrumbitiousBackendExcerciseTypeModule {}
