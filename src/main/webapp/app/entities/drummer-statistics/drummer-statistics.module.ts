import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DrumbitiousBackendSharedModule } from 'app/shared/shared.module';
import { DrummerStatisticsComponent } from './drummer-statistics.component';
import { DrummerStatisticsDetailComponent } from './drummer-statistics-detail.component';
import { DrummerStatisticsUpdateComponent } from './drummer-statistics-update.component';
import {
  DrummerStatisticsDeletePopupComponent,
  DrummerStatisticsDeleteDialogComponent
} from './drummer-statistics-delete-dialog.component';
import { drummerStatisticsRoute, drummerStatisticsPopupRoute } from './drummer-statistics.route';

const ENTITY_STATES = [...drummerStatisticsRoute, ...drummerStatisticsPopupRoute];

@NgModule({
  imports: [DrumbitiousBackendSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DrummerStatisticsComponent,
    DrummerStatisticsDetailComponent,
    DrummerStatisticsUpdateComponent,
    DrummerStatisticsDeleteDialogComponent,
    DrummerStatisticsDeletePopupComponent
  ],
  entryComponents: [DrummerStatisticsDeleteDialogComponent]
})
export class DrumbitiousBackendDrummerStatisticsModule {}
