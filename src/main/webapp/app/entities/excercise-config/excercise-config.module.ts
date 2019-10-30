import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DrumbitiousBackendSharedModule } from 'app/shared/shared.module';
import { ExcerciseConfigComponent } from './excercise-config.component';
import { ExcerciseConfigDetailComponent } from './excercise-config-detail.component';
import { ExcerciseConfigUpdateComponent } from './excercise-config-update.component';
import { ExcerciseConfigDeletePopupComponent, ExcerciseConfigDeleteDialogComponent } from './excercise-config-delete-dialog.component';
import { excerciseConfigRoute, excerciseConfigPopupRoute } from './excercise-config.route';

const ENTITY_STATES = [...excerciseConfigRoute, ...excerciseConfigPopupRoute];

@NgModule({
  imports: [DrumbitiousBackendSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ExcerciseConfigComponent,
    ExcerciseConfigDetailComponent,
    ExcerciseConfigUpdateComponent,
    ExcerciseConfigDeleteDialogComponent,
    ExcerciseConfigDeletePopupComponent
  ],
  entryComponents: [ExcerciseConfigDeleteDialogComponent]
})
export class DrumbitiousBackendExcerciseConfigModule {}
