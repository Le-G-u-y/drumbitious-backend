import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DrumbitiousBackendSharedModule } from 'app/shared/shared.module';
import { FinishedSessionComponent } from './finished-session.component';
import { FinishedSessionDetailComponent } from './finished-session-detail.component';
import { FinishedSessionUpdateComponent } from './finished-session-update.component';
import { FinishedSessionDeletePopupComponent, FinishedSessionDeleteDialogComponent } from './finished-session-delete-dialog.component';
import { finishedSessionRoute, finishedSessionPopupRoute } from './finished-session.route';

const ENTITY_STATES = [...finishedSessionRoute, ...finishedSessionPopupRoute];

@NgModule({
  imports: [DrumbitiousBackendSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    FinishedSessionComponent,
    FinishedSessionDetailComponent,
    FinishedSessionUpdateComponent,
    FinishedSessionDeleteDialogComponent,
    FinishedSessionDeletePopupComponent
  ],
  entryComponents: [FinishedSessionDeleteDialogComponent]
})
export class DrumbitiousBackendFinishedSessionModule {}
