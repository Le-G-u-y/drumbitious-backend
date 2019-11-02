import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ExerciseConfig } from 'app/shared/model/exercise-config.model';
import { ExerciseConfigService } from './exercise-config.service';
import { ExerciseConfigComponent } from './exercise-config.component';
import { ExerciseConfigDetailComponent } from './exercise-config-detail.component';
import { ExerciseConfigUpdateComponent } from './exercise-config-update.component';
import { ExerciseConfigDeletePopupComponent } from './exercise-config-delete-dialog.component';
import { IExerciseConfig } from 'app/shared/model/exercise-config.model';

@Injectable({ providedIn: 'root' })
export class ExerciseConfigResolve implements Resolve<IExerciseConfig> {
  constructor(private service: ExerciseConfigService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IExerciseConfig> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ExerciseConfig>) => response.ok),
        map((exerciseConfig: HttpResponse<ExerciseConfig>) => exerciseConfig.body)
      );
    }
    return of(new ExerciseConfig());
  }
}

export const exerciseConfigRoute: Routes = [
  {
    path: '',
    component: ExerciseConfigComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.exerciseConfig.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ExerciseConfigDetailComponent,
    resolve: {
      exerciseConfig: ExerciseConfigResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.exerciseConfig.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ExerciseConfigUpdateComponent,
    resolve: {
      exerciseConfig: ExerciseConfigResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.exerciseConfig.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ExerciseConfigUpdateComponent,
    resolve: {
      exerciseConfig: ExerciseConfigResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.exerciseConfig.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const exerciseConfigPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ExerciseConfigDeletePopupComponent,
    resolve: {
      exerciseConfig: ExerciseConfigResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.exerciseConfig.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
