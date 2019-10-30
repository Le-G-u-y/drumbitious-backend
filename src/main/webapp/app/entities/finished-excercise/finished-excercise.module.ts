import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DrumbitiousBackendSharedModule } from 'app/shared/shared.module';
import { FinishedExcerciseComponent } from './finished-excercise.component';
import { FinishedExcerciseDetailComponent } from './finished-excercise-detail.component';
import { FinishedExcerciseUpdateComponent } from './finished-excercise-update.component';
import {
  FinishedExcerciseDeletePopupComponent,
  FinishedExcerciseDeleteDialogComponent
} from './finished-excercise-delete-dialog.component';
import { finishedExcerciseRoute, finishedExcercisePopupRoute } from './finished-excercise.route';

const ENTITY_STATES = [...finishedExcerciseRoute, ...finishedExcercisePopupRoute];

@NgModule({
  imports: [DrumbitiousBackendSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    FinishedExcerciseComponent,
    FinishedExcerciseDetailComponent,
    FinishedExcerciseUpdateComponent,
    FinishedExcerciseDeleteDialogComponent,
    FinishedExcerciseDeletePopupComponent
  ],
  entryComponents: [FinishedExcerciseDeleteDialogComponent]
})
export class DrumbitiousBackendFinishedExcerciseModule {}
