import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ExcerciseConfig } from 'app/shared/model/excercise-config.model';
import { ExcerciseConfigService } from './excercise-config.service';
import { ExcerciseConfigComponent } from './excercise-config.component';
import { ExcerciseConfigDetailComponent } from './excercise-config-detail.component';
import { ExcerciseConfigUpdateComponent } from './excercise-config-update.component';
import { ExcerciseConfigDeletePopupComponent } from './excercise-config-delete-dialog.component';
import { IExcerciseConfig } from 'app/shared/model/excercise-config.model';

@Injectable({ providedIn: 'root' })
export class ExcerciseConfigResolve implements Resolve<IExcerciseConfig> {
  constructor(private service: ExcerciseConfigService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IExcerciseConfig> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ExcerciseConfig>) => response.ok),
        map((excerciseConfig: HttpResponse<ExcerciseConfig>) => excerciseConfig.body)
      );
    }
    return of(new ExcerciseConfig());
  }
}

export const excerciseConfigRoute: Routes = [
  {
    path: '',
    component: ExcerciseConfigComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.excerciseConfig.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ExcerciseConfigDetailComponent,
    resolve: {
      excerciseConfig: ExcerciseConfigResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.excerciseConfig.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ExcerciseConfigUpdateComponent,
    resolve: {
      excerciseConfig: ExcerciseConfigResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.excerciseConfig.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ExcerciseConfigUpdateComponent,
    resolve: {
      excerciseConfig: ExcerciseConfigResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.excerciseConfig.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const excerciseConfigPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ExcerciseConfigDeletePopupComponent,
    resolve: {
      excerciseConfig: ExcerciseConfigResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.excerciseConfig.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
