import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DrumbitiousBackendSharedModule } from 'app/shared/shared.module';
import { I18nTextComponent } from './i-18-n-text.component';
import { I18nTextDetailComponent } from './i-18-n-text-detail.component';
import { I18nTextUpdateComponent } from './i-18-n-text-update.component';
import { I18nTextDeletePopupComponent, I18nTextDeleteDialogComponent } from './i-18-n-text-delete-dialog.component';
import { i18nTextRoute, i18nTextPopupRoute } from './i-18-n-text.route';

const ENTITY_STATES = [...i18nTextRoute, ...i18nTextPopupRoute];

@NgModule({
  imports: [DrumbitiousBackendSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    I18nTextComponent,
    I18nTextDetailComponent,
    I18nTextUpdateComponent,
    I18nTextDeleteDialogComponent,
    I18nTextDeletePopupComponent
  ],
  entryComponents: [I18nTextDeleteDialogComponent]
})
export class DrumbitiousBackendI18nTextModule {}
