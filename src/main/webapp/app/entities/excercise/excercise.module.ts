import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DrumbitiousBackendSharedModule } from 'app/shared/shared.module';
import { ExcerciseComponent } from './excercise.component';
import { ExcerciseDetailComponent } from './excercise-detail.component';
import { ExcerciseUpdateComponent } from './excercise-update.component';
import { ExcerciseDeletePopupComponent, ExcerciseDeleteDialogComponent } from './excercise-delete-dialog.component';
import { excerciseRoute, excercisePopupRoute } from './excercise.route';

const ENTITY_STATES = [...excerciseRoute, ...excercisePopupRoute];

@NgModule({
  imports: [DrumbitiousBackendSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ExcerciseComponent,
    ExcerciseDetailComponent,
    ExcerciseUpdateComponent,
    ExcerciseDeleteDialogComponent,
    ExcerciseDeletePopupComponent
  ],
  entryComponents: [ExcerciseDeleteDialogComponent]
})
export class DrumbitiousBackendExcerciseModule {}
