import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DrumbitiousBackendSharedModule } from 'app/shared/shared.module';
import { ExerciseComponent } from './exercise.component';
import { ExerciseDetailComponent } from './exercise-detail.component';
import { ExerciseUpdateComponent } from './exercise-update.component';
import { ExerciseDeletePopupComponent, ExerciseDeleteDialogComponent } from './exercise-delete-dialog.component';
import { exerciseRoute, exercisePopupRoute } from './exercise.route';

const ENTITY_STATES = [...exerciseRoute, ...exercisePopupRoute];

@NgModule({
  imports: [DrumbitiousBackendSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ExerciseComponent,
    ExerciseDetailComponent,
    ExerciseUpdateComponent,
    ExerciseDeleteDialogComponent,
    ExerciseDeletePopupComponent
  ],
  entryComponents: [ExerciseDeleteDialogComponent]
})
export class DrumbitiousBackendExerciseModule {}
