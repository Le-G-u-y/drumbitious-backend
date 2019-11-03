import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DrumbitiousBackendSharedModule } from 'app/shared/shared.module';
import { PlanComponent } from './plan.component';
import { PlanDetailComponent } from './plan-detail.component';
import { PlanUpdateComponent } from './plan-update.component';
import { PlanDeletePopupComponent, PlanDeleteDialogComponent } from './plan-delete-dialog.component';
import { planRoute, planPopupRoute } from './plan.route';

const ENTITY_STATES = [...planRoute, ...planPopupRoute];

@NgModule({
  imports: [DrumbitiousBackendSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [PlanComponent, PlanDetailComponent, PlanUpdateComponent, PlanDeleteDialogComponent, PlanDeletePopupComponent],
  entryComponents: [PlanDeleteDialogComponent]
})
export class DrumbitiousBackendPlanModule {}
