import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DrumbitiousBackendSharedModule } from 'app/shared/shared.module';
import { FinishedExerciseComponent } from './finished-exercise.component';
import { FinishedExerciseDetailComponent } from './finished-exercise-detail.component';
import { FinishedExerciseUpdateComponent } from './finished-exercise-update.component';
import { FinishedExerciseDeletePopupComponent, FinishedExerciseDeleteDialogComponent } from './finished-exercise-delete-dialog.component';
import { finishedExerciseRoute, finishedExercisePopupRoute } from './finished-exercise.route';

const ENTITY_STATES = [...finishedExerciseRoute, ...finishedExercisePopupRoute];

@NgModule({
  imports: [DrumbitiousBackendSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    FinishedExerciseComponent,
    FinishedExerciseDetailComponent,
    FinishedExerciseUpdateComponent,
    FinishedExerciseDeleteDialogComponent,
    FinishedExerciseDeletePopupComponent
  ],
  entryComponents: [FinishedExerciseDeleteDialogComponent]
})
export class DrumbitiousBackendFinishedExerciseModule {}
