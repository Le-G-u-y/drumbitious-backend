import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { I18nText } from 'app/shared/model/i-18-n-text.model';
import { I18nTextService } from './i-18-n-text.service';
import { I18nTextComponent } from './i-18-n-text.component';
import { I18nTextDetailComponent } from './i-18-n-text-detail.component';
import { I18nTextUpdateComponent } from './i-18-n-text-update.component';
import { I18nTextDeletePopupComponent } from './i-18-n-text-delete-dialog.component';
import { II18nText } from 'app/shared/model/i-18-n-text.model';

@Injectable({ providedIn: 'root' })
export class I18nTextResolve implements Resolve<II18nText> {
  constructor(private service: I18nTextService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<II18nText> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<I18nText>) => response.ok),
        map((i18nText: HttpResponse<I18nText>) => i18nText.body)
      );
    }
    return of(new I18nText());
  }
}

export const i18nTextRoute: Routes = [
  {
    path: '',
    component: I18nTextComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.i18nText.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: I18nTextDetailComponent,
    resolve: {
      i18nText: I18nTextResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.i18nText.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: I18nTextUpdateComponent,
    resolve: {
      i18nText: I18nTextResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.i18nText.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: I18nTextUpdateComponent,
    resolve: {
      i18nText: I18nTextResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.i18nText.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const i18nTextPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: I18nTextDeletePopupComponent,
    resolve: {
      i18nText: I18nTextResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.i18nText.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
