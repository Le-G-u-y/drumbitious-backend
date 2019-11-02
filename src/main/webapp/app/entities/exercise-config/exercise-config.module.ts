import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DrumbitiousBackendSharedModule } from 'app/shared/shared.module';
import { ExerciseConfigComponent } from './exercise-config.component';
import { ExerciseConfigDetailComponent } from './exercise-config-detail.component';
import { ExerciseConfigUpdateComponent } from './exercise-config-update.component';
import { ExerciseConfigDeletePopupComponent, ExerciseConfigDeleteDialogComponent } from './exercise-config-delete-dialog.component';
import { exerciseConfigRoute, exerciseConfigPopupRoute } from './exercise-config.route';

const ENTITY_STATES = [...exerciseConfigRoute, ...exerciseConfigPopupRoute];

@NgModule({
  imports: [DrumbitiousBackendSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ExerciseConfigComponent,
    ExerciseConfigDetailComponent,
    ExerciseConfigUpdateComponent,
    ExerciseConfigDeleteDialogComponent,
    ExerciseConfigDeletePopupComponent
  ],
  entryComponents: [ExerciseConfigDeleteDialogComponent]
})
export class DrumbitiousBackendExerciseConfigModule {}
